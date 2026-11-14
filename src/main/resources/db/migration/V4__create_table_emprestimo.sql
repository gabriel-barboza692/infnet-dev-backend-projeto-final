CREATE TABLE emprestimo(
  id serial primary key,
  data_de_retirada date not null,
  prazo_de_devolucao date not null,
  data_efetiva_de_devolucao date ,
  exemplar_id integer references exemplar(id) not null,
  usuario_id integer references usuario(id) not null
);