-- MySQL doesn't support `ADD COLUMN IF NOT EXISTS` in all versions.
-- Make this migration safe to re-run by conditionally adding the column.
SET @column_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'chats'
      AND COLUMN_NAME = 'updated_at'
);

SET @sql := IF(
    @column_exists = 0,
    'ALTER TABLE chats ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP',
    'SELECT 1'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
