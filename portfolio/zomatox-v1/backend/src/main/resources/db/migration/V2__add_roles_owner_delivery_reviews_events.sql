alter table users
  add column if not exists role varchar(40) not null default 'CUSTOMER';

alter table restaurants
  add column if not exists owner_user_id bigint null;

alter table restaurants
  add constraint fk_restaurant_owner
  foreign key (owner_user_id) references users(id);

alter table orders
  add column if not exists delivery_partner_user_id bigint null;

alter table orders
  add constraint fk_orders_delivery_partner
  foreign key (delivery_partner_user_id) references users(id);

alter table orders
  add column if not exists updated_at timestamptz not null default now();

create table if not exists order_events (
  id bigserial primary key,
  order_id bigint not null references orders(id) on delete cascade,
  status varchar(40) not null,
  message text,
  created_at timestamptz not null default now()
);

create index if not exists idx_order_events_order on order_events(order_id);

create table if not exists reviews (
  id bigserial primary key,
  order_id bigint not null unique references orders(id) on delete cascade,
  restaurant_id bigint not null references restaurants(id),
  user_id bigint not null references users(id),
  rating int not null,
  comment text,
  created_at timestamptz not null default now()
);

create index if not exists idx_reviews_restaurant on reviews(restaurant_id);

alter table reviews
  add constraint chk_reviews_rating
  check (rating >= 1 and rating <= 5);
