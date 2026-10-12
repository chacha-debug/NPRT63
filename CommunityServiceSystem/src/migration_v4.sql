-- Migration: adds certificate/audit/policy/feedback tables for the volunteer
-- dashboard, plus one column reports was missing. Run this once on an
-- EXISTING community_service_db database:
--   mysql -u root -p community_service_db < migration_v4.sql

ALTER TABLE reports
    ADD COLUMN file_url VARCHAR(255) NULL;

CREATE TABLE IF NOT EXISTS certificates (
    certificate_id        INT AUTO_INCREMENT PRIMARY KEY,
    volunteer_id          INT NOT NULL,
    opportunity_id        INT NOT NULL,
    issue_date            DATE,
    pdf_url               VARCHAR(255),
    certification_number  VARCHAR(100),
    CONSTRAINT fk_certificates_volunteer FOREIGN KEY (volunteer_id) REFERENCES volunteers(volunteer_id) ON DELETE CASCADE,
    CONSTRAINT fk_certificates_opportunity FOREIGN KEY (opportunity_id) REFERENCES opportunities(opportunity_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS audit_logs (
    audit_log_id  INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT NULL,
    action        VARCHAR(100),
    entity_type   VARCHAR(50),
    entity_id     INT,
    ip_address    VARCHAR(45),
    `timestamp`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details       TEXT,
    status        VARCHAR(20),
    CONSTRAINT fk_auditlog_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS system_policies (
    system_policy_id  INT AUTO_INCREMENT PRIMARY KEY,
    policy_title      VARCHAR(200),
    policy_content    TEXT,
    version           VARCHAR(20),
    effective_date    DATE,
    last_updated_by   INT NULL,
    last_update_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active         BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_systempolicy_user FOREIGN KEY (last_updated_by) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS feedback_entries (
    feedback_id       INT AUTO_INCREMENT PRIMARY KEY,
    volunteer_id      INT NOT NULL,
    opportunity_id    INT NOT NULL,
    rating            INT,
    comment           TEXT,
    submission_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_feedback_volunteer FOREIGN KEY (volunteer_id) REFERENCES volunteers(volunteer_id) ON DELETE CASCADE,
    CONSTRAINT fk_feedback_opportunity FOREIGN KEY (opportunity_id) REFERENCES opportunities(opportunity_id) ON DELETE CASCADE
) ENGINE=InnoDB;
