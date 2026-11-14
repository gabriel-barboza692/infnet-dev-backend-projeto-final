CREATE TABLE multa(
  id serial primary key,
  usuario_id integer not null references usuario(id),
  emprestimo_id integer not null references emprestimo(id),
  valor_pago decimal(10,2) not null,
  pago boolean not null
);