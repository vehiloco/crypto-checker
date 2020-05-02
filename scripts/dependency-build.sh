#!/bin/bash

set -e

export JSR308=$(cd $(dirname "$0")/../../ && pwd)
export CHECKERFRAMEWORK=${JSR308}/checker-framework

export REPO_SITE="${REPO_SITE:-opprop}"

echo "------ Downloading everything from REPO_SITE: $REPO_SITE ------"

if [[ -d ${CHECKERFRAMEWORK} ]] ; then
    (cd ${CHECKERFRAMEWORK} && git pull)
else
    BRANCH=PropertyFileHandler
    (cd ${JSR308} && git clone -b ${BRANCH} --depth 99 https://github.com/"$REPO_SITE"/checker-framework.git)
fi

(cd ${CHECKERFRAMEWORK} && source checker/bin-devel/build.sh)
