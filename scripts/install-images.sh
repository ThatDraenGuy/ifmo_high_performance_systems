#!/bin/bash

cd ..

eval $(minikube docker-env)


build() {
  local last_id=$(docker inspect --format {{.Id}} $1)
  docker build -f docker/$1.Dockerfile -t $1 .
  local curr_id=$(docker inspect --format {{.Id}} $1)
  if [ "$last_id" != "$curr_id" ]; then
    echo "found changes for $1"
    kubectl rollout restart deployment/$1
  else
    echo "no changes for $1"
  fi
}

build account-service
build billing-service
build cdr-service
build config-server
build eureka-server
build file-service
build gateway
build notification-service
