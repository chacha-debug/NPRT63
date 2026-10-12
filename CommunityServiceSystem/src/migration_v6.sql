-- OPTIONAL: the app applies this automatically on startup (see SchemaUpdater.java).
-- Only run by hand if you prefer to. Plain ALTER works on both MySQL and MariaDB;
-- it will error with "Duplicate column" if the columns already exist.
ALTER TABLE users
    ADD COLUMN is_deleted      TINYINT(1)   NOT NULL DEFAULT 0,
    ADD COLUMN deleted_at      DATETIME     NULL,
    ADD COLUMN deleted_by      INT          NULL,
    ADD COLUMN deletion_reason VARCHAR(255) NULL;
