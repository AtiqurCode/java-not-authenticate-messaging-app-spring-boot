# Java Non-Authenticate Messaging App - Spring Boot

A real-time messaging application built with **Spring Boot** (Backend) and **Nuxt.js/Vue 3** (Frontend) that enables users to send, edit, and delete messages in real-time using **Pusher** for instant notifications.

**Author:** [@AtiqurCode](https://github.com/AtiqurCode)

---

## 📚 Project Structure

This is a **monorepo** containing two applications:

- **Backend (Java Spring Boot):** `/springchat` - REST API server
- **Frontend (Nuxt.js/Vue 3):** `https://github.com/AtiqurCode/java-not-authenticate-messaging-app-spring-boot-frontend` - Web UI

---

## ✨ Features

- ✅ Send real-time messages between users
- ✅ Edit messages with instant sync to recipient
- ✅ Delete messages with push notifications
- ✅ Real-time updates using Pusher
- ✅ User profiles and UUID-based identification
- ✅ Browser notifications for incoming messages
- ✅ Responsive design (Mobile & Desktop)
- ✅ Dark mode support
- ✅ Message timestamps with hover tooltips

---

## 🛠️ Tech Stack

### Backend (This Repository)
- **Framework:** Spring Boot 3.x
- **Language:** Java
- **Database:** MySQL
- **Real-time:** Pusher
- **Build Tool:** Maven
- **Database Migration:** Flyway

### Frontend
- **Framework:** Nuxt.js 3
- **Language:** TypeScript
- **Styling:** Tailwind CSS
- **Real-time Client:** Pusher.js
- **Package Manager:** pnpm

---

## 📋 Prerequisites

### Backend Requirements
- **Java** 21 or higher
- **Maven** 3.8.0 or higher
- **MySQL** 8.0 or higher
- **Git**

### Frontend Requirements
- **Node.js** 18 LTS or higher
- **pnpm** 8.0 or higher (or npm/yarn)
- **Git**

---

## 🚀 Quick Start

### 1. Backend Setup (Spring Boot)

#### Clone the repository
```bash
git clone https://github.com/AtiqurCode/java-not-authenticate-messaging-app-spring-boot.git
cd springchat
```

#### Configure Database
Create a MySQL database:
```sql
CREATE DATABASE springchat_db;
```

Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/springchat_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
```

#### Configure Pusher (Optional for Real-time Features)
Update Pusher credentials in your configuration:
```properties
pusher.app-id=YOUR_APP_ID
pusher.key=YOUR_APP_KEY
pusher.secret=YOUR_APP_SECRET
pusher.cluster=ap1
```

#### Build the project
```bash
./mvnw clean install
```

#### Run the server
```bash
./mvnw spring-boot:run
```

The backend will start on **http://localhost:8081**

---

### 2. Frontend Setup (Nuxt.js)

#### Clone the repository
```bash
git clone https://github.com/AtiqurCode/java-not-authenticate-messaging-app-spring-boot-frontend.git
cd chatmodule
```

#### Install dependencies
Using pnpm (recommended):
```bash
pnpm install
```

Or using npm:
```bash
npm install
```

#### Configure API Base URL
Update `nuxt.config.ts` with the backend API URL:
```typescript
export default defineNuxtConfig({
  runtimeConfig: {
    public: {
      apiBase: 'http://localhost:8081/api/v1'
    }
  }
})
```

#### Run the development server
```bash
pnpm dev
```

Or with npm:
```bash
npm run dev
```

The frontend will start on **http://localhost:3000**

---

## 📖 Usage Instructions

### 1. Access the Application
- Open your browser and navigate to **http://localhost:3000**

### 2. Set Your User ID
- Enter a unique UUID (you can generate one or use any string)
- This will be saved locally in your browser

### 3. Find a User to Chat
- Enter the recipient's UUID
- Click "Find" to fetch their profile
- Chats will auto-load after finding the user

### 4. Send Messages
- Type your message in the input field
- Press Enter or click Send
- Messages appear in real-time with your name

### 5. Edit Messages
- Hover over your message
- Click the blue pencil icon to edit
- Type the new message and click Save
- The recipient sees the update instantly via Pusher

### 6. Delete Messages
- Hover over your message
- Click the red trash icon to delete
- The message is removed from both users' screens in real-time

### 7. View Message Time
- Hover over any message
- A tooltip shows the exact date and time

---

## 🔌 API Endpoints

### Chat Endpoints
- **POST** `/api/v1/chats` - Create a new message
- **GET** `/api/v1/chats` - Get all messages
- **GET** `/api/v1/chats/{id}` - Get message by ID
- **PUT** `/api/v1/chats/{id}` - Update/edit a message
- **DELETE** `/api/v1/chats/{id}` - Delete a message
- **POST** `/api/v1/chats/between` - Get messages between two users
- **POST** `/api/v1/chats/sent` - Get messages sent by a user
- **POST** `/api/v1/chats/received` - Get messages received by a user

### User Endpoints
- **GET** `/api/v1/users/uuid/{uuid}` - Get user by UUID

### Pusher Test
- **POST** `/api/v1/chats/test-pusher/{userUuid}` - Test Pusher connectivity

---

## 📦 Database Schema

### Users Table
```sql
CREATE TABLE users (
  uuid VARCHAR(255) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Chats Table
```sql
CREATE TABLE chats (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  chat_from_uuid VARCHAR(255),
  chat_to_uuid VARCHAR(255),
  message LONGTEXT,
  is_encrypted BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL,
  FOREIGN KEY (chat_from_uuid) REFERENCES users(uuid),
  FOREIGN KEY (chat_to_uuid) REFERENCES users(uuid)
);
```

---

## 🔒 Security Notes

- This is a demonstration project with **no authentication**
- UUIDs are used for user identification (not secure for production)
- All messages are visible to anyone who knows the UUIDs
- For production use, implement proper authentication and encryption

---

## 📂 Project Structure

```
springchat/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/springtest/
│   │   │       ├── controller/
│   │   │       │   ├── ChatController.java
│   │   │       │   └── UserController.java
│   │   │       ├── service/
│   │   │       │   ├── ChatService.java
│   │   │       │   ├── UserService.java
│   │   │       │   └── PusherService.java
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── dto/
│   │   │       └── config/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   └── test/
├── pom.xml
└── mvnw
```

---

## 🧪 Testing

### Manual Testing
```bash
# Test Pusher connectivity
curl -X POST http://localhost:8081/api/v1/chats/test-pusher/{USER_UUID}

# Create a test message
curl -X POST http://localhost:8081/api/v1/chats \
  -H "Content-Type: application/json" \
  -d '{
    "chatFromUuid": "user-1",
    "chatToUuid": "user-2",
    "message": "Hello!"
  }'

# Get all messages
curl http://localhost:8081/api/v1/chats
```

---

## 🐛 Troubleshooting

### Backend Issues

**Port 8081 already in use:**
```bash
# Change port in application.properties
server.port=8082
```

**MySQL connection failed:**
- Ensure MySQL is running: `mysql -u root -p`
- Check database exists: `SHOW DATABASES;`
- Update credentials in `application.properties`

**Pusher not working:**
- Verify Pusher credentials are correct
- Check API limits and quotas on Pusher dashboard
- See backend logs for push event details

### Frontend Issues

**Port 3000 already in use:**
```bash
npm run dev -- --port 3001
```

**API not responding:**
- Verify backend is running on `http://localhost:8081`
- Check `nuxt.config.ts` API base URL
- Open browser DevTools → Network tab to see API calls

**Real-time updates not working:**
- Check Pusher connection in browser DevTools → Network
- Verify you're subscribed to correct channel: `chat-{YOUR_UUID}`
- Check browser console for errors

---

## 📚 Repository Links

- **Backend:** https://github.com/AtiqurCode/java-not-authenticate-messaging-app-spring-boot
- **Frontend:** https://github.com/AtiqurCode/java-not-authenticate-messaging-app-spring-boot-frontend

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

## 📄 License

This project is open source and available under the MIT License.

---

## 👨‍💻 Author

**Atiqur Code**
- GitHub: [@AtiqurCode](https://github.com/AtiqurCode)
- Email: Contact via GitHub

---

## 🙏 Acknowledgments

- Spring Boot framework
- Pusher for real-time capabilities
- Nuxt.js and Vue.js community
- MySQL and Flyway for database management

---

## 📞 Support

For issues, questions, or suggestions:
1. Open an issue on GitHub
2. Check existing issues and documentation
3. Contact the author via GitHub

---

**Happy Coding! 🚀**
