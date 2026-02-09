# Quick Reference Guide

## 🎯 Customization Checklist

Before deploying, customize these items:

### 1. Personal Information
- [ ] Update your name in `frontend/index.html` (title, heading, navbar)
- [ ] Set birth date in `backend/src/main/resources/application.properties`
- [ ] Update social links in footer (GitHub, LinkedIn, Twitter)

### 2. Projects
- [ ] Add your projects to database via SQL or API
- [ ] Include project descriptions, tech stacks, links
- [ ] Add project images to `frontend/assets/`

### 3. Skills
- [ ] Update skills in database with your expertise
- [ ] Adjust categories (Backend, Frontend, Infra, AI)
- [ ] Set proficiency levels (1-5)

### 4. Content
- [ ] Update "About" section text
- [ ] Update tagline and hero text
- [ ] Add your actual contact information

### 5. Security
- [ ] Change JWT secret in `application.properties`
- [ ] Enable HTTPS in production
- [ ] Update CORS origins for your domain
- [ ] Set strong database password

### 6. Styling (Optional)
- [ ] Customize colors in Tailwind config
- [ ] Adjust spacing/sizing if desired
- [ ] Change font if preferred
- [ ] Add your logo/branding

---

## 📊 Key Files Reference

| File | Purpose | Edit For |
|------|---------|----------|
| `frontend/index.html` | Main UI | Content, structure, styling |
| `frontend/js/main.js` | Frontend logic | API endpoints, behavior |
| `backend/pom.xml` | Dependencies | Adding libraries |
| `backend/src/main/resources/application.properties` | Backend config | Database, JWT, ports |
| `database/schema.sql` | Database | Projects, skills, data |
| `docs/README.md` | Documentation | Setup instructions |

---

## 🔑 Important Configuration Values

```properties
# Backend Port
server.port=8080

# JWT Token Expiration (milliseconds)
jwt.expiration=86400000  # 24 hours

# Your Birth Date (for time counter)
portfolio.birth-date=1995-07-15

# Database Connection (change for production)
spring.datasource.url=jdbc:h2:mem:portfoliodb

# Frontend API Base URL
const API_BASE_URL = 'http://localhost:8080/api';
```

---

## 🚀 Common Commands

### Build Backend
```bash
cd backend
mvn clean install
```

### Run Backend
```bash
cd backend
mvn spring-boot:run
```

### Run Frontend (Python)
```bash
cd frontend
python -m http.server 3000
```

### View Database (H2)
Navigate to: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:portfoliodb`
- Username: `sa`
- Password: (leave blank)

### Insert Sample Data
```bash
# Connect to database and run:
# Mac/Linux:
psql -U postgres -d portfolio_db -f database/schema.sql

# Windows PowerShell:
psql -U postgres -d portfolio_db -f database/schema.sql

# Or use H2 console in browser
```

### Deploy with Docker
```bash
docker-compose up -d

# Check logs
docker-compose logs -f backend

# Stop
docker-compose down
```

---

## 🛠️ Troubleshooting Quick Fixes

### Backend won't start
```bash
# Check if port 8080 is in use
netstat -ano | findstr :8080

# Use different port
# Edit application.properties: server.port=8081

# Check Java version
java -version  # Should be 17+

# Clean build
mvn clean
mvn install
```

### Frontend not connecting to backend
```javascript
// In frontend/js/main.js
// Change API_BASE_URL to match your backend
const API_BASE_URL = 'http://localhost:8080/api';
```

### Database errors
```bash
# H2 automatically creates database
# For PostgreSQL, create first:
createdb portfolio_db

# Run schema file:
psql -U postgres -d portfolio_db -f database/schema.sql
```

### CORS errors
1. Check frontend URL is in CORS origins in `PortfolioApplication.java`
2. Restart backend after changes
3. Clear browser cache (Ctrl+Shift+Delete)

### Token expiration
```properties
# Increase token expiration (in milliseconds)
jwt.expiration=604800000  # 7 days instead of 24 hours
```

---

## 📱 API Testing

### Test Visitor Counter
```bash
# Get count
curl http://localhost:8080/api/visitors/count

# Increment visitor
curl -X POST http://localhost:8080/api/visitors/increment
```

### Test Projects
```bash
# Get all projects
curl http://localhost:8080/api/projects

# Get specific project
curl http://localhost:8080/api/projects/1
```

### Test Skills
```bash
# Get all skills
curl http://localhost:8080/api/skills

# Get by category
curl http://localhost:8080/api/skills/category/Backend
```

### Test Auth
```bash
# Sign up
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","username":"testuser","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Send message (replace TOKEN with actual JWT)
curl -X POST http://localhost:8080/api/contact \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"subject":"Hello","message":"Great work!"}'
```

---

## 🎨 CSS Classes Reference

### Color Utilities
```html
<!-- Text Colors -->
<p class="text-dark-50">Light text</p>
<p class="text-dark-300">Medium text</p>
<p class="text-dark-600">Dark text</p>

<!-- Background Colors -->
<div class="bg-dark-900">Dark background</div>
<div class="bg-dark-800">Slightly lighter</div>

<!-- Borders -->
<div class="border border-subtle">With border</div>
```

### Animation Classes
```html
<!-- Fade in effect -->
<div class="animate-fade-in">Content</div>

<!-- Slide up effect -->
<div class="animate-slide-up">Content</div>

<!-- Hover effects -->
<div class="card-hover">Hover me</div>
```

### Responsive Layout
```html
<!-- Hidden on mobile, visible on desktop -->
<div class="hidden md:flex">Desktop only</div>

<!-- Responsive text sizes -->
<h1 class="text-3xl sm:text-4xl lg:text-5xl">Responsive heading</h1>

<!-- Responsive grid -->
<div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4">
  <!-- Responsive items -->
</div>
```

---

## 📚 File Structure Explained

```
hope/
├── backend/                    # Spring Boot application
│   ├── src/main/java/         # Java source code
│   │   └── com/portfolio/app/
│   │       ├── entity/        # Database entities (User, Project, etc.)
│   │       ├── dto/           # Data Transfer Objects
│   │       ├── service/       # Business logic
│   │       ├── controller/    # REST endpoints
│   │       ├── repository/    # Database queries
│   │       └── security/      # JWT and auth
│   ├── src/main/resources/
│   │   └── application.properties  # Configuration
│   └── pom.xml               # Maven dependencies
│
├── frontend/                   # Static website files
│   ├── index.html            # Main HTML page
│   ├── js/
│   │   └── main.js           # JavaScript logic
│   ├── assets/               # Images, icons, etc.
│   └── nginx.conf            # Nginx config for production
│
├── database/
│   └── schema.sql            # Database schema and seed data
│
├── docs/
│   ├── README.md             # Main documentation
│   └── DEPLOYMENT.md         # Deployment guide
│
└── docker-compose.yml        # Docker configuration (production)
```

---

## 🔐 Security Best Practices

### Before Production
1. **Change JWT Secret**
   ```properties
   jwt.secret=generate-32-character-random-string
   ```

2. **Update CORS Origins**
   ```java
   registry.addMapping("/api/**")
           .allowedOrigins("https://your-domain.com")
   ```

3. **Enable HTTPS**
   - Get SSL certificate (Let's Encrypt is free)
   - Configure in Nginx or load balancer

4. **Environment Variables**
   ```bash
   export SPRING_DATASOURCE_PASSWORD=secure_password
   export JWT_SECRET=your_random_secret
   ```

5. **Database Security**
   - Use strong password for database user
   - Restrict database access to application only
   - Enable SSL for database connections

---

## 📈 Performance Tips

### Frontend
- Images are lazy-loaded by browser
- Minimal CSS (Tailwind is tree-shaked)
- No heavy JavaScript libraries
- Single page app (no full page reloads)

### Backend
- Database queries are indexed
- Connection pooling enabled
- Gzip compression on responses
- Static content caching

### Optimization Ideas
1. Add Redis for session caching
2. Implement API response caching
3. Compress images with WebP
4. Use CDN for static assets
5. Enable database query optimization

---

## 📞 Support Resources

### Documentation
- [Backend README](../docs/README.md)
- [Deployment Guide](../docs/DEPLOYMENT.md)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Tailwind CSS Docs](https://tailwindcss.com/docs)

### Tools
- [JWT.io](https://jwt.io) - JWT debugging
- [Postman](https://www.postman.com/) - API testing
- [pgAdmin](https://www.pgadmin.org/) - Database management

### Debugging
1. Check browser console for frontend errors
2. Check backend logs: `mvn spring-boot:run` output
3. Use H2 console for database inspection
4. Use Postman to test APIs directly

---

## 🎯 Next Steps After Setup

1. **Test Locally**
   - Run backend: `mvn spring-boot:run`
   - Run frontend: `python -m http.server 3000`
   - Test all pages and features

2. **Customize Content**
   - Add your projects
   - Update skills
   - Change personal info

3. **Setup Domain**
   - Register domain name
   - Point DNS to your server

4. **Deploy**
   - Choose deployment option (Docker, Linux, Cloud)
   - Follow deployment guide
   - Test in production

5. **Monitor**
   - Setup error monitoring
   - Track visitor metrics
   - Monitor server performance

---

**Last Updated**: February 2026 | **Status**: Production Ready ✅
