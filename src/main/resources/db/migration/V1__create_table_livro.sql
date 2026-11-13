CREATE TABLE livro(
  id serial PRIMARY KEY,
  titulo varchar(100) not null,
  autor varchar(100) not null,
  isbn varchar(13) not null
);