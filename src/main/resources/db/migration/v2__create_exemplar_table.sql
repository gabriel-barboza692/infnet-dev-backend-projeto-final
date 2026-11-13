CREATE TABLE exemplar(
  id serial PRIMARY KEY,
  codigo_de_patrimonio varchar(100) not null,
  estado_de_conservacao varchar(100) not null,
  livro_id serial not null,

  CONSTRAINT fk_exemplar_livro FOREIGN KEY(livro_id) REFERENCES livro(id),
  CONSTRAINT check_estado_conservacao
    CHECK (
      estado_de_conservacao in ('EXCELENTE','REGULAR','RUIM')
    )
);