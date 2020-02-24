CREATE TABLE orders (
  order_id BIGINT NOT NULL PRIMARY KEY,
  region VARCHAR(255),
country VARCHAR(255),
item_type VARCHAR(255),
sales_channel VARCHAR(255),
order_priority VARCHAR(255),
order_date DATE,
ship_date DATE,
units_sold INT,
unit_price DECIMAL,
unit_cost DECIMAL,
total_revenue DECIMAL,
total_cost DECIMAL,
total_profit DECIMAL,
line_number INT,
job_execution_id BIGINT
);

CREATE TABLE error_log (
  error_log_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  exception_name VARCHAR(255),
line_number INT,
job_execution_id BIGINT
);
