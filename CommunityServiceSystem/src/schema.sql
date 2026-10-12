-- Community Service+ — MySQL schema
-- This matches the DAOs (UserDAO / NGODAO / VolunteerDAO / AdminDAO) and the
-- database structure already in use for this project. Only needed if you're
-- setting up a fresh database — if you already have these tables, skip this file.

CREATE DATABASE IF NOT EXISTS community_service_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE community_service_db;

CREATE TABLE IF NOT EXISTS users (
    user_id                    INT AUTO_INCREMENT PRIMARY KEY,
    full_name                  VARCHAR(100)  NOT NULL,
    email                      VARCHAR(150)  NOT NULL UNIQUE,
    password_hash              VARCHAR(255)  NOT NULL,
    role                       ENUM('Volunteer','NGO','Administrator') NOT NULL,
    profile_pic                VARCHAR(255)  NULL,
    date_registered            DATETIME      DEFAULT CURRENT_TIMESTAMP,
    last_login                 DATETIME      NULL,
    is_active                  TINYINT(1)    DEFAULT 1,
    is_verified                TINYINT(1)    DEFAULT 0,
    verification_code          VARCHAR(10)   NULL,
    verification_code_expiry   DATETIME      NULL,
    reset_token                VARCHAR(255)  NULL,
    reset_token_expiry         DATETIME      NULL,
    failed_login_attempts      INT           NOT NULL DEFAULT 0,
    lockout_until              DATETIME      NULL,
    is_deleted                 TINYINT(1)    NOT NULL DEFAULT 0,
    deleted_at                 DATETIME      NULL,
    deleted_by                 INT           NULL,
    deletion_reason            VARCHAR(255)  NULL,
    verification_attempts      INT           NOT NULL DEFAULT 0,
    reset_attempts             INT           NOT NULL DEFAULT 0,
    verification_resend_available_at DATETIME NULL,
    reset_resend_available_at  DATETIME      NULL,
    admin_approval_status      VARCHAR(20)   NOT NULL DEFAULT 'APPROVED'
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS administrators (
    admin_id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id          INT NOT NULL UNIQUE,
    permissions      VARCHAR(100) NOT NULL,
    department       VARCHAR(100),
    employee_number  VARCHAR(50) UNIQUE,
    CONSTRAINT fk_admin_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS ngos (
    ngo_id               INT AUTO_INCREMENT PRIMARY KEY,
    user_id              INT NOT NULL UNIQUE,
    org_name             VARCHAR(150) NOT NULL,
    registration_number  VARCHAR(50) UNIQUE,
    contact_person       VARCHAR(100) NOT NULL,
    contact_phone        VARCHAR(20) NOT NULL,
    verified_status      TINYINT(1) DEFAULT 0,
    website              VARCHAR(255),
    CONSTRAINT fk_ngo_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS volunteers (
    volunteer_id             INT AUTO_INCREMENT PRIMARY KEY,
    user_id                  INT NOT NULL UNIQUE,
    total_hours              DECIMAL(10,2) DEFAULT 0.00,
    preferences              TEXT,
    academic_year            VARCHAR(20),
    date_of_birth            DATE,
    race                     VARCHAR(50),
    gender                   VARCHAR(50),
    phone_number             VARCHAR(20),
    completed_events_count   INT DEFAULT 0,
    bio                      TEXT,
    skills                   TEXT,
    interests                TEXT,
    availability             VARCHAR(255),
    location                 VARCHAR(100),
    emergency_contact_name   VARCHAR(100),
    emergency_contact_phone  VARCHAR(20),
    CONSTRAINT fk_volunteer_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;
