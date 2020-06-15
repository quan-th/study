create table customer
(
  customer_id VARCHAR(128) NOT NULL,
  customer_code VARCHAR(128) NOT NULL,
  customer_name VARCHAR(128),
  sex VARCHAR(10),
  age int,
  address VARCHAR(128),
  register_timestamp BIGINT(11),
  UNIQUE INDEX customer_code (customer_code)
);