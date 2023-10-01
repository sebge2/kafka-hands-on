#!/bin/sh

# shellcheck disable=SC2039
CURRENT_DIRECTORY="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT="kafka"

echo "Setup SSL"
bash ${CURRENT_DIRECTORY}/setup-ssl-certificates.sh

docker-compose -p $PROJECT up -d --remove-orphans
