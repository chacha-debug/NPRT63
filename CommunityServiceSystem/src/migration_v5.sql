-- Migration: adds the override_flag column the volunteer-side application
-- code expects (lets an NGO approve an application even when normal
-- capacity/eligibility checks would otherwise block it). Run this once:
--   mysql -u root -p community_service_db < migration_v5.sql

ALTER TABLE opportunity_applications
    ADD COLUMN override_flag BOOLEAN DEFAULT FALSE;
