CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL CHECK (LENGTH(password) > 5),
    first_name VARCHAR(50),
    last_name VARCHAR(50)
);