#!/usr/bin/env bash

DIR="$(cd "`dirname "$0"`"; pwd)"
cd $DIR

function join_by {
  local d=${1-} f=${2-}
  if shift 2; then
    printf %s "$f" "${@/#/$d}"
  fi
}

# ========================================

# local | dev | prod
export APP_MODE=dev
CONF=application.conf

MAIN_CLASS=org.altera.app.TestApp

JARS="
pure-java-template-1.0.jar
$CONF
"

# ========================================

java -cp ".:$(join_by ':' ${JARS[*]})" $MAIN_CLASS
