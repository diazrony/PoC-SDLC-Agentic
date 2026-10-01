#!/usr/bin/env bash
# =============================================================================
# new-module.sh
# -----------------------------------------------------------------------------
# Crea el esqueleto de una aplicacion o de una libreria nueva y la registra en
# el agregador correspondiente. Evita que cada equipo invente su propia
# estructura, que es como un monorepo deja de serlo y pasa a ser una carpeta
# con proyectos dentro.
#
# Uso:
#   scripts/new-module.sh app <nombre> <paquete-java>
#   scripts/new-module.sh lib <nombre>
#
# Ejemplos:
#   scripts/new-module.sh app billing com.example.billing
#   scripts/new-module.sh lib caching
# =============================================================================
set -euo pipefail

KIND="${1:-}"
NAME="${2:-}"
PACKAGE="${3:-}"

if [[ -z "$KIND" || -z "$NAME" ]]; then
  sed -n '2,20p' "$0"
  exit 2
fi

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$REPO_ROOT"

case "$KIND" in
  app)
    [[ -z "$PACKAGE" ]] && { echo "Falta el paquete Java" >&2; exit 2; }
    TARGET="apps/$NAME"
    PKG_PATH="${PACKAGE//.//}"
    mkdir -p "$TARGET/src/main/java/$PKG_PATH"/{domain/{model,service,exception},application/{dto,port/in,port/out,service,mapper},infrastructure/{adapter/in/rest,adapter/out/persistence,persistence/{entity,repository,mapper},config}}
    mkdir -p "$TARGET/src/main/resources" "$TARGET/src/main/docker" "$TARGET/src/test/java/$PKG_PATH"
    cat > "$TARGET/pom.xml" <<POM
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.example.monorepo.apps</groupId>
    <artifactId>apps</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <relativePath>../pom.xml</relativePath>
  </parent>
  <artifactId>app-$NAME</artifactId>
  <name>app-$NAME</name>
</project>
POM
    sed -i "s|  </modules>|    <module>$NAME</module>\n  </modules>|" apps/pom.xml
    echo "Aplicacion creada en $TARGET y registrada en apps/pom.xml"
    echo "Recuerda anadir el equipo responsable en .github/CODEOWNERS"
    ;;

  lib)
    TARGET="libs/$NAME"
    PKG_PATH="com/example/monorepo/${NAME//-/}"
    mkdir -p "$TARGET/src/main/java/$PKG_PATH" "$TARGET/src/test/java/$PKG_PATH"
    cat > "$TARGET/pom.xml" <<POM
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.example.monorepo.libs</groupId>
    <artifactId>libs</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <relativePath>../pom.xml</relativePath>
  </parent>
  <artifactId>lib-$NAME</artifactId>
  <name>lib-$NAME</name>
  <dependencies>
    <dependency>
      <groupId>com.example.monorepo.libs</groupId>
      <artifactId>lib-common-core</artifactId>
    </dependency>
  </dependencies>
</project>
POM
    sed -i "s|  </modules>|    <module>$NAME</module>\n  </modules>|" libs/pom.xml
    echo "Libreria creada en $TARGET y registrada en libs/pom.xml"
    echo "Recuerda declarar su version en el dependencyManagement del POM raiz"
    ;;

  *)
    echo "Tipo desconocido: $KIND (usa app o lib)" >&2
    exit 2
    ;;
esac
