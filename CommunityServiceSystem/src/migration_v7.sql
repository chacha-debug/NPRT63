-- Security hardening: OTP attempt limits, resend throttling, and administrator approval.
ALTER TABLE users
    ADD COLUMN verification_attempts INT NOT NULL DEFAULT 0,
    ADD COLUMN reset_attempts INT NOT NULL DEFAULT 0,
    ADD COLUMN verification_resend_available_at DATETIME NULL,
    ADD COLUMN reset_resend_available_at DATETIME NULL,
    ADD COLUMN admin_approval_status VARCHAR(20) NOT NULL DEFAULT 'APPROVED';
