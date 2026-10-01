#!/usr/bin/env bash
# =============================================================================
# build-affected.sh
# -----------------------------------------------------------------------------
# Construye y verifica unicamente los proyectos afectados por los cambios.
# Es el comando que ejecuta el pipeline de Pull Request.
#
# Uso:
#   scripts/build-affected.sh [--base origin/main] [goal...]
#
# Ejemplos:
#   scripts/build-affected.sh                      # verify de lo afectado
#   scripts/build-affected.sh --base origin/main test
# =============================================================================
set -euo pipefail

BASE_REF="origin/main"
if [[ "${1:-}" == "--base" ]]; then
  BASE_REF="$2"; shift 2
fi
GOALS=("$@")
[[ ${#GOALS[@]} -eq 0 ]] && GOALS=("verify")

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$REPO_ROOT"

MAVEN_ARGS="$(scripts/affected-projects.sh --base "$BASE_REF" --format maven)"

if [[ -z "$MAVEN_ARGS" ]]; then
  echo "No hay modulos afectados. Nada que construir."
  exit 0
fi

echo "Modulos afectados:"
scripts/affected-projects.sh --base "$BASE_REF" --format list | sed 's/^/  - /'
echo
echo "Ejecutando: ./mvnw $MAVEN_ARGS ${GOALS[*]}"
# shellcheck disable=SC2086
exec ./mvnw -B $MAVEN_ARGS "${GOALS[@]}"
