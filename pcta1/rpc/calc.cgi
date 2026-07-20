#!/usr/bin/env bash
# calc.cgi — Reto de extensión: servicio multi-operación con idempotencia
# URL: /rpc/calc.cgi?op=sum|sub|mul|div&a=<num>&b=<num>
# Header opcional: Idempotency-Key: <clave>

set -euo pipefail

qs="${QUERY_STRING:-}"
if [ "${REQUEST_METHOD:-GET}" = "POST" ]; then
  qs=$(head -c "${CONTENT_LENGTH:-0}")
fi

param() { echo "$1" | sed -n "s/.*${2}=\([^&]*\).*/\1/p"; }

op=$(param "$qs" op)
a=$(param "$qs" a)
b=$(param "$qs" b)
idem="${HTTP_IDEMPOTENCY_KEY:-}"

cache=/var/tmp/rpc-cache
mkdir -p "$cache"
if [ -n "$idem" ] && [ -f "$cache/$idem" ]; then
  printf 'Content-Type: application/json; charset=UTF-8\r\n\r\n'
  cat "$cache/$idem"
  exit 0
fi

if [ -z "$op" ] || [ -z "$a" ] || [ -z "$b" ]; then
  printf 'Status: 400 Bad Request\r\n'
  printf 'Content-Type: application/json; charset=UTF-8\r\n\r\n'
  echo '{"error":"op, a y b son obligatorios"}'
  exit 0
fi

resultado=""
case "$op" in
  sum) resultado=$(echo "$a + $b" | bc -l) ;;
  sub) resultado=$(echo "$a - $b" | bc -l) ;;
  mul) resultado=$(echo "$a * $b" | bc -l) ;;
  div)
    if [ "$(echo "$b == 0" | bc -l)" = "1" ]; then
      printf 'Status: 422 Unprocessable Entity\r\n'
      printf 'Content-Type: application/json; charset=UTF-8\r\n\r\n'
      echo '{"error":"división entre cero"}'
      exit 0
    fi
    resultado=$(echo "$a / $b" | bc -l)
    ;;
  *)
    printf 'Status: 400 Bad Request\r\n'
    printf 'Content-Type: application/json; charset=UTF-8\r\n\r\n'
    echo '{"error":"operación no soportada"}'
    exit 0
    ;;
esac

resp="{\"servicio\":\"calc\",\"op\":\"$op\",\"a\":$a,\"b\":$b,\"resultado\":$resultado}"
[ -n "$idem" ] && echo "$resp" > "$cache/$idem"

printf 'Content-Type: application/json; charset=UTF-8\r\n\r\n'
echo "$resp"
