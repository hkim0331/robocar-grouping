create table groups (
  id SERIAL PRIMARY KEY,
  uhour char(4),
  members TEXT,
  create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
