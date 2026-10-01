#!/usr/bin/env bash
# =============================================================================
# affected-projects.sh
# -----------------------------------------------------------------------------
# Calcula que modulos del monorepo estan AFECTADOS por un conjunto de cambios.
#
#   Git Diff
#      |
#      v
#   Detect Changed Projects      <- este script
#      |
#      v
#   Calculate Dependency Graph   <- Maven Reactor (-am / -amd)
#      |
#      v
#   Determine Affected Projects
#      |
#      v
#   Build / Test only affected projects
#
# Division de responsabilidades: git sabe QUE ha cambiado, Maven sabe QUIEN
# depende de ello. Este script une ambas piezas y no reimplementa el grafo.
#
# Uso:
#   scripts/affected-projects.sh [--base <ref>] [--format list|maven|github]
#
#   --base    Referencia de comparacion. Por defecto origin/main.
#             Se usa el merge-base (tres puntos) para no marcar como afectado
#             lo que simplemente ha avanzado en la rama destino.
#   --format  list   -> una ruta de modulo por linea (por defecto)
#             maven  -> argumentos listos para ./mvnw
#             github -> escribe en $GITHUB_OUTPUT para GitHub Actions
#
# Ejemplos:
#   scripts/affected-projects.sh --base origin/main --format maven
#   ./mvnw $(scripts/affected-projects.sh --format maven) verify
# =============================================================================
set -euo pipefail

BASE_REF="${BASE_REF:-origin/main}"
FORMAT="list"

while [[ $# -gt 0 ]]; do
  case "$1" in
    --base)   BASE_REF="$2"; shift 2 ;;
    --format) FORMAT="$2";   shift 2 ;;
    -h|--help) sed -n '2,36p' "$0"; exit 0 ;;
    *) echo "Argumento desconocido: $1" >&2; exit 2 ;;
  esac
done

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$REPO_ROOT"

# ---------------------------------------------------------------------------
# 1. Inventario de modulos construibles
# ---------------------------------------------------------------------------
ALL_MODULES=()
for pom in apps/*/pom.xml libs/*/pom.xml; do
  [[ -f "$pom" ]] && ALL_MODULES+=("$(dirname "$pom")")
done

# ---------------------------------------------------------------------------
# 2. Ficheros modificados respecto a la base
# ---------------------------------------------------------------------------
if ! git rev-parse --verify --quiet "$BASE_REF" >/dev/null; then
  echo "AVISO: la referencia '$BASE_REF' no existe; se construye todo." >&2
  CHANGED_FILES=""
  FORCE_ALL=1
else
  CHANGED_FILES="$(git diff --name-only "$BASE_REF"...HEAD)"
  FORCE_ALL=0
fi

# ---------------------------------------------------------------------------
# 3. Cambios globales: obligan a reconstruir el monorepo completo
#    (POM raiz, agregadores, wrapper, pipelines, scripts de build)
# ---------------------------------------------------------------------------
GLOBAL_PATTERN='^(pom\.xml|apps/pom\.xml|libs/pom\.xml|\.mvn/|mvnw|mvnw\.cmd|scripts/|\.github/workflows/)'
if [[ "$FORCE_ALL" -eq 1 ]] || echo "$CHANGED_FILES" | grep -Eq "$GLOBAL_PATTERN"; then
  CHANGED_MODULES=("${ALL_MODULES[@]}")
else
  declare -A SEEN=()
  CHANGED_MODULES=()
  while IFS= read -r file; do
    [[ -z "$file" ]] && continue
    for module in "${ALL_MODULES[@]}"; do
      if [[ "$file" == "$module/"* ]]; then
        if [[ -z "${SEEN[$module]:-}" ]]; then
          SEEN[$module]=1
          CHANGED_MODULES+=("$module")
        fi
      fi
    done
  done <<< "$CHANGED_FILES"
fi

# ---------------------------------------------------------------------------
# 4. Salida
# ---------------------------------------------------------------------------
if [[ ${#CHANGED_MODULES[@]} -eq 0 ]]; then
  case "$FORMAT" in
    maven)  echo "" ;;
    github) { echo "affected=false"; echo "modules="; echo "maven_args="; } >> "${GITHUB_OUTPUT:-/dev/stdout}" ;;
    *)      : ;;
  esac
  exit 0
fi

PROJECT_LIST="$(IFS=,; echo "${CHANGED_MODULES[*]}")"

# -pl  : modulos semilla (los que han cambiado)
# -am  : ademas construye sus DEPENDENCIAS  (upstream) -> necesarias para compilar
# -amd : ademas construye sus DEPENDIENTES  (downstream) -> hay que revalidarlos
MAVEN_ARGS="-pl $PROJECT_LIST -am -amd"

case "$FORMAT" in
  list)
    printf '%s\n' "${CHANGED_MODULES[@]}"
    ;;
  maven)
    echo "$MAVEN_ARGS"
    ;;
  github)
    {
      echo "affected=true"
      echo "modules=$PROJECT_LIST"
      echo "maven_args=$MAVEN_ARGS"
    } >> "${GITHUB_OUTPUT:-/dev/stdout}"
    ;;
  *)
    echo "Formato desconocido: $FORMAT" >&2
    exit 2
    ;;
esac
