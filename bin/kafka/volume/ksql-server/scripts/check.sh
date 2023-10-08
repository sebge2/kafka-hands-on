#!/bin/sh

CURRENT_DIRECTORY="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

set -e

echo "Setup Tables/Streams"
bash ${CURRENT_DIRECTORY}/setup-stream-table.sh