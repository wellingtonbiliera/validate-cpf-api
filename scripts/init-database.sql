-- Script de inicialização do banco de dados Oracle
-- Este script cria o schema e tabela necessária para o projeto Valida CPF

-- Criar usuário para a aplicação (opcional - pode usar SYSTEM também)
-- CREATE USER validacpf IDENTIFIED BY "Validacpf123";
-- GRANT CONNECT, RESOURCE, UNLIMITED TABLESPACE TO validacpf;
-- ALTER USER validacpf QUOTA UNLIMITED ON USERS;

-- Usar schema SYSTEM ou criar schema específico
-- ALTER SESSION SET CURRENT_SCHEMA = validacpf;

-- Criar sequence para ID da tabela VALIDA_CPF
CREATE SEQUENCE VALIDA_CPF_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Criar tabela VALIDA_CPF
CREATE TABLE VALIDA_CPF (
    ID NUMBER(19) PRIMARY KEY,
    CPF VARCHAR2(14) NOT NULL,
    VALIDO NUMBER(1) NOT NULL,
    DATA_VALIDACAO TIMESTAMP NOT NULL,
    MENSAGEM VARCHAR2(500),
    CODIGO_RETORNO VARCHAR2(10),
    NOME VARCHAR2(255),
    DATA_NASCIMENTO DATE,
    SITUACAO VARCHAR2(50),
    TIMESTAMP_EVENTO TIMESTAMP,
    CONSTRAINT CHK_VALIDO CHECK (VALIDO IN (0, 1))
);

-- Criar índices para otimizar consultas
CREATE INDEX IDX_VALIDA_CPF_DATA_VALIDACAO ON VALIDA_CPF(DATA_VALIDACAO);
CREATE INDEX IDX_VALIDA_CPF_CPF ON VALIDA_CPF(CPF);

-- Comentários nas colunas
COMMENT ON TABLE VALIDA_CPF IS 'Tabela para armazenar validações de CPF';
COMMENT ON COLUMN VALIDA_CPF.ID IS 'Identificador único da validação';
COMMENT ON COLUMN VALIDA_CPF.CPF IS 'CPF validado (sem formatação ou formatado)';
COMMENT ON COLUMN VALIDA_CPF.VALIDO IS 'Indica se o CPF é válido (1) ou inválido (0)';
COMMENT ON COLUMN VALIDA_CPF.DATA_VALIDACAO IS 'Data e hora da validação';
COMMENT ON COLUMN VALIDA_CPF.MENSAGEM IS 'Mensagem descritiva do resultado';
COMMENT ON COLUMN VALIDA_CPF.CODIGO_RETORNO IS 'Código de retorno da ReceitaWS';
COMMENT ON COLUMN VALIDA_CPF.TIMESTAMP_EVENTO IS 'Timestamp quando evento foi publicado no Kafka';

-- Commit
COMMIT;
