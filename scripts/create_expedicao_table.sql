-- ============================================================
-- Portaria Expedição
-- Executar manualmente no banco antes do deploy
-- ============================================================

-- 1. Remover tabela existente (caso exista)
IF OBJECT_ID('dbo.bio_portaria_expedicao', 'U') IS NOT NULL
    DROP TABLE dbo.bio_portaria_expedicao;

-- 2. Remover sequence existente (caso exista)
IF OBJECT_ID('dbo.seq_portaria_expedicao', 'SO') IS NOT NULL
    DROP SEQUENCE dbo.seq_portaria_expedicao;

-- 3. Criar sequence
CREATE SEQUENCE dbo.seq_portaria_expedicao
    START WITH 1
    INCREMENT BY 1;

-- 4. Criar tabela
CREATE TABLE dbo.bio_portaria_expedicao (
    id                    BIGINT          NOT NULL DEFAULT (NEXT VALUE FOR dbo.seq_portaria_expedicao),
    registro_id           BIGINT          NOT NULL,
    transportadora_id     BIGINT          NULL,
    veiculo_id            BIGINT          NULL,
    usuario_id            BIGINT          NULL,
    data_entrada          DATE            NOT NULL,
    horario_entrada       TIME            NOT NULL,
    atividade             VARCHAR(100)    NOT NULL,
    transportadora_manual VARCHAR(200)    NULL,
    tipo_veiculo          VARCHAR(100)    NOT NULL,
    placa                 VARCHAR(20)     NULL,
    motorista             VARCHAR(200)    NOT NULL,
    cpf_passaporte        VARCHAR(20)     NOT NULL,
    nota_fiscal           VARCHAR(100)    NULL,
    peso_inicial          DECIMAL(18,3)   NULL,
    peso_final            DECIMAL(18,3)   NULL,
    data_saida            DATE            NULL,
    horario_saida         TIME            NULL,
    observacao            VARCHAR(500)    NULL,
    criado_em             DATETIME2       NOT NULL,
    atualizado_em         DATETIME2       NOT NULL,
    CONSTRAINT PK_bio_portaria_expedicao PRIMARY KEY (id)
);

-- 5. Adicionar coluna expedicao_id na tabela de registro (se ainda não existir)
IF NOT EXISTS (
    SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'bio_portaria_registro' AND COLUMN_NAME = 'expedicao_id'
)
BEGIN
    ALTER TABLE dbo.bio_portaria_registro ADD expedicao_id BIGINT NULL;
END;
