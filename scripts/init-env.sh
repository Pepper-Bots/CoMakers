#!/bin/sh
set -eu
root=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
if [ -e "$root/.env" ]; then
  echo ".env already exists; preserved without changes."
  exit 0
fi
command -v openssl >/dev/null 2>&1 || { echo "Install OpenSSL to generate local secrets." >&2; exit 1; }
db_password=$(openssl rand -base64 32)
root_password=$(openssl rand -base64 32)
jwt_secret=$(openssl rand -base64 32)
umask 077
# noclobber prevents overwriting if another process creates the file.
set -C
{
  printf '%s\n' '# Local Docker credentials. Do not commit or share.'
  printf 'DB_PASSWORD=%s\nMYSQL_ROOT_PASSWORD=%s\nJWT_SECRET=%s\n' "$db_password" "$root_password" "$jwt_secret"
  printf '%s\n' 'DB_PORT=3307' 'API_PORT=8081'
} > "$root/.env"
echo ".env created with random local credentials. Values were not printed."
