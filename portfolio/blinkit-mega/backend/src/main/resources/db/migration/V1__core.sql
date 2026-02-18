create table users (
  id bigserial primary key,
  name varchar(120) not null,
  email varchar(160) not null unique,
  password_hash varchar(255) not null,
  role varchar(40) not null,
  pincode varchar(10),
  created_at timestamp not null default now()
);

create table categories (
  id bigserial primary key,
  name varchar(100) not null,
  active boolean not null default true
);

create table products (
  id bigserial primary key,
  category_id bigint not null,
  name varchar(150) not null,
  description varchar(500),
  price numeric(12,2) not null,
  active boolean not null default true,
  foreign key (category_id) references categories(id)
);

create table carts (
  id bigserial primary key,
  user_id bigint not null unique,
  store_id bigint,
  updated_at timestamp not null default now(),
  foreign key (user_id) references users(id)
);

create table cart_items (
  id bigserial primary key,
  cart_id bigint not null,
  product_id bigint not null,
  qty int not null,
  unique (cart_id, product_id),
  foreign key (cart_id) references carts(id) on delete cascade,
  foreign key (product_id) references products(id)
);

create table addresses (
  id bigserial primary key,
  user_id bigint not null,
  line1 varchar(180) not null,
  city varchar(100) not null,
  pincode varchar(10) not null,
  is_default boolean not null default false,
  foreign key (user_id) references users(id)
);

create table orders (
  id bigserial primary key,
  user_id bigint not null,
  store_id bigint,
  status varchar(40) not null,
  total_amount numeric(12,2) not null,
  delivery_fee numeric(12,2) not null default 0,
  discount_amount numeric(12,2) not null default 0,
  wallet_used numeric(12,2) not null default 0,
  created_at timestamp not null default now(),
  delivered_at timestamp,
  rider_id bigint,
  reservation_id bigint,
  foreign key (user_id) references users(id)
);

create table order_items (
  id bigserial primary key,
  order_id bigint not null,
  product_id bigint not null,
  product_name varchar(150) not null,
  qty int not null,
  price numeric(12,2) not null,
  refunded_qty int not null default 0,
  foreign key (order_id) references orders(id) on delete cascade,
  foreign key (product_id) references products(id)
);

create table payments (
  id bigserial primary key,
  order_id bigint not null unique,
  status varchar(40) not null,
  provider_ref varchar(120),
  created_at timestamp not null default now(),
  foreign key (order_id) references orders(id)
);
