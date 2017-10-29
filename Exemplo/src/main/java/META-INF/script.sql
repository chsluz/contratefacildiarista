set charset utf8;
INSERT INTO estado(nome,sigla) SELECT * FROM (SELECT 'Paraná','PR') AS tmp WHERE NOT EXISTS (SELECT nome FROM estado WHERE nome = 'Paraná') LIMIT 1;
INSERT INTO cidade(nome,id_estado) SELECT * FROM (SELECT 'Curitiba',(SELECT id FROM estado WHERE nome = 'Paraná')) AS tmp WHERE NOT EXISTS (SELECT nome FROM cidade WHERE nome = 'Curitiba') LIMIT 1;
INSERT INTO bairro(descricao,id_cidade) SELECT * FROM (SELECT 'Santa Cândida',(SELECT id FROM cidade WHERE nome = 'Curitiba')) AS tmp WHERE NOT EXISTS (SELECT descricao FROM bairro WHERE descricao = 'Santa Cândida') LIMIT 1;
INSERT INTO tipo_atividade(descricao) SELECT * FROM (SELECT 'Lavar Roupa') AS tmp WHERE NOT EXISTS (SELECT descricao FROM tipo_atividade WHERE descricao = 'Lavar Roupa') LIMIT 1;
INSERT INTO tipo_atividade(descricao) SELECT * FROM (SELECT 'Passar Roupa') AS tmp WHERE NOT EXISTS (SELECT descricao FROM tipo_atividade WHERE descricao = 'Passar Roupa') LIMIT 1;