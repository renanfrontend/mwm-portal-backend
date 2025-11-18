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

insert into analise_cooperado(id, name, valor, color) VALUES (1, 'Ademir E.', 2.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (2, 'Ademir M.', 2.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (3, 'Ademir R.', 3.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (4, 'André S.', -1.5, '#ef4444');
insert into analise_cooperado(id, name, valor, color) VALUES (5, 'Arsênio W.', 3.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (6, 'Carlos P.', 2.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (7, 'Clarindo M.', -0.5, '#ef4444');
insert into analise_cooperado(id, name, valor, color) VALUES (8, 'Delcio R.', 2.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (9, 'Divino', 1.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (10, 'Ederson D.', 3.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (11, 'Egon P.', 1.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (12, 'Fazenda E.', 0.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (13, 'Francisco', -1.5, '#ef4444');
insert into analise_cooperado(id, name, valor, color) VALUES (14, 'Gelson R.', 3.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (15, 'Gilberto', 1.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (16, 'Gilmar P.', -0.5, '#ef4444');
insert into analise_cooperado(id, name, valor, color) VALUES (17, 'Guido D.', 1.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (18, 'Jacir M.', 5.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (19, 'Jose F.', 1.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (20, 'Ladir N.', 3.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (21, 'Ladir R.', 3.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (22, 'Laurindo M.', 3.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (23, 'Marcelo', 3.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (24, 'Marcos C.', 3.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (25, 'Marcos S.', 7.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (26, 'Marina K.', 2.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (27, 'Marines C.', 3.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (28, 'Marlise K.', 2.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (29, 'Nelson B.', 4.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (30, 'Oswaldo G.', 4.5, '#334bff');
insert into analise_cooperado(id, name, valor, color) VALUES (31, 'Renato I.', 1.5, '#334bff');

insert into qualidade_dejetos_item(id, data_coleta, cooperado, placa, ph, densidade, entrega_referencia) VALUES (1, '13/10/2025', 'Ademir Engelsing', 'ABC-1D23', 7.2, '1025', 'ENT-54321');
insert into qualidade_dejetos_item(id, data_coleta, cooperado, placa, ph, densidade, entrega_referencia) VALUES (2, '13/10/2025', 'Ademir Marchioro', 'DEF-4567', 7.5, 'N/A', 'ENT-54322');