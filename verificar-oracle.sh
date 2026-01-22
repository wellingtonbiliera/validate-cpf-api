#!/bin/bash

echo "🔍 Verificando dados no Oracle..."
echo ""

docker exec -i valida-cpf-oracle sqlplus -s system/Oracle123@XE <<EOF
SET PAGESIZE 1000
SET LINESIZE 200

PROMPT ========================================
PROMPT Total de registros na tabela VALIDA_CPF
PROMPT ========================================
SELECT COUNT(*) as total_registros FROM VALIDA_CPF;

PROMPT 
PROMPT ========================================
PROMPT Últimos 10 registros salvos
PROMPT ========================================
SELECT 
    ID,
    CPF,
    CASE WHEN VALIDO = 1 THEN 'SIM' ELSE 'NÃO' END as VALIDO,
    DATA_VALIDACAO,
    SUBSTR(MENSAGEM, 1, 50) as MENSAGEM,
    NOME,
    SITUACAO
FROM VALIDA_CPF 
ORDER BY DATA_VALIDACAO DESC 
FETCH FIRST 10 ROWS ONLY;

PROMPT 
PROMPT ========================================
PROMPT Estatísticas por validação
PROMPT ========================================
SELECT 
    CASE WHEN VALIDO = 1 THEN 'Válidos' ELSE 'Inválidos' END as TIPO,
    COUNT(*) as QUANTIDADE
FROM VALIDA_CPF
GROUP BY VALIDO
ORDER BY VALIDO DESC;

EXIT;
EOF

echo ""
echo "✅ Verificação concluída!"
