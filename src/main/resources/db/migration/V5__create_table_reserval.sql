CREATE TABLE reserva(
  id serial primary key,
  data_da_reserva date not null,
  livro_id integer not null references livro(id),
  usuario_id integer not null references usuario(id)
);
