create table if not exists users (
  id bigserial primary key,
  name varchar(120) not null,
  email varchar(180) not null unique
);

create table if not exists restaurants (
  id bigserial primary key,
  name varchar(180) not null,
  city varchar(80) not null,
  cuisine_type varchar(80) not null,
  rating_avg double precision not null,
  delivery_time_min int not null,
  image_url text
);

create table if not exists menu_items (
  id bigserial primary key,
  restaurant_id bigint not null references restaurants(id),
  name varchar(180) not null,
  price bigint not null,
  is_veg boolean not null,
  available boolean not null,
  stock_qty int not null
);

create table if not exists carts (
  id bigserial primary key,
  user_id bigint not null unique references users(id),
  updated_at timestamptz not null
);

create table if not exists cart_items (
  id bigserial primary key,
  cart_id bigint not null references carts(id) on delete cascade,
  menu_item_id bigint not null references menu_items(id),
  qty int not null,
  constraint uk_cart_menu unique(cart_id, menu_item_id)
);

create table if not exists addresses (
  id bigserial primary key,
  user_id bigint not null references users(id),
  line1 varchar(220) not null,
  city varchar(80) not null,
  pincode varchar(12) not null,
  phone varchar(20) not null
);

create table if not exists orders (
  id bigserial primary key,
  user_id bigint not null references users(id),
  restaurant_id bigint not null references restaurants(id),
  status varchar(40) not null,
  item_total bigint not null,
  delivery_fee bigint not null,
  payable_total bigint not null,
  created_at timestamptz not null
);

create table if not exists order_items (
  id bigserial primary key,
  order_id bigint not null references orders(id) on delete cascade,
  menu_item_name_snapshot varchar(180) not null,
  price_snapshot bigint not null,
  qty int not null,
  line_total bigint not null
);

create table if not exists payments (
  id bigserial primary key,
  order_id bigint not null unique references orders(id) on delete cascade,
  status varchar(40) not null,
  method varchar(40) not null,
  created_at timestamptz not null
);

create index if not exists idx_restaurants_city on restaurants(city);
create index if not exists idx_menu_items_restaurant on menu_items(restaurant_id);
create index if not exists idx_orders_user on orders(user_id);
