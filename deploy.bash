#!/bin/bash -eu


pushd client
npm install
npm run build
popd

cp -r nginx/api/ client/dist/shortify/

docker-compose up
