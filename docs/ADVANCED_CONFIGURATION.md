# Advanced Configuration Guide

## 🔧 Application Properties - Complete Reference

Located at: `backend/src/main/resources/application.properties`

### Server Configuration
```properties
# Server port
server.port=8080

# Server servlet context path
server.servlet.context-path=/

# Server shutdown behavior (graceful or immediate)
server.shutdown=graceful
server.tomcat.threads.max=200
```

### Database Configuration

#### H2 (Development)
```properties
# H2 in-memory database
spring.datasource.url=jdbc:h2:mem:portfoliodb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=false
```

#### PostgreSQL (Production)
```properties
# PostgreSQL connection
spring.datasource.url=jdbc:postgresql://localhost:5432/portfolio_db
spring.datasource.username=portfolio_user
spring.datasource.password=your_secure_password
spring.datasource.driver-class-name=org.postgresql.Driver

# Connection pooling
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
```

### JPA/Hibernate Configuration
```properties
# Database dialect
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL10Dialect

# DDL auto strategies:
# validate   - validate schema without changes
# update     - update schema when needed
# create     - create schema (drop on shutdown)
# create-drop - create and drop schema on each run
spring.jpa.hibernate.ddl-auto=validate

# Show SQL
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Batch processing
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.jdbc.fetch_size=50
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Performance tuning
spring.jpa.properties.hibernate.dialect.storage_engine=innodb
spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true
```

### JWT Configuration
```properties
# JWT Secret (minimum 32 characters)
jwt.secret=your-256-bit-secret-key-generate-something-secure

# Token expiration in milliseconds
jwt.expiration=86400000

# Token refresh settings (if implementing refresh tokens)
jwt.refresh-expiration=604800000  # 7 days
```

### Logging Configuration
```properties
# Root logger level
logging.level.root=INFO

# Application specific logging
logging.level.com.portfolio.app=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Log file
logging.file.name=logs/portfolio.log
logging.file.max-size=10MB
logging.file.max-history=7
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

### Portfolio Custom Properties
```properties
# Your birth date for time counter
portfolio.birth-date=1995-07-15

# Portfolio display name
portfolio.name=Your Name

# Portfolio description
portfolio.description=Engineer | Builder | Thinker
```

### CORS Configuration
```properties
# CORS is configured in PortfolioApplication.java class
# But can be extended here if needed
cors.allowed-origins=http://localhost:3000,http://localhost:8080
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed-headers=*
cors.allow-credentials=true
cors.max-age=3600
```

---

## 🔒 Environment Variables (Production)

### Using Environment Variables Instead of Properties File

```bash
# Linux/Mac
export SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/portfolio_db
export SPRING_DATASOURCE_USERNAME=portfolio_user
export SPRING_DATASOURCE_PASSWORD=secure_password
export JWT_SECRET=your_jwt_secret_key
export PORTFOLIO_BIRTH_DATE=1995-07-15
export SERVER_PORT=8080

# Then run:
java -jar target/portfolio-app-1.0.0.jar
```

```powershell
# Windows PowerShell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://prod-db:5432/portfolio_db"
$env:SPRING_DATASOURCE_USERNAME = "portfolio_user"
$env:SPRING_DATASOURCE_PASSWORD = "secure_password"
$env:JWT_SECRET = "your_jwt_secret_key"
$env:PORTFOLIO_BIRTH_DATE = "1995-07-15"
$env:SERVER_PORT = "8080"

# Then run:
java -jar target/portfolio-app-1.0.0.jar
```

---

## 🚀 Profile-Specific Configurations

### Create Multiple Config Files

1. `application.properties` - Default (development)
2. `application-prod.properties` - Production
3. `application-test.properties` - Testing

### Activate Profile

```bash
# Via command line
java -jar app.jar --spring.profiles.active=prod

# Via environment variable
export SPRING_PROFILES_ACTIVE=prod

# Via application.properties
spring.profiles.active=prod
```

### Example: application-prod.properties
```properties
server.port=8080
spring.datasource.url=jdbc:postgresql://prod-db:5432/portfolio_db
spring.datasource.username=portfolio_user
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.root=WARN
logging.level.com.portfolio.app=INFO
jwt.secret=${JWT_SECRET}
```

---

## 🔐 Security Configuration

### Password Encoding (BCrypt)

The application uses BCrypt with Spring Security:
```java
// In SecurityConfig.java - Already configured
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

### JWT Secret Generation

Generate a secure JWT secret:

```bash
# Linux/Mac - Generate 32+ character random string
openssl rand -base64 32

# PowerShell
[System.Convert]::ToBase64String((1..32|ForEach-Object{Get-Random -Max 256})) | Cut -c1-32
```

### HTTPS/SSL Configuration

Add to `application.properties`:
```properties
# SSL Configuration
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=tomcat
server.port=8443
```

### CORS Security Headers

Modify `PortfolioApplication.java`:
```java
registry.addMapping("/api/**")
    .allowedOrigins("https://your-domain.com")
    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    .allowedHeaders("*")
    .allowCredentials(true)
    .maxAge(3600)
    .exposedHeaders("Authorization", "X-Total-Count");
```

---

## 📊 Database Performance Tuning

### Connection Pool Settings
```properties
# HikariCP settings (default connection pool)
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.auto-commit=true
```

### Query Optimization
```properties
# Batch inserts/updates
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true

# Fetch strategy
spring.jpa.properties.hibernate.jdbc.fetch_size=50

# Second-level caching
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=\
    org.hibernate.cache.jcache.JCacheRegionFactory
```

### Index Creation
```sql
-- Add indexes for frequently queried columns
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_visitor_ip ON visitors(ip_address);
CREATE INDEX idx_project_order ON projects(order);
CREATE INDEX idx_skill_category ON skills(category);
CREATE INDEX idx_contact_user ON contact_messages(user_id);
```

---

## 🔄 Database Migration (Optional)

### Add Flyway for Version Control

Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

Create `src/main/resources/db/migration/V1__Initial_Schema.sql` with schema.sql content.

### Or Add Liquibase

Alternative to Flyway with more features.

---

## 🌐 Multiple Instance Deployment

### Load Balancer Configuration

```properties
# Enable sticky sessions
server.session.persistence.enabled=true

# Disable JSESSIONID
server.servlet.session.tracking-modes=cookie

# Use Redis for distributed sessions
spring.session.store-type=redis
spring.redis.host=redis-host
spring.redis.port=6379
```

### Database Connection in Multi-Instance

- Use PostgreSQL (not H2) for shared state
- Ensure all instances use same database
- Use connection pooling for performance

---

## 📈 Monitoring & Metrics (Actuator)

Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Add to `application.properties`:
```properties
# Enable actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics,env
management.endpoint.health.show-details=when-authorized
management.metrics.export.prometheus.enabled=true

# Custom info
info.app.name=Portfolio Application
info.app.version=1.0.0
info.company=Your Company
```

Access metrics at: `http://localhost:8080/actuator/metrics`

---

## 🐛 Debugging Configuration

### Enable Remote Debugging

```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
    -jar target/portfolio-app-1.0.0.jar
```

### Detailed SQL Logging

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Slow Query Logging

```properties
spring.jpa.properties.hibernate.session.events.log.LOG_SLOW_QUERY=true
spring.jpa.properties.hibernate.session.events.log.LOG_SLOW_QUERY_THRESHOLD_MILLIS=1000
```

---

## 🔄 Graceful Shutdown

```properties
# Wait up to 30 seconds for active requests to complete
server.shutdown=graceful
spring.lifecycle.timeout-per-shutdown-phase=30s

# Custom shutdown endpoint
management.endpoints.web.exposure.include=shutdown
management.endpoint.shutdown.enabled=true
```

---

## 📦 Building for Production

### Create Production JAR

```bash
cd backend
mvn clean package -P production -DskipTests

# JAR will be at:
# target/portfolio-app-1.0.0.jar
```

### Run with Java Options

```bash
java -server \
    -Xmx512m \
    -Xms256m \
    -XX:+UseG1GC \
    -XX:MaxGCPauseMillis=200 \
    -jar portfolio-app-1.0.0.jar
```

---

## 🧪 Testing Configuration

### application-test.properties
```properties
# Use H2 for testing
spring.datasource.url=jdbc:h2:mem:test
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# Faster tests
spring.test.database.replace=any
```

---

**Last Updated**: February 2026
