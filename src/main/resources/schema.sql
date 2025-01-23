CREATE TABLE IF NOT EXISTS order_events (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL,
    employee_id INTEGER,
    event_date_time TIMESTAMP NOT NULL,
    event_data TEXT NOT NULL,
    event_type VARCHAR(50) NOT NULL
);