-- ============================================
-- Tabela: bio_transportadora
-- Descrição: Cadastro de transportadoras
-- ============================================

-- Sequência para transportadora
CREATE SEQUENCE seq_transportadora START WITH 1 INCREMENT BY 1;

-- Tabela principal de transportadora
CREATE TABLE bio_transportadora (
    id BIGINT PRIMARY KEY DEFAULT (NEXT VALUE FOR seq_transportadora),
    nome_fantasia VARCHAR(255) NOT NULL,
    razao_social VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,  -- CNPJ com máscara (XX.XXX.XXX/XXXX-XX)
    cidade VARCHAR(255),
    uf VARCHAR(2),        -- Estado (2 caracteres)
    endereco VARCHAR(500), -- Endereço completo (opcional)
    categoria VARCHAR(255), -- Ex: Logística Geral (opcional)
    telefone_comercial NVARCHAR(MAX),  -- Telefone comercial do JSON
    email_comercial NVARCHAR(MAX),    -- Email comercial do JSON
    -- Contatos específicos (opcional)
    contato_principal_nome VARCHAR(255),
    contato_principal_telefone VARCHAR(20),
    contato_principal_email VARCHAR(255),
    criado_em DATETIME DEFAULT GETDATE(),
    atualizado_em DATETIME DEFAULT GETDATE(),
    status NVARCHAR(MAX) DEFAULT 'Ativo' NOT NULL
);  

-- Índices
CREATE INDEX idx_bio_transportadora_nome_fantasia ON bio_transportadora (nome_fantasia);
CREATE INDEX idx_bio_transportadora_cnpj ON bio_transportadora (cnpj);
CREATE INDEX idx_bio_transportadora_cidade ON bio_transportadora (cidade);