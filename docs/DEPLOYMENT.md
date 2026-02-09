# Deployment Guide

## 🚀 Production Deployment

### Prerequisites
- Linux server (Ubuntu 20.04+ recommended)
- Java 17 runtime
- PostgreSQL 13+
- Docker & Docker Compose (recommended)
- Domain name with DNS configured
- SSL certificate (Let's Encrypt is free)

---

## Option 1: Docker Deployment (Recommended)

### Step 1: Create Docker Files

Create `backend/Dockerfile`:
```dockerfile
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/target/portfolio-app-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Create `frontend/Dockerfile`:
```dockerfile
FROM nginx:alpine
COPY index.html /usr/share/nginx/html/
COPY js /usr/share/nginx/html/js/
COPY assets /usr/share/nginx/html/assets/
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
```

Create `docker-compose.yml` (root directory):
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: portfolio_db
      POSTGRES_USER: portfolio_user
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./database/schema.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U portfolio_user"]
      interval: 10s
      timeout: 5s
      retries: 5

  backend:
    build: ./backend
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/portfolio_db
      SPRING_DATASOURCE_USERNAME: portfolio_user
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
      JWT_SECRET: ${JWT_SECRET}
      PORTFOLIO_BIRTH_DATE: ${BIRTH_DATE}
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy

  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  postgres_data:
```

### Step 2: Create `.env` File
```bash
DB_PASSWORD=your_secure_password_here
JWT_SECRET=generate_a_long_random_string_here_minimum_32_characters
BIRTH_DATE=1995-07-15
```

### Step 3: Deploy
```bash
docker-compose up -d

# View logs
docker-compose logs -f backend

# Stop
docker-compose down
```

---

## Option 2: Traditional Linux Deployment

### Step 1: Server Setup
```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Java
sudo apt install -y openjdk-17-jdk

# Install PostgreSQL
sudo apt install -y postgresql postgresql-contrib

# Create database
sudo -u postgres createdb portfolio_db
sudo -u postgres createuser portfolio_user -P
sudo -u postgres psql -c "ALTER ROLE portfolio_user WITH PASSWORD 'your_password';"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE portfolio_db TO portfolio_user;"
```

### Step 2: Build Backend
```bash
cd /opt/portfolio/backend
mvn clean package -DskipTests

# Run as systemd service
sudo nano /etc/systemd/system/portfolio-backend.service
```

Add to service file:
```ini
[Unit]
Description=Portfolio Backend Service
After=network.target postgresql.service

[Service]
Type=simple
User=portfolio
WorkingDirectory=/opt/portfolio/backend
ExecStart=/usr/bin/java -jar target/portfolio-app-1.0.0.jar
Restart=on-failure
RestartSec=10

Environment="SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/portfolio_db"
Environment="SPRING_DATASOURCE_USERNAME=portfolio_user"
Environment="SPRING_DATASOURCE_PASSWORD=your_password"
Environment="JWT_SECRET=your_jwt_secret"

[Install]
WantedBy=multi-user.target
```

### Step 3: Setup Frontend
```bash
# Using Nginx as reverse proxy
sudo apt install -y nginx

# Create Nginx config
sudo nano /etc/nginx/sites-available/portfolio
```

Add:
```nginx
upstream backend {
    server localhost:8080;
}

server {
    listen 80;
    server_name your-domain.com www.your-domain.com;
    
    # Redirect HTTP to HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name your-domain.com www.your-domain.com;
    
    ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;
    
    root /var/www/portfolio;
    index index.html;
    
    # Frontend
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # API Proxy
    location /api/ {
        proxy_pass http://backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
    
    # Security headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header Referrer-Policy "strict-origin-when-cross-origin" always;
}
```

### Step 4: Enable HTTPS
```bash
sudo apt install -y certbot python3-certbot-nginx
sudo certbot certonly --nginx -d your-domain.com -d www.your-domain.com
sudo systemctl restart nginx
```

### Step 5: Start Services
```bash
sudo systemctl daemon-reload
sudo systemctl enable portfolio-backend
sudo systemctl start portfolio-backend
sudo systemctl enable nginx
sudo systemctl start nginx

# Check status
sudo systemctl status portfolio-backend
sudo systemctl status nginx
```

---

## Option 3: AWS EC2 Deployment

### Step 1: Launch EC2 Instance
- AMI: Ubuntu 20.04 LTS
- Instance Type: t3.small (or larger)
- Security Group: Allow ports 80, 443, 22

### Step 2: Connect and Setup
```bash
ssh -i your-key.pem ubuntu@your-ec2-ip

# Run the Linux deployment steps above
```

### Step 3: Configure RDS Database
- Create RDS PostgreSQL instance
- Update connection string in environment variables
- Ensure security group allows EC2 to RDS

### Step 4: Use Elastic IP
- Allocate Elastic IP
- Associate with EC2 instance
- Update domain DNS to point to Elastic IP

### Step 5: Auto Scaling (Optional)
```bash
# Create AMI from configured instance
# Setup Launch Template with current AMI
# Create Auto Scaling Group (min: 2, desired: 3, max: 5)
# Configure Application Load Balancer
```

---

## Option 4: Heroku Deployment

### Step 1: Setup Heroku
```bash
# Install Heroku CLI
# Login
heroku login

# Create app
heroku create portfolio-app

# Add PostgreSQL addon
heroku addons:create heroku-postgresql:standard-0
```

### Step 2: Configure Environment
```bash
heroku config:set JWT_SECRET="your_secret_key"
heroku config:set PORTFOLIO_BIRTH_DATE="1995-07-15"
```

### Step 3: Create Procfile
```
web: java -Dserver.port=$PORT $JAVA_OPTS -jar target/portfolio-app-1.0.0.jar
```

### Step 4: Deploy
```bash
git push heroku main
heroku logs --tail
```

---

## Option 5: DigitalOcean App Platform

### Step 1: Create App
- Connect GitHub repository
- DigitalOcean automatically detects Spring Boot app

### Step 2: Add Database
- Create managed PostgreSQL database
- Update environment variables

### Step 3: Configure
Add to `app.yaml`:
```yaml
name: portfolio
services:
- name: api
  github:
    branch: main
    repo: your-username/portfolio
  build_command: mvn clean package -DskipTests
  run_command: java -jar target/portfolio-app-1.0.0.jar
  http_port: 8080
  env:
  - key: SPRING_DATASOURCE_URL
    value: ${db.connection_string}
  - key: JWT_SECRET
    value: ${JWT_SECRET}

- name: web
  github:
    branch: main
    repo: your-username/portfolio
    source_dir: frontend
  build_command: npm run build
  source_dir: frontend
  http_port: 3000

databases:
- name: db
  engine: PG
  version: "12"
```

---

## Monitoring & Maintenance

### Health Checks
```bash
# Backend health
curl http://localhost:8080/actuator/health

# Frontend check
curl http://localhost/
```

### Database Backups
```bash
# Automated backup (daily)
sudo pg_dump -U portfolio_user portfolio_db | gzip > /backups/db_$(date +%Y%m%d).sql.gz

# Restore
gunzip < db_backup.sql.gz | psql -U portfolio_user portfolio_db
```

### Log Monitoring
```bash
# Backend logs
tail -f /var/log/portfolio/backend.log

# Nginx logs
tail -f /var/log/nginx/access.log
tail -f /var/log/nginx/error.log
```

### Performance Monitoring
- Setup DataDog or New Relic
- Monitor CPU, Memory, Disk
- Track API response times
- Alert on anomalies

---

## SSL/TLS Configuration

### Auto-Renewal with Certbot
```bash
# Test auto-renewal
sudo certbot renew --dry-run

# Setup cron job
sudo crontab -e
# Add: 0 3 * * * certbot renew --quiet && systemctl reload nginx
```

---

## Scaling Considerations

### Horizontal Scaling (Multiple Servers)
1. Setup load balancer (AWS ALB, Nginx)
2. Run multiple backend instances
3. Use shared PostgreSQL database
4. Use Redis for session caching

### Vertical Scaling
- Increase instance size
- Increase database resources
- Optimize queries and indexes

### Caching Strategy
```properties
# Add Redis
spring.redis.host=localhost
spring.redis.port=6379

# Cache projects/skills (rarely change)
@Cacheable(value = "projects")
public List<ProjectDTO> getAllProjects() { ... }
```

---

## Security Hardening

### Firewall Rules
```bash
# Allow only necessary ports
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw enable
```

### Regular Updates
```bash
# Keep system updated
sudo apt update && sudo apt upgrade -y

# Keep Java updated
sudo apt install -y openjdk-17-jdk-headless
```

### Database Security
```sql
-- Enforce strong passwords
ALTER ROLE portfolio_user WITH PASSWORD 'very_strong_password_here';

-- Limit connections
CREATE ROLE admin CREATEDB;
GRANT CONNECT ON DATABASE portfolio_db TO portfolio_user;
```

---

## Cost Optimization

### AWS
- Use t3.micro for low traffic (free tier eligible)
- RDS Multi-AZ only if critical
- Use CloudFront for static assets
- Setup auto-scaling groups

### DigitalOcean
- Basic $5/month droplet
- Managed database $15/month
- Backups are automatic

### Heroku
- Free tier: https://www.heroku.com/free (if still available)
- Paid: starting $50/month

---

## Rollback Procedure

### Docker
```bash
docker-compose down
docker-compose up -d --build --force-recreate
```

### Linux
```bash
# Keep backup of previous JAR
cp target/portfolio-app-1.0.0.jar target/portfolio-app-1.0.0.jar.backup

# Restore previous version
sudo systemctl stop portfolio-backend
cp target/portfolio-app-1.0.0.jar.backup target/portfolio-app-1.0.0.jar
sudo systemctl start portfolio-backend
```

---

**Recommended**: Docker deployment for simplicity and consistency across environments.
