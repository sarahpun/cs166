-- drop index if they exist
DROP INDEX IF EXISTS idx_users_login;
DROP INDEX IF EXISTS idx_users_role;
DROP INDEX IF EXISTS idx_catalog_price;
DROP INDEX IF EXISTS idx_catalog_gameID;

-- Users table indexes
CREATE INDEX idx_users_login 
ON Users 
USING BTREE
(login);

CREATE INDEX idx_users_role 
ON Users 
USING BTREE
(role);

-- Catalog table indexes
CREATE INDEX idx_catalog_gameID 
ON Catalog
USING BTREE 
(gameID);

CREATE INDEX idx_catalog_price 
ON Catalog
USING BTREE
(price);