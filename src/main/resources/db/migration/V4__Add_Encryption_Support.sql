-- Migration V4: Add encryption support for chat messages
-- 
-- This migration prepares the database for encrypted messages.
-- Note: Existing plain text messages in the database need to be encrypted.
-- A data migration utility (DataMigrationService) will be used to encrypt existing messages.
-- 
-- After this migration runs, all new messages will be encrypted automatically.
-- Run the /api/v1/admin/encrypt-existing-chats endpoint to encrypt existing data.

-- Add a column to track if message is encrypted (temporary, for migration purposes)
ALTER TABLE chats ADD COLUMN is_encrypted BOOLEAN DEFAULT FALSE;

-- Add index for faster lookup of unencrypted messages
CREATE INDEX idx_is_encrypted ON chats(is_encrypted);
