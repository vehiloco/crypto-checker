#!/bin/bash

set -e

CORPUS_FILE_NAME=$1

docker run -v $HOME/.gradle:/root/.gradle -v $HOME/.m2:/root/.m2 ${DOCKER_IMAGE} /bin/bash -c "true &&
  set -e
  if [ ${TRAVIS_EVENT_TYPE} = pull_request ] ; then
    git clone --quiet https://xingweitian:${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git ${THIS_REPO} || git clone --quiet https://xingweitian:${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git ${THIS_REPO}
    cd ${THIS_REPO}
    git fetch origin +refs/pull/${TRAVIS_PULL_REQUEST}/merge
    git checkout -qf ${TRAVIS_PULL_REQUEST_SHA}
    git remote add theupstream https://xingweitian:${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git
    git pull theupstream ${TRAVIS_BRANCH} || git pull theupstream ${TRAVIS_BRANCH}
  else
    git clone --quiet -b ${TRAVIS_BRANCH} https://xingweitian:${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git ${THIS_REPO} || git clone --quiet -b ${TRAVIS_BRANCH} https://xingweitian:${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git ${THIS_REPO}
    cd ${THIS_REPO}
    git checkout -qf ${TRAVIS_COMMIT}
  fi &&
  ./gradlew test
  ./scripts/build.sh ${CORPUS_FILE_NAME}
"
