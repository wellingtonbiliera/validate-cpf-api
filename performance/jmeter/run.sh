#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

JMETER_HOST="${JMETER_HOST:-localhost}"
JMETER_PORT="${JMETER_PORT:-8080}"
JMETER_THREADS="${JMETER_THREADS:-20}"
JMETER_RAMP_UP="${JMETER_RAMP_UP:-10}"
JMETER_DURATION="${JMETER_DURATION:-60}"

RESULTS_DIR="${ROOT_DIR}/results"
mkdir -p "${RESULTS_DIR}/report"

echo "Rodando JMeter..."
echo "  host=${JMETER_HOST} port=${JMETER_PORT}"
echo "  threads=${JMETER_THREADS} rampUp=${JMETER_RAMP_UP}s duration=${JMETER_DURATION}s"

docker run --rm \
  -u "$(id -u):$(id -g)" \
  -v "${ROOT_DIR}:/jmeter" \
  -w /jmeter \
  justb4/jmeter:latest \
  -n \
  -t /jmeter/valida-cpf.jmx \
  -l /jmeter/results/results.jtl \
  -e -o /jmeter/results/report \
  -Jhost="${JMETER_HOST}" \
  -Jport="${JMETER_PORT}" \
  -Jthreads="${JMETER_THREADS}" \
  -JrampUp="${JMETER_RAMP_UP}" \
  -Jduration="${JMETER_DURATION}"

echo "OK. Relatório em: ${RESULTS_DIR}/report/index.html"
