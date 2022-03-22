CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR UNIQUE
);

CREATE TABLE products (
    sku VARCHAR PRIMARY KEY,
    name VARCHAR,
    category BIGINT REFERENCES categories,
    price INTEGER
);

CREATE INDEX products_price_category ON products(price, category);
CREATE INDEX categories_name ON categories(name);
