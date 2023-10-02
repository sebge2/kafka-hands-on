#!/bin/sh

CURRENT_DIRECTORY="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Setup ACL"
bash ${CURRENT_DIRECTORY}/setup-schema.sh