-- Migration: adds opportunity/event management tables for the NGO dashboard
-- (events, applications, attendance, reports). Run this once on an EXISTING
-- community_service_db database:
--   mysql -u root -p community_service_db < migration_v3.sql
--
-- Note: this does NOT duplicate users/ngos/volunteers — the ported dashboard
-- code was adjusted to read/write those existing tables directly (e.g. a
-- volunteer's total_hours, phone_number, bio, etc. all already live on the
-- existing `volunteers` table).

CREATE TABLE IF NOT EXISTS opportunities (
    opportunity_id        INT AUTO_INCREMENT PRIMARY KEY,
    ngo_user_id           INT NOT NULL,               -- users.user_id of the NGO account that created this
    title                 VARCHAR(200) NOT NULL,
    description           TEXT,
    date                  DATE,
    application_deadline  DATE,
    start_time            TIME,
    end_time              TIME,
    location              VARCHAR(255),
    location_notes        VARCHAR(255),
    max_slots             INT,
    current_slots         INT DEFAULT 0,
    minimum_age           INT,
    category              VARCHAR(100),
    status                VARCHAR(20) DEFAULT 'Draft', -- Draft / Open / Closed / Cancelled / Completed
    required_skills       TEXT,
    CONSTRAINT fk_opportunities_ngo_user FOREIGN KEY (ngo_user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS opportunity_applications (
    application_id     INT AUTO_INCREMENT PRIMARY KEY,
    opportunity_id     INT NOT NULL,
    volunteer_id       INT NOT NULL,
    application_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status             VARCHAR(20) DEFAULT 'Pending', -- Pending / Approved / Rejected
    rejection_reason   TEXT,
    reviewed_by        INT NULL,                       -- users.user_id of the NGO reviewer
    reviewed_date       TIMESTAMP NULL,
    CONSTRAINT fk_applications_opportunity FOREIGN KEY (opportunity_id) REFERENCES opportunities(opportunity_id) ON DELETE CASCADE,
    CONSTRAINT fk_applications_volunteer FOREIGN KEY (volunteer_id) REFERENCES volunteers(volunteer_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS attendance_records (
    attendance_id     INT AUTO_INCREMENT PRIMARY KEY,
    volunteer_id      INT NOT NULL,
    opportunity_id    INT NOT NULL,
    check_in_time     TIMESTAMP NULL,
    status            VARCHAR(20) DEFAULT 'Present', -- Present / Completed
    hours_awarded     DECIMAL(5,2) DEFAULT 0,
    reviewed_by       INT NULL,                       -- users.user_id of the NGO reviewer
    CONSTRAINT fk_attendance_volunteer FOREIGN KEY (volunteer_id) REFERENCES volunteers(volunteer_id) ON DELETE CASCADE,
    CONSTRAINT fk_attendance_opportunity FOREIGN KEY (opportunity_id) REFERENCES opportunities(opportunity_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS reports (
    report_id       INT AUTO_INCREMENT PRIMARY KEY,
    generated_by    INT NOT NULL,   -- users.user_id of the NGO that generated it
    report_type     VARCHAR(50),
    title           VARCHAR(200),
    generated_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    start_date      DATE,
    end_date        DATE,
    file_format     VARCHAR(20),
    data_summary    TEXT,
    CONSTRAINT fk_reports_user FOREIGN KEY (generated_by) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;
