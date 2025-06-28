export VAULT_ADDR="http://host.docker.internal:8200"
export VAULT_TOKEN="root"

vault kv put secret/auth-service \
  jwt.public-key="$(awk '!/-----/ {printf "%s", $0}' /public.pem)" \
  jwt.private-key="$(awk '!/-----/ {printf "%s", $0}' /private.pem)"

vault kv put secret/api-gateway \
  jwt.public-key="$(awk '!/-----/ {printf "%s", $0}' /public.pem)"