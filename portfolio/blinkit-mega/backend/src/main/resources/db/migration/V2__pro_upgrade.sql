create table refresh_tokens (
  id bigserial primary key,
  user_id bigint not null,
  token_hash varchar(255) not null,
  expires_at timestamp not null,
  revoked boolean not null default false,
  foreign key (user_id) references users(id)
);

create table stores (
  id bigserial primary key,
  name varchar(120) not null,
  pincode varchar(10) not null,
  active boolean not null default true
);

create table store_inventory (
  id bigserial primary key,
  store_id bigint not null,
  product_id bigint not null,
  stock_on_hand int not null,
  reserved_qty int not null default 0,
  version bigint,
  updated_at timestamp not null default now(),
  unique (store_id, product_id),
  foreign key (store_id) references stores(id),
  foreign key (product_id) references products(id)
);

create table inventory_reservations (
  id bigserial primary key,
  user_id bigint not null,
  store_id bigint not null,
  status varchar(40) not null,
  expires_at timestamp not null,
  created_at timestamp not null default now(),
  foreign key (user_id) references users(id),
  foreign key (store_id) references stores(id)
);

create table reservation_items (
  id bigserial primary key,
  reservation_id bigint not null,
  product_id bigint not null,
  qty int not null,
  unit_price numeric(12,2) not null,
  foreign key (reservation_id) references inventory_reservations(id) on delete cascade,
  foreign key (product_id) references products(id)
);

create table idempotency_keys (
  id bigserial primary key,
  user_id bigint not null,
  idem_key varchar(120) not null,
  payload_hash varchar(120) not null,
  order_id bigint,
  created_at timestamp not null default now(),
  unique (user_id, idem_key),
  foreign key (user_id) references users(id),
  foreign key (order_id) references orders(id)
);
