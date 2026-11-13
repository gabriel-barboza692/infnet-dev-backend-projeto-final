CREATE TABLE usuario(
  id serial primary key,
  nome varchar(100) not null,
  tipo_usuario varchar(100) not null,
  matricula varchar(100) not null,
  email varchar(100) not null unique,
  password varchar(200) not null,

  CONSTRAINT check_tipo_usario check(
    tipo_usuario in ('ALUNO','PROFESSOR','FUNCIONARIO')
  )
);