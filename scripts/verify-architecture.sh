#!/usr/bin/env bash
# =============================================================================
# verify-architecture.sh
# -----------------------------------------------------------------------------
# Comprueba las fronteras del monorepo con tres mecanismos complementarios:
#
#   1. Grep sobre los POM   -> detecta la dependencia prohibida antes de compilar
#                              y da un mensaje inmediato al desarrollador.
#   2. Maven Enforcer       -> bloquea apps -> apps y libs -> apps en el build,
#                              incluyendo dependencias transitivas.
#   3. ArchUnit             -> bloquea el acoplamiento a nivel de codigo fuente
#                              (capas hexagonales, adaptadores, entidades JPA).
#
# Uso: scripts/verify-architecture.sh
# =============================================================================
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$REPO_ROOT"

FAILURES=0

echo "== 1/3  apps -> apps (inspeccion de POM) =="
for pom in apps/*/pom.xml; do
  if grep -q "<groupId>com.example.monorepo.apps</groupId>" "$pom" \
     && grep -A1 "<groupId>com.example.monorepo.apps</groupId>" "$pom" | grep -q "<artifactId>app-"; then
    echo "  VIOLACION: $pom depende de otra aplicacion"
    FAILURES=$((FAILURES + 1))
  fi
done
[[ $FAILURES -eq 0 ]] && echo "  OK: ninguna aplicacion depende de otra aplicacion"

echo "== 2/3  libs -> apps (inspeccion de POM) =="
LIB_VIOLATIONS=$(grep -l "com.example.monorepo.apps" libs/*/pom.xml 2>/dev/null || true)
if [[ -n "$LIB_VIOLATIONS" ]]; then
  echo "  VIOLACION: estas librerias dependen de aplicaciones:"
  echo "$LIB_VIOLATIONS" | sed 's/^/    /'
  FAILURES=$((FAILURES + 1))
else
  echo "  OK: ninguna libreria depende de una aplicacion"
fi

echo "== 3/3  Enforcer + ArchUnit (build completo) =="
./mvnw -B -q validate
./mvnw -B test -Dtest='ArchitectureTest' -DfailIfNoTests=false

if [[ $FAILURES -gt 0 ]]; then
  echo
  echo "Se han detectado $FAILURES violaciones arquitectonicas."
  exit 1
fi
echo
echo "Arquitectura verificada correctamente."
