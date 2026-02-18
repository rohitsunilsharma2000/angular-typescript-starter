create table audit_logs (
  id bigserial primary key,
  actor_user_id bigint,
  action varchar(120) not null,
  entity_type varchar(80) not null,
  entity_id varchar(80) not null,
  payload varchar(2000),
  created_at timestamp not null default now()
);

create table coupons (
  id bigserial primary key,
  code varchar(80) not null unique,
  discount_type varchar(20) not null,
  discount_value numeric(12,2) not null,
  min_cart_value numeric(12,2) not null default 0,
  max_discount numeric(12,2) not null default 99999,
  category_id bigint,
  active boolean not null default true,
  foreign key (category_id) references categories(id)
);

create table coupon_redemptions (
  id bigserial primary key,
  coupon_id bigint not null,
  user_id bigint not null,
  order_id bigint,
  discount_amount numeric(12,2) not null,
  created_at timestamp not null default now(),
  foreign key (coupon_id) references coupons(id),
  foreign key (user_id) references users(id)
);

create table wallets (
  id bigserial primary key,
  user_id bigint not null unique,
  balance numeric(12,2) not null default 0,
  foreign key (user_id) references users(id)
);

create table wallet_txns (
  id bigserial primary key,
  wallet_id bigint not null,
  order_id bigint,
  type varchar(40) not null,
  amount numeric(12,2) not null,
  note varchar(250),
  created_at timestamp not null default now(),
  foreign key (wallet_id) references wallets(id),
  foreign key (order_id) references orders(id)
);

create table refunds (
  id bigserial primary key,
  order_id bigint not null,
  order_item_id bigint,
  amount numeric(12,2) not null,
  status varchar(40) not null,
  destination varchar(40) not null,
  created_at timestamp not null default now(),
  approved_at timestamp,
  foreign key (order_id) references orders(id),
  foreign key (order_item_id) references order_items(id)
);

create table referrals (
  id bigserial primary key,
  referrer_user_id bigint not null,
  referred_user_id bigint not null,
  reward_credited boolean not null default false,
  created_at timestamp not null default now()
);

create table delivery_fee_rules (
  id bigserial primary key,
  name varchar(120) not null,
  min_km numeric(8,2) not null,
  max_km numeric(8,2) not null,
  peak_hour boolean not null default false,
  high_load boolean not null default false,
  fee numeric(10,2) not null,
  active boolean not null default true
);

create table picking_tasks (
  id bigserial primary key,
  order_id bigint not null unique,
  assigned_to_user_id bigint,
  status varchar(40) not null,
  created_at timestamp not null default now()
);

create table picking_task_items (
  id bigserial primary key,
  picking_task_id bigint not null,
  order_item_id bigint not null,
  missing_qty int not null default 0,
  picked_qty int not null default 0,
  foreign key (picking_task_id) references picking_tasks(id) on delete cascade,
  foreign key (order_item_id) references order_items(id)
);

create table riders (
  id bigserial primary key,
  user_id bigint not null unique,
  capacity int not null default 5,
  active boolean not null default true,
  foreign key (user_id) references users(id)
);

create table delivery_batches (
  id bigserial primary key,
  rider_id bigint not null,
  status varchar(40) not null,
  created_at timestamp not null default now(),
  foreign key (rider_id) references riders(id)
);

create table delivery_assignments (
  id bigserial primary key,
  batch_id bigint not null,
  order_id bigint not null,
  accepted boolean not null default false,
  unique (batch_id, order_id),
  foreign key (batch_id) references delivery_batches(id),
  foreign key (order_id) references orders(id)
);

create table delivery_events (
  id bigserial primary key,
  order_id bigint not null,
  event_type varchar(80) not null,
  message varchar(250) not null,
  created_at timestamp not null default now(),
  foreign key (order_id) references orders(id)
);

create table notifications (
  id bigserial primary key,
  user_id bigint not null,
  title varchar(120) not null,
  body varchar(350) not null,
  read_flag boolean not null default false,
  created_at timestamp not null default now(),
  foreign key (user_id) references users(id)
);

create table outbox_events (
  id bigserial primary key,
  topic varchar(80) not null,
  aggregate_id varchar(80) not null,
  payload varchar(3000) not null,
  published boolean not null default false,
  created_at timestamp not null default now()
);
