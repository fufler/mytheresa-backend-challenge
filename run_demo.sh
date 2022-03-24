#!/bin/sh -e

mvn                       \
  -P docker               \
  -D maven.test.skip=true \
  -D it.skip=true         \
  clean verify

./utils/generate_test_data.py    \
  --categories-number 10000      \
  --products-number 100000       \
  --boots 20                     \
   > demo/initial_data_100k.json

docker-compose                   \
  -f demo/docker-compose.yml     \
  -p mytheresa-backend-challenge \
  up                             \
  --force-recreate               \
  --renew-anon-volumes
