-- ============================================
-- Tabela: bio_veiculo_combustivel
-- Descrição: Tipos de combustível disponíveis
-- ============================================

-- Criar sequence
CREATE SEQUENCE seq_bio_veiculo_combustivel START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE bio_veiculo_combustivel (
    id BIGINT PRIMARY KEY DEFAULT NEXT VALUE FOR seq_bio_veiculo_combustivel,
    label VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL,
    criado_em DATETIME DEFAULT GETDATE(),
    atualizado_em DATETIME DEFAULT GETDATE()
);

-- Criar índice
CREATE INDEX idx_bio_veiculo_combustivel_value ON bio_veiculo_combustivel (value);

-- Inserir dados iniciais
INSERT INTO bio_veiculo_combustivel (id, label, value) VALUES (1, 'Diesel', 'diesel');
INSERT INTO bio_veiculo_combustivel (id, label, value) VALUES (2, 'Biomethane', 'biomethane');
