-- ----------------------------
-- Schema for db_api
-- ----------------------------
CREATE SCHEMA IF NOT EXISTS sch_application;
-- ----------------------------
-- Table structure for product
-- ----------------------------
CREATE TABLE IF NOT EXISTS db_api.sch_application.product (
    id TEXT PRIMARY KEY UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    price_in_cents INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    user_id TEXT,
    CONSTRAINT fk_product_user FOREIGN KEY(user_id) REFERENCES db_api.sch_application.tb_user(id)
)