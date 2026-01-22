#!/bin/bash

echo "🧪 Testando API e verificando dados no Oracle..."
echo ""

if ! docker ps | grep -q valida-cpf-oracle; then
    echo "❌ Oracle não está rodando!"
    echo "📦 Execute: docker compose up -d oracle"
    exit 1
fi

echo "✅ Oracle está rodando!"
echo ""
echo "📊 Verificando dados ANTES do teste..."
echo "=========================================="
docker exec -i valida-cpf-oracle sqlplus -s system/Oracle123@XE <<EOF
SET PAGESIZE 1000
SELECT COUNT(*) as total_antes FROM VALIDA_CPF;
EXIT;
EOF

echo ""
echo "🚀 Testando API (aguarde alguns segundos para a aplicação iniciar)..."
echo ""

curl -X POST http://localhost:8080/api/validar-cpf \
  -H "Content-Type: application/json" \
  -d '{"cpf": "12345678909"}' \
  2>/dev/null | jq '.' || echo "⚠️ API não está respondendo (pode estar iniciando)"

sleep 2

echo ""
echo "📊 Verificando dados DEPOIS do teste..."
echo "=========================================="
docker exec -i valida-cpf-oracle sqlplus -s system/Oracle123@XE <<EOF
SET PAGESIZE 1000
SET LINESIZE 200

PROMPT Total de registros:
SELECT COUNT(*) as total_depois FROM VALIDA_CPF;

PROMPT 
PROMPT Últimos registros salvos:
SELECT 
    ID,
    CPF,
    CASE WHEN VALIDO = 1 THEN 'SIM' ELSE 'NÃO' END as VALIDO,
    TO_CHAR(DATA_VALIDACAO, 'DD/MM/YYYY HH24:MI:SS') as DATA_VALIDACAO,
    SUBSTR(MENSAGEM, 1, 40) as MENSAGEM,
    NOME,
    SITUACAO
FROM VALIDA_CPF 
ORDER BY DATA_VALIDACAO DESC 
FETCH FIRST 5 ROWS ONLY;

EXIT;
EOF

echo ""
echo "✅ Verificação concluída!"
