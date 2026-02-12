insert into cooperado_item (id, matricula, filial, motorista, tipo_veiculo, placa, certificado, doam_dejetos, fase, cnpj, num_granja, qtd_cabecas, municipio, localizacao, latitude, longitude, tecnico, telefone, num_propriedade, num_estabelecimento) VALUES (1, 102646, 'Primato', 'Renato Ivan', 'Caminhão de dejetos', 'ABC-1D23', 'Inativo', 'Não', 'Fase Term. Firmesa', '069.037.349-02', null, 1450, 'Toledo', 'https://www.google.com/maps/dir/-24.7229319,-53.8641137', -24.7229319, -53.8641137, 'DANIEL', '4533761170', 41277001602, 41000592839);
insert into cooperado_item (id, matricula, filial, motorista, tipo_veiculo, placa, certificado, doam_dejetos, fase, cnpj, num_granja, qtd_cabecas, municipio, localizacao, latitude, longitude, tecnico, telefone, num_propriedade, num_estabelecimento) VALUES (2, 102284, 'Primato', 'Ademir Machioro', 'Caminhão de dejetos', 'ABC-1D23', 'Ativo', 'Sim', 'GRSC', '762.125.999-04', null, 800, 'Toledo', 'https://www.google.com/maps/dir/-24.7229319,-53.8641137', -24.606607, -53.82236858, 'RAFAELA', '45998181345', 41277001684, 41000575810);
insert into cooperado_item (id, matricula, filial, motorista, tipo_veiculo, placa, certificado, doam_dejetos, fase, cnpj, num_granja, qtd_cabecas, municipio, localizacao, latitude, longitude, tecnico, telefone, num_propriedade, num_estabelecimento) VALUES (3, 103034, 'Primato', 'Carlos Jaime Pauly', 'Caminhão de dejetos', 'ABC-1D23', 'Ativo', 'Sim', 'Fase Crechário', '627.854.199-87', null, 2800, 'Toledo', 'https://www.google.com/maps/dir/-24.7229319,-53.8641137', -24.83777355, -53.75586386, 'CLAUDEMIR', '45999271035', 412777002072, 41000579024);
insert into cooperado_item (id, matricula, filial, motorista, tipo_veiculo, placa, certificado, doam_dejetos, fase, cnpj, num_granja, qtd_cabecas, municipio, localizacao, latitude, longitude, tecnico, telefone, num_propriedade, num_estabelecimento) VALUES (4, 100173, 'Primato', 'Clarindo Mazzarollo', 'Caminhão de dejetos', 'ABC-1D23', 'Ativo', 'Sim', 'UPD', '559.724.709-34', null, 2180, 'Toledo', 'https://www.google.com/maps/dir/-24.7229319,-53.8641137', -24.67645104, -54.03771448, 'DANIEL', '4599730757', 41277002288, 41000636754);

insert into coleta_item (id, cooperado, motorista, tipo_veiculo, placa, odometro, data_previsao, hora_previsao, status) VALUES (1, 'Primato', 'Luiz Carlos', 'Caminhão de dejetos', 'ABC-1D23', 123456, '2025-01-01', '15:00', 'Pendente');
insert into coleta_item (id, cooperado, motorista, tipo_veiculo, placa, odometro, data_previsao, hora_previsao, status) VALUES (2, 'Primato', 'Marcos Paulo', 'Caminhão de ração', 'XYZ-4567', 234567, '2025-01-02', '10:00', 'Entregue');
insert into coleta_item (id, cooperado, motorista, tipo_veiculo, placa, odometro, data_previsao, hora_previsao, status) VALUES (3, 'Primato', 'Ana Cássia', 'Caminhão de dejetos', 'GHI-7890', 345678, '2025-01-03', '11:30', 'Atrasado');

insert into abastecimento_report_item (id, status, cliente, veiculo, placa, data, hora_inicio, hora_termino, volume, odometro, usuario, produto) VALUES (1, 'Concluído', 'Primato Cooperativa Agroindustrial', 'Caminhão (Ração)', 'BCK-0138', '2025-09-25', '17:09:21', '17:09:21', 134.56, 391396, 'vanessa', 'Biometano');
insert into abastecimento_report_item (id, status, cliente, veiculo, placa, data, hora_inicio, hora_termino, volume, odometro, usuario, produto) VALUES (2, 'Concluído', 'Primato Cooperativa Agroindustrial', 'Caminhão (Dejeto)', 'BBW-9C55', '2025-09-25', '17:08:56', '17:08:56', 157.66, 370306, 'vanessa', 'Biometano');
insert into abastecimento_report_item (id, status, cliente, veiculo, placa, data, hora_inicio, hora_termino, volume, odometro, usuario, produto) VALUES (3, 'Concluído', 'Primato Cooperativa Agroindustrial', 'Caminhão (Ração)', 'BCK-0138', '2025-09-25', '17:05:53', '17:05:53', 166.03, 381134, 'vanessa', 'Biometano');
insert into abastecimento_report_item (id, status, cliente, veiculo, placa, data, hora_inicio, hora_termino, volume, odometro, usuario, produto) VALUES (4, 'Concluído', 'Primato Cooperativa Agroindustrial', 'Caminhão (Dejeto)', 'BBW-9C55', '2025-09-25', '17:05:32', '17:05:33', 206.63, 384328, 'vanessa', 'Biometano');
insert into abastecimento_report_item (id, status, cliente, veiculo, placa, data, hora_inicio, hora_termino, volume, odometro, usuario, produto) VALUES (5, 'Concluído', 'Primato Cooperativa Agroindustrial', 'Caminhão (Ração)', 'BCK-0138', '2025-09-25', '17:04:57', '17:04:58', 136.78, 390768, 'vanessa', 'Biometano');
insert into abastecimento_report_item (id, status, cliente, veiculo, placa, data, hora_inicio, hora_termino, volume, odometro, usuario, produto) VALUES (6, 'Concluído', 'Primato Cooperativa Agroindustrial', 'Caminhão (Dejeto)', 'BBW-9C55', '2025-09-25', '17:04:10', '17:04:10', 94.72, 370055, 'vanessa', 'Biometano');
insert into abastecimento_report_item (id, status, cliente, veiculo, placa, data, hora_inicio, hora_termino, volume, odometro, usuario, produto) VALUES (7, 'Concluído', 'Primato Cooperativa Agroindustrial', 'Caminhão (Ração)', 'BCK-0138', '2025-09-25', '17:03:29', '17:03:30', 171.27, 534723, 'vanessa', 'Biometano');
insert into abastecimento_report_item (id, status, cliente, veiculo, placa, data, hora_inicio, hora_termino, volume, odometro, usuario, produto) VALUES (8, 'Concluído', 'Primato Cooperativa Agroindustrial', 'Caminhão (Ração)', 'BCK-0138', '2025-09-25', '17:03:01', '17:03:01', 107.41, 399801, 'vanessa', 'Biometano');

insert into portaria_item (id, categoria, data, horario, empresa, motorista, tipo_veiculo, placa, atividade, status) VALUES (1, 'Entregas', '01/01/2026', '10:00H', 'Primato', 'Ademir Engelsing', 'Caminhão de dejetos', 'ABC-1D23', 'Entrega de dejetos', 'Concluído');
insert into portaria_item (id, categoria, data, horario, empresa, motorista, tipo_veiculo, placa, atividade, status) VALUES (2, 'Abastecimentos', '02/01/2026', '09:30H', 'Transportadora XYZ', 'Carlos Silva', 'Caminhão Tanque', 'DEF-4567', 'Abastecimento de Diesel', 'Concluído');
insert into portaria_item (id, categoria, data, horario, empresa, motorista, tipo_veiculo, placa, atividade, status) VALUES (3, 'Entregas', '01/01/2026', '13:00H', 'Mosaic', 'Renato Ivan', 'Caminhão de entrega', 'ABC-1D23', 'Entrega de materiais', 'Pendente');

insert into agenda_data (id, cooperado, seg, ter, qua, qui, sex, qtd, km, transportadora, status) VALUES (1, 'Ademir Engelsing', 0, 2, 0, 0, 0, 2, 300, 'Primato', 'Realizado');
insert into agenda_data (id, cooperado, seg, ter, qua, qui, sex, qtd, km, transportadora, status) VALUES (2, 'Ademir Marchioro', 4, 2, 0, 4, 0, 10, 300, 'Primato', 'Realizado');
insert into agenda_data (id, cooperado, seg, ter, qua, qui, sex, qtd, km, transportadora, status) VALUES (3, 'Arseno Weschendeider', 10, 4, 4, 6, 0, 34, 300, 'Primato', 'Realizado');
insert into agenda_data (id, cooperado, seg, ter, qua, qui, sex, qtd, km, transportadora, status) VALUES (4, 'Carlos Jaime Pauly', 10, 9, 10, 10, 10, 49, 300, 'Primato', 'Realizado');
insert into agenda_data (id, cooperado, seg, ter, qua, qui, sex, qtd, km, transportadora, status) VALUES (5, 'Delcio Rossetto', 10, 10, 10, 10, 10, 50, 300, 'Agrocampo', 'Planejado');
insert into agenda_data (id, cooperado, seg, ter, qua, qui, sex, qtd, km, transportadora, status) VALUES (6, 'Delcio Rossetto', 10, 10, 10, 10, 10, 50, 300, 'Agrocampo', 'Planejado');
insert into agenda_data (id, cooperado, seg, ter, qua, qui, sex, qtd, km, transportadora, status) VALUES (7, 'Delcio Rossetto', 10, 10, 10, 10, 10, 50, 300, 'Agrocampo', 'Planejado');

insert into faturamento_item (id, name, faturamento, label) VALUES (1, 'Janeiro', 2774.38, '3.50');
insert into faturamento_item (id, name, faturamento, label) VALUES (2, 'Fevereiro', 2637.99, '3.72');
insert into faturamento_item (id, name, faturamento, label) VALUES (3, 'Março', 5027.0, '3.70');
insert into faturamento_item (id, name, faturamento, label) VALUES (4, 'Abril', 3847.0, '3.60');
insert into faturamento_item (id, name, faturamento, label) VALUES (5, 'Maio', 5122.71, '3.50');
insert into faturamento_item (id, name, faturamento, label) VALUES (6, 'Junho', 18231.53, '3.46');
insert into faturamento_item (id, name, faturamento, label) VALUES (7, 'Julho', 26145.7, '3.46');
insert into faturamento_item (id, name, faturamento, label) VALUES (8, 'Agosto', 30948.37, '3.47');
insert into faturamento_item (id, name, faturamento, label) VALUES (9, 'Setembro', 0, '0.0');
insert into faturamento_item (id, name, faturamento, label) VALUES (10, 'Outubro', 0, '0.0');
insert into faturamento_item (id, name, faturamento, label) VALUES (11, 'Novembro', 0, '0.0');
insert into faturamento_item (id, name, faturamento, label) VALUES (12, 'Dezembro', 0, '0.0');

insert into metric(id, icon, label, valor, trend, unit) VALUES (1, 'density_medium', 'Densidade dos dejetos', '1014', 'up', NULL);
insert into metric(id, icon, label, valor, trend, unit) VALUES (2, 'water_drop', 'Volume recebido', '34.6', 'up', 'm³');
insert into metric(id, icon, label, valor, trend, unit) VALUES (3, 'timer', 'TMO diário', '16:00:00', 'up', NULL);
insert into metric(id, icon, label, valor, trend, unit) VALUES (4, 'power_settings_new', 'Status operacional', 'Operando', 'up', NULL);

insert into stock_item(id, label, valor, capacity, unit, color) VALUES (1, 'Fertilizantes', 74480, 78400, 't', 'is-link');
insert into stock_item(id, label, valor, capacity, unit, color) VALUES (2, 'Bio Metano', 65000, 100000, 'm³', 'is-success');
insert into stock_item(id, label, valor, capacity, unit, color) VALUES (3, 'CO₂', 38000, 100000, 'm³', 'is-warning');

insert into analise_produtor(id, name, valor, color) VALUES (1, 'Ademir E.', 2.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (2, 'Ademir M.', 2.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (3, 'Ademir R.', 3.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (4, 'André S.', -1.5, '#ef4444');
insert into analise_produtor(id, name, valor, color) VALUES (5, 'Arsênio W.', 3.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (6, 'Carlos P.', 2.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (7, 'Clarindo M.', -0.5, '#ef4444');
insert into analise_produtor(id, name, valor, color) VALUES (8, 'Delcio R.', 2.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (9, 'Divino', 1.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (10, 'Ederson D.', 3.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (11, 'Egon P.', 1.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (12, 'Fazenda E.', 0.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (13, 'Francisco', -1.5, '#ef4444');
insert into analise_produtor(id, name, valor, color) VALUES (14, 'Gelson R.', 3.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (15, 'Gilberto', 1.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (16, 'Gilmar P.', -0.5, '#ef4444');
insert into analise_produtor(id, name, valor, color) VALUES (17, 'Guido D.', 1.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (18, 'Jacir M.', 5.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (19, 'Jose F.', 1.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (20, 'Ladir N.', 3.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (21, 'Ladir R.', 3.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (22, 'Laurindo M.', 3.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (23, 'Marcelo', 3.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (24, 'Marcos C.', 3.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (25, 'Marcos S.', 7.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (26, 'Marina K.', 2.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (27, 'Marines C.', 3.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (28, 'Marlise K.', 2.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (29, 'Nelson B.', 4.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (30, 'Oswaldo G.', 4.5, '#334bff');
insert into analise_produtor(id, name, valor, color) VALUES (31, 'Renato I.', 1.5, '#334bff');

insert into qualidade_dejetos_item(id, data_coleta, cooperado, placa, ph, densidade, entrega_referencia) VALUES (1, '13/10/2025', 'Ademir Engelsing', 'ABC-1D23', 7.2, '1025', 'ENT-54321');
insert into qualidade_dejetos_item(id, data_coleta, cooperado, placa, ph, densidade, entrega_referencia) VALUES (2, '13/10/2025', 'Ademir Marchioro', 'DEF-4567', 7.5, 'N/A', 'ENT-54322');


-- Bio Transportadora (Dados Reais)
INSERT INTO bio_transportadora (id, nome_fantasia, razao_social, cnpj, telefone_comercial, email_comercial, cidade, uf, endereco, categoria, contato_principal_nome, contato_principal_telefone, contato_principal_email, criado_em, atualizado_em, status) VALUES 
(1, 'Agrocampo', 'Agrocampo Transportes Ltda', '98.765.432/0001-00', '45999470471', 'carlos@agrocampo.com', 'Cascavel', 'BA', 'Av. Central 456', 'Agrícola', 'Carlos Souza', '45999470471', 'carlos@agrocampo.com', '2026-02-10 21:55:29.460', '2026-02-11 11:52:05.473', 'Ativo');
INSERT INTO bio_transportadora (id, nome_fantasia, razao_social, cnpj, telefone_comercial, email_comercial, cidade, uf, endereco, categoria, contato_principal_nome, contato_principal_telefone, contato_principal_email, criado_em, atualizado_em, status) VALUES 
(2, 'MWM', 'MWM Transportes Ltda', '11.222.333/0001-00', '45999470480', 'contato@mwm.com', 'Marechal Cândido Rondon', 'PR', 'Rua Industrial 789', 'Geral', 'José Oliveira', '45999470481', 'jose@mwm.com', '2026-02-10 21:55:29.473', '2026-02-10 21:55:29.473', 'Ativo');
INSERT INTO bio_transportadora (id, nome_fantasia, razao_social, cnpj, telefone_comercial, email_comercial, cidade, uf, endereco, categoria, contato_principal_nome, contato_principal_telefone, contato_principal_email, criado_em, atualizado_em, status) VALUES 
(3, 'Tupy', 'Tupy Transportes Ltda', '44.555.666/0001-00', '45999470490', 'contato@tupy.com', 'Palotina', 'PR', 'Estrada Rural 101', 'Especializada', 'Antônio Ferreira', '45999470491', 'antonio@tupy.com', '2026-02-10 21:55:29.480', '2026-02-10 21:55:29.480', 'Ativo');
INSERT INTO bio_transportadora (id, nome_fantasia, razao_social, cnpj, telefone_comercial, email_comercial, cidade, uf, endereco, categoria, contato_principal_nome, contato_principal_telefone, contato_principal_email, criado_em, atualizado_em, status) VALUES 
(11, 'Transportadora XYZ Ltda', 'XYZ Logística e Transportes SA', '12.345.678/0001-99', '(45) 3333-4444', 'contato@xyz.com.br', 'Toledo', 'PR', 'Rua das Flores, 123, Centro', 'Logística Geral', 'João Silva', '45999470460', 'joao@transportadora.com', '2026-02-10 20:03:12.287', '2026-02-10 20:03:12.287', 'Ativo');
INSERT INTO bio_transportadora (id, nome_fantasia, razao_social, cnpj, telefone_comercial, email_comercial, cidade, uf, endereco, categoria, contato_principal_nome, contato_principal_telefone, contato_principal_email, criado_em, atualizado_em, status) VALUES 
(12, 'Transportadora XYZ Ltda', 'XYZ Logística e Transportes SA', '00.345.678/0001-00', '(45) 3333-4444', 'contato@xyz.com.br', 'Toledo', 'PR', 'Rua das Flores, 123, Centro', 'Logística Geral', 'João Silva', '45999470460', 'joao@transportadora.com', '2026-02-10 20:03:47.217', '2026-02-10 20:03:47.217', 'Ativo');
INSERT INTO bio_transportadora (id, nome_fantasia, razao_social, cnpj, telefone_comercial, email_comercial, cidade, uf, endereco, categoria, contato_principal_nome, contato_principal_telefone, contato_principal_email, criado_em, atualizado_em, status) VALUES 
(13, 'cooperativa fantasia', 'cooperativa fantasia social', '46816297000169', '11000002002', 'antonio@cooperativa.com.br', 'sao paulo', 'SP', 'endereco completo', 'Agrícola', 'antonio', '11000002002', 'antonio@cooperativa.com.br', '2026-02-10 21:12:21.470', '2026-02-11 09:13:06.687', 'Ativo');

-- Test data for bio_planta (assuming one default)
INSERT INTO bio_planta (id, nome, codigo_interno, criado_em, atualizado_em)
VALUES (1, 'Planta Toledo', '001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Bio Veiculo Transportadora (Dados Reais)
INSERT INTO bio_veiculo_transportadora (id, transportadora_id, tipo, capacidade, placa, tipo_abastecimento, status, criado_em, atualizado_em) VALUES 
(2, 11, 'Caminhão Truck', '16000', 'ABC-1999', 'Diesel', 'Ativo', '2026-02-10 20:03:12.443', '2026-02-10 20:22:20.797');
INSERT INTO bio_veiculo_transportadora (id, transportadora_id, tipo, capacidade, placa, tipo_abastecimento, status, criado_em, atualizado_em) VALUES 
(6, 11, 'Caminhão Truck', '16000', 'ABC-1234', 'Diesel', 'Ativo', '2026-02-10 20:12:13.217', '2026-02-10 20:12:13.217');
INSERT INTO bio_veiculo_transportadora (id, transportadora_id, tipo, capacidade, placa, tipo_abastecimento, status, criado_em, atualizado_em) VALUES 
(11, 13, 'carreta', '13', 'PP0-X012D', 'diesel', 'Ativo', '2026-02-11 09:13:07.573', '2026-02-11 09:13:07.573');
INSERT INTO bio_veiculo_transportadora (id, transportadora_id, tipo, capacidade, placa, tipo_abastecimento, status, criado_em, atualizado_em) VALUES 
(12, 13, 'utilitario', '13', 'IUI-0D99', 'diesel', 'Ativo', '2026-02-11 09:13:07.723', '2026-02-11 09:13:07.723');
INSERT INTO bio_veiculo_transportadora (id, transportadora_id, tipo, capacidade, placa, tipo_abastecimento, status, criado_em, atualizado_em) VALUES 
(13, 13, 'truck', '25', 'JKS-8WOP', 'diesel', 'Ativo', '2026-02-11 09:13:07.857', '2026-02-11 09:13:07.857');
INSERT INTO bio_veiculo_transportadora (id, transportadora_id, tipo, capacidade, placa, tipo_abastecimento, status, criado_em, atualizado_em) VALUES 
(14, 13, 'carreta', '15', 'QQW-01Q11', 'diesel', 'Ativo', '2026-02-11 09:13:07.990', '2026-02-11 09:13:07.990');
INSERT INTO bio_veiculo_transportadora (id, transportadora_id, tipo, capacidade, placa, tipo_abastecimento, status, criado_em, atualizado_em) VALUES 
(18, 1, 'truck', '2', 'AAA-X002', 'diesel', 'Ativo', '2026-02-11 11:52:06.433', '2026-02-11 11:52:06.433');

-- Test data for bio_filiada (linking to transportadoras - mantido para compatibilidade com produtor)
INSERT INTO bio_filiada (id, bio_planta_id, nome, criado_em, atualizado_em) VALUES (1, 1, 'Primato', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bio_filiada (id, bio_planta_id, nome, criado_em, atualizado_em) VALUES (2, 1, 'Agrocampo', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bio_filiada (id, bio_planta_id, nome, criado_em, atualizado_em) VALUES (3, 1, 'MWM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bio_filiada (id, bio_planta_id, nome, criado_em, atualizado_em) VALUES (4, 1, 'Tupy', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- Dados para bio_categoria
INSERT INTO bio_categoria (id, label, valor, criado_em, atualizado_em) VALUES (1, 'Logística', 'Logística', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bio_categoria (id, label, valor, criado_em, atualizado_em) VALUES (2, 'Agrícola', 'Agrícola', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bio_categoria (id, label, valor, criado_em, atualizado_em) VALUES (3, 'Geral', 'Geral', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bio_categoria (id, label, valor, criado_em, atualizado_em) VALUES (4, 'Especializada', 'Especializada', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Dados para bio_veiculo_tipo
INSERT INTO bio_veiculo_tipo (id, label, valor, criado_em, atualizado_em) VALUES (1, 'Truck', 'truck', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bio_veiculo_tipo (id, label, valor, criado_em, atualizado_em) VALUES (2, 'Carreta', 'carreta', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bio_veiculo_tipo (id, label, valor, criado_em, atualizado_em) VALUES (3, 'Bitrem', 'bitrem', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bio_veiculo_tipo (id, label, valor, criado_em, atualizado_em) VALUES (4, 'VUC', 'vuc', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bio_veiculo_tipo (id, label, valor, criado_em, atualizado_em) VALUES (5, 'Utilitário', 'utilitario', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bio_veiculo_tipo (id, label, valor, criado_em, atualizado_em) VALUES (6, 'Empilhadeira', 'empilhadeira', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Dados para bio_veiculo_combustivel
INSERT INTO bio_veiculo_combustivel (id, label, valor, criado_em, atualizado_em) VALUES (1, 'Diesel', 'diesel', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO bio_veiculo_combustivel (id, label, valor, criado_em, atualizado_em) VALUES (2, 'Biomethane', 'biomethane', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);



-- Dados para bio_produtor
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(1, 1, 'COOP001', 'EDEMAR OTAVIO HORN', '616.030.669-34', '45999726031', NULL, NULL, 'PF', 'A', '2026-01-29', '2026-01-29 14:12:04.803', '2026-01-29 14:12:04.803');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(2, 1, 'COOP002', 'PRIMATO COOPERATIVA AGROINDUSTRIAL', '02.168.202/0008-49', NULL, NULL, NULL, 'PJ', 'A', '2026-01-29', '2026-01-29 14:12:06.027', '2026-01-29 14:12:06.027');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(3, 1, 'COOP003', 'PRIMATO COOPERATIVA AGROINDUSTRIAL', '02.168.202/0026-20', NULL, NULL, NULL, 'PJ', 'A', '2026-01-29', '2026-01-29 14:12:06.420', '2026-01-29 14:12:06.420');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(4, 1, 'PROD-1769706418090', 'João Silva', '123.456.789-00', '45999470460', NULL, NULL, 'PF', 'A', '2026-01-29', '2026-01-29 14:06:58.090', '2026-01-29 14:06:58.090');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(5, 1, 'PROD-1770214239306', 'josue Silva', '123.456.789-21', '4599947046021', NULL, NULL, 'PF', 'A', '2026-02-04', '2026-02-04 11:10:39.307', '2026-02-04 11:10:39.307');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(6, 1, 'PROD-1770217838686', 'almir Silva', '123.456.789-22', '45999470460', NULL, NULL, 'PF', 'A', '2026-02-04', '2026-02-04 12:10:38.687', '2026-02-04 12:10:38.687');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(7, 1, 'PROD-1770218456284', 'almir Silva', '123.456.789-23', '45999470460', NULL, NULL, 'PF', 'A', '2026-02-04', '2026-02-04 12:20:56.283', '2026-02-04 12:20:56.283');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(11, 1, 'PROD-1770220542940', 'wander produtor', '5454545454', '45999999999', NULL, NULL, 'PF', 'A', '2026-02-04', '2026-02-04 12:55:42.940', '2026-02-04 12:55:42.940');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(12, 1, 'PROD-1770297148953', 'João Silva25', '123.456.789-25', '4599947046025', NULL, NULL, 'PF', 'A', '2026-02-05', '2026-02-05 10:12:28.953', '2026-02-05 10:12:28.953');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(14, 1, 'PROD-1770299017826', 'monica', '94045022000111', '45999999999', NULL, NULL, 'PF', 'A', '2026-02-05', '2026-02-05 10:43:37.827', '2026-02-05 10:43:37.827');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(15, 1, 'PROD-1770299525592', 'RENATO IVAN KUNZLER', NULL, '45999999999', NULL, NULL, 'PF', 'A', '2026-02-05', '2026-02-05 10:52:05.593', '2026-02-05 10:52:05.593');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(16, 1, 'PROD-1770300017784', 'ADEMIR JOSE ENGELSING', '762.125.999-04', '45999999999', NULL, NULL, 'PF', 'A', '2026-02-05', '2026-02-05 11:00:17.783', '2026-02-05 11:00:17.783');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(18, 1, 'PROD-1770769361863', 'carlos produtor', '37963951000176', '45999999999', NULL, NULL, 'PF', 'A', '2026-02-10', '2026-02-10 21:22:41.863', '2026-02-10 21:22:41.863');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(19, 1, 'PROD-1770769440304', 'agro forte', '16137127000197', '45999999999', NULL, NULL, 'PF', 'A', '2026-02-10', '2026-02-10 21:24:00.303', '2026-02-10 21:24:00.303');
INSERT INTO bio_produtor (id, bio_filiada_id, codigo_produtor, nome, cpf_cnpj, telefone_principal, telefone_secundario, email, tipo_pessoa, status, data_cadastro, criado_em, atualizado_em) VALUES 
(20, 1, 'PROD-1770815163286', 'vinicios agro', '84327265241', '45999999999', NULL, NULL, 'PF', 'A', '2026-02-11', '2026-02-11 10:06:03.287', '2026-02-11 10:06:03.287');

-- Dados para bio_estabelecimento
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(1, 1, 'EST001', '41000582569', '275001', '41277002439', NULL, NULL, 'MARECHAL CÂNDIDO RONDON', 'PR', NULL, 12.00, -54.04, 'Local', 'A', '2026-01-29 14:12:05.103', '2026-01-29 14:12:05.103', NULL, '45', NULL);
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(3, 2, 'EST003', '41000610559', '8', '41277000989', NULL, NULL, 'OURO VERDE DO OESTE', 'PR', NULL, -24.80, -53.86, 'Local', 'A', '2026-01-29 14:12:06.147', '2026-01-29 14:12:06.147', NULL, '60', NULL);
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(4, 3, 'EST004', '41000631452', '26', '41277002016', NULL, NULL, 'OURO VERDE DO OESTE', 'PR', NULL, -24.78, -53.84, 'Local', 'A', '2026-01-29 14:12:06.560', '2026-01-29 14:12:06.560', NULL, '22', NULL);
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(5, 4, 'EST-1769706418526', 'EST001A', '123456', 'PROP001A', 'João Silva - PROP001A', 'Toledo - PR', 'Toledo - PR', 'PR', NULL, -24.73, -53.74, NULL, 'A', '2026-01-29 14:06:58.527', '2026-01-29 14:06:58.527', NULL, '40', NULL);
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(6, 5, 'EST-1770214239738', 'EST0012', '12345621', 'PROP00121', 'josue Silva - PROP00121', 'Toledo - PR', 'Toledo - PR', 'PR', NULL, -24.73, -53.74, NULL, 'A', '2026-02-04 11:10:39.740', '2026-02-04 11:10:39.740', NULL, NULL, NULL);
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(7, 6, 'EST-1770217839107', 'EST00122', '12345622', 'PROP00122', 'almir Silva - PROP00122', 'Linha São Paulo', 'Toledo - PR', 'PR', NULL, -24.73, -53.74, NULL, 'A', '2026-02-04 12:10:39.107', '2026-02-04 12:10:39.107', 'Maria Souza', '15', 'Caminhão não entra com chuva');
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(8, 7, 'EST-1770218456701', 'EST00123', '12345623', 'PROP00123', 'almir Silva - PROP00123', 'Linha São Paulo', 'Toledo - PR', 'PR', NULL, -24.73, -53.74, NULL, 'A', '2026-02-04 12:20:56.700', '2026-02-04 12:20:56.700', 'Maria Souza', '15', 'Caminhão não entra com chuva');
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(12, 11, 'EST-1770220543386', '102646', '11212121', '2222', 'wander produtor - 2222', NULL, 'Toledo-PR', 'PR', NULL, 12.35, 65.43, NULL, 'A', '2026-02-04 12:55:43.387', '2026-02-04 12:55:43.387', 'meu nome é contato', NULL, 'Nenhuma');
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(13, 12, 'EST-1770297149407', 'EST0012425', '12345625', 'PROP001', 'João Silva25 - PROP001', 'Linha São Paulo', 'Toledo - PR', 'PR', NULL, -24.73, -53.74, NULL, 'A', '2026-02-05 10:12:29.407', '2026-02-05 10:12:29.407', 'Maria Souza', '15', 'Caminhão não entra com chuva');
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(15, 14, 'EST-1770299018226', '3.192', '333', '33', 'monica - 33', NULL, 'Toledo-PR', 'PR', NULL, -24.72, 53.74, NULL, 'A', '2026-02-05 10:43:38.227', '2026-02-05 10:43:38.227', '333', '10', 'Nenhuma');
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(16, 15, 'EST-1770299526025', '41000592839', '102646', '41277001602', 'RENATO IVAN KUNZLER - 41277001602', NULL, 'Toledo-PR', 'PR', NULL, -24.72, -53.86, NULL, 'A', '2026-02-05 10:52:06.027', '2026-02-05 10:52:06.027', 'nao informado', '18', 'Nenhuma');
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(17, 16, 'EST-1770300018188', '41000575810', '511224', '41277001684', 'ADEMIR JOSE ENGELSING - 41277001684', NULL, 'Toledo-PR', 'PR', NULL, -24.61, -53.82, NULL, 'A', '2026-02-05 11:00:18.190', '2026-02-05 11:00:18.190', 'NAO INFORMADO', '60', 'Nenhuma');
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(22, 18, 'EST-1770769362297', 'carlos produtor ltda', '5453344', '122222', 'carlos produtor - 122222', NULL, 'Toledo-PR', 'PR', NULL, 0.00, 0.00, NULL, 'A', '2026-02-10 21:22:42.297', '2026-02-10 21:22:42.297', NULL, NULL, 'Nenhuma');
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(23, 19, 'EST-1770769440696', 'agro forte ltda', '223232', '1', 'agro forte - 1', NULL, 'Toledo-PR', 'PR', NULL, 12.00, 14.00, NULL, 'A', '2026-02-10 21:24:00.697', '2026-02-10 21:24:00.697', NULL, NULL, 'Nenhuma');
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(24, 15, 'EST-1770813378242', '', '0', '', ' - ', NULL, 'Toledo-PR', 'PR', NULL, 12.00, 12.00, NULL, 'A', '2026-02-11 09:36:18.243', '2026-02-11 09:36:18.243', 'Almir', NULL, 'Nenhuma');
INSERT INTO bio_estabelecimento (id, bio_produtor_id, codigo_estabelecimento, matricula, numero_estabelecimento, numero_propriedade, responsavel, endereco, municipio, estado, cep, latitude, longitude, localizacao_link, status, criado_em, atualizado_em, restricoes, distancia, nome_propriedade) VALUES 
(25, 20, 'EST-1770815163858', 'vinicios agro ltda', '300303003', '292829922', 'vinicios agro - 292829922', NULL, 'Toledo-PR', 'PR', NULL, 10.00, 20.00, NULL, 'A', '2026-02-11 10:06:03.860', '2026-02-11 10:06:03.860', 'jonas', NULL, 'Nenhuma');
