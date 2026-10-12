-- Migration: adds login-lockout and verification-code-expiry support
-- to an EXISTING community_service_db database. Run this once:
--   mysql -u root -p community_service_db < migration_v2.sql
--
-- (If you're setting up a brand-new database instead, just use schema.sql —
-- it already includes these columns.)

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS failed_login_attempts INT NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS lockout_until DATETIME NULL,
    ADD COLUMN IF NOT EXISTS verification_code_expiry DATETIME NULL;
