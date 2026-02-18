alter table users
  add column if not exists password_hash varchar(255),
  add column if not exists is_active boolean not null default true;

create table if not exists refresh_tokens (
  id bigserial primary key,
  user_id bigint not null references users(id) on delete cascade,
  token_hash varchar(255) not null,
  expires_at timestamptz not null,
  revoked_at timestamptz,
  created_at timestamptz not null default now()
);
create index if not exists idx_refresh_tokens_user on refresh_tokens(user_id);

alter table restaurants
  add column if not exists approval_status varchar(40) not null default 'PENDING_APPROVAL',
  add column if not exists is_blocked boolean not null default false,
  add column if not exists blocked_reason text;

alter table menu_items
  add column if not exists is_blocked boolean not null default false;

create table if not exists coupons (
  id bigserial primary key,
  code varchar(80) not null unique,
  type varchar(20) not null,
  value bigint not null,
  min_order bigint not null,
  max_cap bigint,
  valid_from timestamptz not null,
  valid_to timestamptz not null,
  active boolean not null default true,
  restaurant_id bigint null references restaurants(id) on delete cascade,
  usage_limit_per_user int not null default 1,
  applicable_cuisine_type varchar(80)
);

create table if not exists coupon_redemptions (
  id bigserial primary key,
  coupon_id bigint not null references coupons(id) on delete cascade,
  user_id bigint not null references users(id) on delete cascade,
  order_id bigint references orders(id) on delete set null,
  redeemed_at timestamptz not null default now()
);
create index if not exists idx_coupon_redemptions_user_coupon on coupon_redemptions(user_id, coupon_id);

create table if not exists favorites_restaurants (
  id bigserial primary key,
  user_id bigint not null references users(id) on delete cascade,
  restaurant_id bigint not null references restaurants(id) on delete cascade,
  created_at timestamptz not null default now(),
  constraint uk_fav_user_restaurant unique(user_id, restaurant_id)
);

create table if not exists recent_restaurants (
  id bigserial primary key,
  user_id bigint not null references users(id) on delete cascade,
  restaurant_id bigint not null references restaurants(id) on delete cascade,
  viewed_at timestamptz not null default now()
);
create index if not exists idx_recent_user_viewed on recent_restaurants(user_id, viewed_at desc);

alter table reviews
  add column if not exists status varchar(20) not null default 'VISIBLE';

alter table orders
  add column if not exists discount_amount bigint not null default 0,
  add column if not exists applied_coupon_code varchar(80),
  add column if not exists pricing_snapshot_json text;
