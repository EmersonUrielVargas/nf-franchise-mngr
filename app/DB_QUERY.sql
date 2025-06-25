CREATE SCHEMA IF NOT EXISTS franchises_app;

CREATE TABLE franchises_app.franchises (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
	created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS franchises_app.branches (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    franchise_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    CONSTRAINT fk_branches_franchise
        FOREIGN KEY (franchise_id)
        REFERENCES franchises_app.franchises (id)
        ON DELETE CASCADE 
        ON UPDATE CASCADE
);

CREATE TABLE franchises_app.products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    stock INT NOT NULL CHECK (stock >= 0),
    branch_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT fk_products_branch
        FOREIGN KEY (branch_id)
        REFERENCES franchises_app.branches (id) ON DELETE CASCADE
);



GRANT USAGE ON SCHEMA franchises_app TO franchises_app_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA franchises_app TO franchises_app_user;

GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA franchises_data TO franchises_app_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA franchises_app
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO franchises_app_user;

GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA franchises_app TO franchises_app_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA franchises_app
GRANT USAGE, SELECT ON SEQUENCES TO franchises_app_user;
