-- #LIVRO
INSERT INTO tb_livro (id, autor, isbn, nome) VALUES (1, 'Machado de Assis', '9788535914849', 'Capitu');
INSERT INTO tb_livro (id, autor, isbn, nome) VALUES (2, 'George Orwell', '9788535914848', 'A fazenda dos bixos');
INSERT INTO tb_livro (id, autor, isbn, nome) VALUES (3, 'Dostoiévski', '9788595084759', 'Crime e Castigo');
INSERT INTO tb_livro (id, autor, isbn, nome) VALUES (4, 'Tolstoy', '9788532529626','Guerra e Paz');
INSERT INTO tb_livro (id, autor, isbn, nome) VALUES (5, 'Jorge Amado', '9788535914847', 'Capitães de Areia');

-- Estados de conservação: EXCELENTE | REGULAR | RUIM
-- # EXEMPLARES
INSERT INTO tb_exemplar (id, codigo_de_patrimonio, estado_de_conservacao, livro_id) VALUES (1, 'PAT-001', 'REGULAR', 1);
INSERT INTO tb_exemplar (id, codigo_de_patrimonio, estado_de_conservacao, livro_id) VALUES (2, 'PAT-002', 'EXCELENTE', 1);
INSERT INTO tb_exemplar (id, codigo_de_patrimonio, estado_de_conservacao, livro_id) VALUES (3, 'PAT-003', 'RUIM', 2);
INSERT INTO tb_exemplar (id, codigo_de_patrimonio, estado_de_conservacao, livro_id) VALUES (4, 'PAT-004', 'EXCELENTE', 2);
INSERT INTO tb_exemplar (id, codigo_de_patrimonio, estado_de_conservacao, livro_id) VALUES (5, 'PAT-005', 'REGULAR', 3);
INSERT INTO tb_exemplar (id, codigo_de_patrimonio, estado_de_conservacao, livro_id) VALUES (6, 'PAT-006', 'EXCELENTE', 3);
INSERT INTO tb_exemplar (id, codigo_de_patrimonio, estado_de_conservacao, livro_id) VALUES (7, 'PAT-007', 'RUIM', 4);
INSERT INTO tb_exemplar (id, codigo_de_patrimonio, estado_de_conservacao, livro_id) VALUES (8, 'PAT-008', 'REGULAR', 4);
INSERT INTO tb_exemplar (id, codigo_de_patrimonio, estado_de_conservacao, livro_id) VALUES (9, 'PAT-009', 'EXCELENTE', 5);
INSERT INTO tb_exemplar (id, codigo_de_patrimonio, estado_de_conservacao, livro_id) VALUES (10, 'PAT-010', 'REGULAR', 5);

-- Tipos de membros: ALUNO | PROFESSOR | FUNCIONARIO

INSERT INTO tb_membro (id, nome, matricula, tipo_aluno) VALUES (1, 'Gabriel Barbosa', 'MAT-001', 'ALUNO');
INSERT INTO tb_membro (id, nome, matricula, tipo_aluno) VALUES (2, 'Ana Oliveira', 'MAT-002', 'ALUNO');
INSERT INTO tb_membro (id, nome, matricula, tipo_aluno) VALUES (3, 'Carlos Eduardo', 'MAT-003', 'PROFESSOR');
INSERT INTO tb_membro (id, nome, matricula, tipo_aluno) VALUES (4, 'Mariana Santos', 'MAT-004', 'PROFESSOR');
INSERT INTO tb_membro (id, nome, matricula, tipo_aluno) VALUES (5, 'Pedro Almeida', 'MAT-005', 'FUNCIONARIO');



