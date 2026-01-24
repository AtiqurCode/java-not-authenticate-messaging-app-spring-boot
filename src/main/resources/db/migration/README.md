# Database Migration Files

This directory contains Flyway migration scripts for the Spring Test application.

## Migration Files

### V1__Create_Users_And_Chats_Tables.sql
Creates the initial database schema with:
- **users table**: Stores user information with UUID, name, email, phone, and timestamps
- **chats table**: Stores chat messages with foreign key relationships to users

#### Users Table Schema
- `id` (BIGINT): Primary key, auto-increment
- `uuid` (VARCHAR): Unique identifier for external references
- `name` (VARCHAR): User's full name
- `email` (VARCHAR): User's email address (unique)
- `phone` (VARCHAR): User's phone number (optional)
- `created_at` (TIMESTAMP): Record creation timestamp
- `updated_at` (TIMESTAMP): Record last update timestamp

#### Chats Table Schema
- `id` (BIGINT): Primary key, auto-increment
- `uuid` (VARCHAR): Unique identifier for external references
- `chat_from` (BIGINT): Foreign key to users table (sender)
- `chat_to` (BIGINT): Foreign key to users table (receiver)
- `message` (TEXT): Chat message content
- `created_at` (TIMESTAMP): Message creation timestamp

### V2__Insert_Sample_Data.sql
Populates the database with sample data:
- **10 sample users** with realistic names, emails, and phone numbers
- **25 sample chat messages** showing various conversation patterns

## Sample Data

### Users (10 records)
1. John Doe - john.doe@example.com
2. Jane Smith - jane.smith@example.com
3. Mike Johnson - mike.johnson@example.com
4. Sarah Williams - sarah.williams@example.com
5. David Brown - david.brown@example.com
6. Emily Davis - emily.davis@example.com
7. Robert Miller - robert.miller@example.com
8. Lisa Anderson - lisa.anderson@example.com
9. James Taylor - james.taylor@example.com
10. Maria Garcia - maria.garcia@example.com

### Chat Messages (25 records)
- Conversations between users spanning the last 15 hours
- Multiple chat patterns: one-on-one conversations, greetings, work-related messages
- Realistic message timing using `DATE_SUB()` function

## How to Use

### First Time Setup
1. Ensure MySQL database 'springtest' exists
2. Update database credentials in `application.properties`
3. Run the application - Flyway will automatically execute migrations

### Manual Migration
```bash
# Run Flyway migrations manually
mvn flyway:migrate

# Check migration status
mvn flyway:info

# Clean database (development only)
mvn flyway:clean
```

## Flyway Configuration
Configuration is in `src/main/resources/application.properties`:
```properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true
```

## Adding New Migrations

1. Create a new file following the naming convention: `V{version}__{description}.sql`
   - Example: `V3__Add_User_Profile_Table.sql`
2. Write your SQL DDL/DML statements
3. Place the file in `src/main/resources/db/migration`
4. Restart the application or run `mvn flyway:migrate`

## Notes

- Migrations are executed in version order (V1, V2, V3, etc.)
- Once executed, migrations are tracked in `flyway_schema_history` table
- Do not modify executed migration files - create new ones instead
- Use `spring.jpa.hibernate.ddl-auto=validate` to prevent JPA from auto-creating tables
