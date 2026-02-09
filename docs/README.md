# Personal Portfolio Website - Complete Setup Guide

## 📋 Project Overview

This is a **production-ready, high-end personal portfolio website** built with modern technologies:

- **Frontend**: HTML5, Tailwind CSS, Vanilla JavaScript (ES6+)
- **Backend**: Java Spring Boot with REST APIs
- **Database**: PostgreSQL (with H2 for local development)
- **Authentication**: JWT-based email/password system

### Key Features
✨ **Live Visitor Counter** - Track unique visitors in real-time
⏱️ **Time on Earth Counter** - Dynamic counter showing time alive
🔐 **JWT Authentication** - Secure signup/login system
📧 **Contact Messages** - Authenticated users can send messages
🎨 **Minimalist Design** - Black & white palette with premium typography
📱 **Fully Responsive** - Works perfectly on all devices
⚡ **Production Ready** - Optimized performance, SEO, and security

---

## 🚀 Quick Start (5 Minutes)

### Prerequisites
- **Java 17+** (for backend)
- **Maven 3.6+** (for building)
- **Node.js** (optional, only if using Node proxy)
- **PostgreSQL** (for production) - H2 included for local dev

### Step 1: Clone/Setup Project
```bash
cd c:\Users\2503p\DEVELOPMENTS\hope
```

### Step 2: Start Backend
```bash
cd backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Step 3: Open Frontend
```bash
cd frontend

# Simply open index.html in your browser
# OR use a simple HTTP server (Python):
python -m http.server 3000
```

Frontend will be available at `http://localhost:3000`

---

## 📁 Project Structure

```
hope/
├── backend/
│   ├── pom.xml                          # Maven dependencies
│   ├── src/main/
│   │   ├── java/com/portfolio/app/
│   │   │   ├── PortfolioApplication.java    # Main class
│   │   │   ├── controller/                  # REST endpoints
│   │   │   ├── service/                     # Business logic
│   │   │   ├── entity/                      # JPA entities
│   │   │   ├── dto/                         # Data transfer objects
│   │   │   ├── repository/                  # Database access
│   │   │   ├── security/                    # JWT & security
│   │   │   └── config/                      # Spring configuration
│   │   └── resources/
│   │       └── application.properties       # Configuration
│   └── target/                          # Compiled output
│
├── frontend/
│   ├── index.html                       # Main HTML
│   ├── js/
│   │   └── main.js                      # JavaScript logic
│   └── assets/                          # Images, icons, etc.
│
├── database/
│   └── schema.sql                       # Database schema & seed data
│
└── docs/
    ├── README.md                        # This file
    ├── DEPLOYMENT.md                    # Deployment guide
    ├── API.md                           # API documentation
    └── CONFIGURATION.md                 # Configuration options
```

---

## 🔧 Backend Configuration

### application.properties

Located at: `backend/src/main/resources/application.properties`

Key settings to customize:

```properties
# Server
server.port=8080

# Database (Development - H2)
spring.datasource.url=jdbc:h2:mem:portfoliodb

# JWT
jwt.secret=your-super-secret-key-change-this-in-production
jwt.expiration=86400000  # 24 hours in ms

# Your birth date (for time counter)
portfolio.birth-date=1995-07-15
```

### Connect to PostgreSQL (Production)

Uncomment these lines in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/portfolio_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL10Dialect
```

---

## 📊 REST API Endpoints

### Authentication
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/login` - Login user

### Public Data
- `GET /api/visitors/count` - Get total visitor count
- `POST /api/visitors/increment` - Track new visitor
- `GET /api/time-on-earth` - Get time data
- `GET /api/projects` - Get all projects
- `GET /api/projects/{id}` - Get project details
- `GET /api/skills` - Get all skills
- `GET /api/skills/category/{category}` - Get skills by category

### Authenticated Routes
- `POST /api/contact` - Send contact message (requires JWT token)

### Request Examples

**Sign Up:**
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "username": "john_doe",
    "password": "secure_password"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "secure_password"
  }'
```

**Send Message (Authenticated):**
```bash
curl -X POST http://localhost:8080/api/contact \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "subject": "Project Inquiry",
    "message": "I loved your work on the AI system!"
  }'
```

---

## 🗄️ Database Schema

### Users Table
- `id` - Primary key
- `email` - Unique, required
- `password` - Bcrypt hashed
- `username` - Display name
- `roles` - User roles (ROLE_USER, ROLE_ADMIN)

### Visitors Table
- `id` - Primary key
- `ip_address` - Unique visitor identifier
- `user_agent` - Browser info
- `visited_at` - Timestamp

### Projects Table
- `id` - Primary key
- `title`, `description`, `short_description`
- `tech_stack` - Many-to-many with tech tags
- `github_url`, `live_url`
- `image_url` - Project screenshot
- `order` - Display order

### Skills Table
- `id` - Primary key
- `name` - Skill name
- `category` - Backend, Frontend, Infra, AI
- `proficiency` - 1-5 rating
- `order` - Display order

### Contact Messages Table
- `id` - Primary key
- `user_id` - Foreign key to users
- `subject`, `message`
- `is_read` - Admin flag
- `created_at` - Timestamp

---

## 🎨 Customization Guide

### Change Your Name and Branding

Edit `frontend/index.html`:
```html
<!-- Change site title -->
<title>Your Name — Engineer | Builder | Thinker</title>

<!-- Change navbar logo -->
<a href="#home" class="text-xl font-display font-bold">your.name</a>

<!-- Change hero text -->
<h1 class="text-5xl sm:text-6xl lg:text-7xl font-display font-bold mb-6">
    Engineer,<br>Builder,<br>Thinker
</h1>
<p>I craft digital experiences with precision and purpose...</p>
```

### Update Your Birth Date

Edit `backend/src/main/resources/application.properties`:
```properties
portfolio.birth-date=1990-01-15  # Your actual birth date
```

### Customize Colors

Edit `frontend/index.html` Tailwind config:
```javascript
colors: {
    'dark': {
        50: '#f9f9f9',
        900: '#1a1a1a',
        950: '#0f0f0f',
        // Adjust these values
    }
}
```

### Add/Edit Projects

**Option 1: Via Database**
```sql
INSERT INTO projects (title, short_description, description, github_url, live_url, `order`)
VALUES ('My Amazing Project', 'Short desc', 'Long description', 'https://github.com/...', 'https://...', 1);
```

**Option 2: Via API (Admin endpoint - can be added)**
POST /api/admin/projects with auth token

### Add/Edit Skills

```sql
INSERT INTO skills (name, category, proficiency, `order`)
VALUES ('Docker', 'Infra', 5, 1);
```

---

## 🔐 Security Checklist

### Before Production:

1. **Change JWT Secret**
   ```properties
   jwt.secret=generate-a-long-random-string-here-minimum-32-chars
   ```

2. **Use Environment Variables**
   ```bash
   export DB_URL=jdbc:postgresql://prod-host:5432/portfolio
   export JWT_SECRET=your-secret-key
   export JWT_EXPIRATION=86400000
   ```

3. **Enable HTTPS**
   - Configure SSL/TLS certificate
   - Redirect HTTP to HTTPS

4. **Set Secure Headers**
   ```java
   // Add to SecurityConfig.java
   .headers().contentSecurityPolicy("default-src 'self'")
   ```

5. **CORS Configuration**
   - Update `PortfolioApplication.java` CORS origins
   - Only allow your domain in production

6. **Rate Limiting**
   - Consider adding Spring Cloud Gateway
   - Implement auth endpoint rate limiting

---

## 📈 Performance Optimization

### Frontend
- ✅ Minimal CSS (Tailwind)
- ✅ Vanilla JS (no heavy frameworks)
- ✅ Lazy loading images
- ✅ Code splitting

### Backend
- ✅ Connection pooling
- ✅ Database indexing
- ✅ Caching headers
- ✅ Gzip compression

### Enable Caching

Add to `application.properties`:
```properties
spring.h2.console.settings.trace=false
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

---

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Find process on port 8080
netstat -ano | findstr :8080

# Kill process (Windows)
taskkill /PID <PID> /F

# Or use different port
# Edit application.properties: server.port=8081
```

### Database Connection Issues
```bash
# Check PostgreSQL is running
psql -U postgres

# Create database
CREATE DATABASE portfolio_db;
```

### CORS Errors
- Check frontend URL is in CORS origins
- Ensure backend is responding correctly
- Check browser console for specific errors

### JWT Token Issues
- Verify token is in Authorization header as: `Bearer <token>`
- Check token hasn't expired (24 hours by default)
- Ensure JWT secret matches between signup/login and validate

---

## 📚 Additional Resources

### Documentation Files
- [API_DOCUMENTATION.md](API_DOCUMENTATION.md) - Detailed API specs
- [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) - Production deployment
- [CONFIGURATION_ADVANCED.md](CONFIGURATION_ADVANCED.md) - Advanced settings

### External Resources
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Tailwind CSS Docs](https://tailwindcss.com/docs)
- [JWT.io](https://jwt.io)
- [PostgreSQL Docs](https://www.postgresql.org/docs/)

---

## 📄 License

This project is provided as-is for personal use. Customize and deploy freely!

---

## 🤝 Support

For issues or questions:
1. Check the troubleshooting section above
2. Review backend logs: `tail -f target/*.log`
3. Check browser DevTools console for frontend errors
4. Verify all services are running on expected ports

---

**Last Updated**: February 2026
**Status**: Production Ready ✅
