#!/bin/bash

# Script para testar a API com Oracle real do docker-compose

echo "🔍 Verificando se o Oracle está rodando..."
if ! docker ps | grep -q valida-cpf-oracle; then
    echo "❌ Oracle não está rodando!"
    echo "📦 Iniciando docker-compose..."
    docker compose up -d oracle
    echo "⏳ Aguardando Oracle ficar pronto (pode levar 1-2 minutos)..."
    sleep 60
fi

echo "✅ Oracle está rodando!"
echo ""
echo "🧪 Executando teste de integração real com Oracle..."
echo ""

./gradlew test --tests OracleRealIntegrationTest -Dspring.profiles.active=oracle-test --no-daemon

echo ""
echo "📊 Verificando registros no Oracle..."
docker exec -i valida-cpf-oracle sqlplus -s system/Oracle123@XE <<EOF
SELECT COUNT(*) as total_registros FROM VALIDA_CPF;
SELECT * FROM VALIDA_CPF ORDER BY DATA_VALIDACAO DESC FETCH FIRST 5 ROWS ONLY;
EXIT;
EOF
