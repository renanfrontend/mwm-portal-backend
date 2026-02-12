-- Inserts para Categoria de Transportadora
INSERT INTO bio_categoria (label, valor, criado_em, atualizado_em) VALUES ('Logística', 'Logística', GETDATE(), GETDATE());
INSERT INTO bio_categoria (label, valor, criado_em, atualizado_em) VALUES ('Agrícola', 'Agrícola', GETDATE(), GETDATE());
INSERT INTO bio_categoria (label, valor, criado_em, atualizado_em) VALUES ('Geral', 'Geral', GETDATE(), GETDATE());
INSERT INTO bio_categoria (label, valor, criado_em, atualizado_em) VALUES ('Especializada', 'Especializada', GETDATE(), GETDATE());

-- Inserts para Tipos de Veículo
INSERT INTO bio_veiculo_tipo (label, valor, criado_em, atualizado_em) VALUES ('Truck', 'truck', GETDATE(), GETDATE());
INSERT INTO bio_veiculo_tipo (label, valor, criado_em, atualizado_em) VALUES ('Carreta', 'carreta', GETDATE(), GETDATE());
INSERT INTO bio_veiculo_tipo (label, valor, criado_em, atualizado_em) VALUES ('Bitrem', 'bitrem', GETDATE(), GETDATE());
INSERT INTO bio_veiculo_tipo (label, valor, criado_em, atualizado_em) VALUES ('VUC', 'vuc', GETDATE(), GETDATE());
INSERT INTO bio_veiculo_tipo (label, valor, criado_em, atualizado_em) VALUES ('Utilitário', 'utilitario', GETDATE(), GETDATE());
INSERT INTO bio_veiculo_tipo (label, valor, criado_em, atualizado_em) VALUES ('Empilhadeira', 'empilhadeira', GETDATE(), GETDATE());

-- Inserts para Combustíveis
INSERT INTO bio_veiculo_combustivel (label, valor, criado_em, atualizado_em) VALUES ('Diesel', 'diesel', GETDATE(), GETDATE());
INSERT INTO bio_veiculo_combustivel (label, valor, criado_em, atualizado_em) VALUES ('Biomethane', 'biomethane', GETDATE(), GETDATE());
