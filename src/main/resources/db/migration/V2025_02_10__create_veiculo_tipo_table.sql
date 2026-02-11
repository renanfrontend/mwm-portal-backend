-- ============================================
-- Tabela: bio_veiculo_tipo
-- Descrição: Tipos de veículos disponíveis
-- ============================================

-- Criar sequence
CREATE SEQUENCE seq_bio_veiculo_tipo START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE bio_veiculo_tipo (
    id BIGINT PRIMARY KEY DEFAULT NEXT VALUE FOR seq_bio_veiculo_tipo,
    label VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL,
    criado_em DATETIME DEFAULT GETDATE(),
    atualizado_em DATETIME DEFAULT GETDATE()
);

-- Criar índice
CREATE INDEX idx_bio_veiculo_tipo_value ON bio_veiculo_tipo (value);

-- Inserir dados iniciais
INSERT INTO bio_veiculo_tipo (id, label, value) VALUES (1, 'Truck', 'truck');
INSERT INTO bio_veiculo_tipo (id, label, value) VALUES (2, 'Carreta', 'carreta');
INSERT INTO bio_veiculo_tipo (id, label, value) VALUES (3, 'Bitrem', 'bitrem');
INSERT INTO bio_veiculo_tipo (id, label, value) VALUES (4, 'VUC', 'vuc');
INSERT INTO bio_veiculo_tipo (id, label, value) VALUES (5, 'Utilitário', 'utilitario');
INSERT INTO bio_veiculo_tipo (id, label, value) VALUES (6, 'Empilhadeira', 'empilhadeira');
