# API Documentation

## 📡 Complete API Reference

Base URL: `http://localhost:8080/api`

---

## 🔐 Authentication Endpoints

### POST /auth/signup
**Create a new user account**

**Request:**
```json
{
  "email": "user@example.com",
  "username": "john_doe",
  "password": "secure_password_123"
}
```

**Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "id": 1,
  "email": "user@example.com",
  "username": "john_doe"
}
```

**Error (400 Bad Request):**
```json
{
  "error": "Email already exists"
}
```

---

### POST /auth/login
**Authenticate and receive JWT token**

**Request:**
```json
{
  "email": "user@example.com",
  "password": "secure_password_123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "id": 1,
  "email": "user@example.com",
  "username": "john_doe"
}
```

**Error (400 Bad Request):**
```json
{
  "error": "Invalid credentials"
}
```

---

## 👥 Visitor Tracking Endpoints

### GET /visitors/count
**Get total number of unique visitors**

**Request:**
```
GET /api/visitors/count
```

**Response (200 OK):**
```json
{
  "totalVisitors": 1523
}
```

---

### POST /visitors/increment
**Register a new visitor (called automatically on page load)**

**Request:**
```
POST /api/visitors/increment
```

**Response (200 OK):**
```
(empty response, just status code)
```

---

## ⏰ Time on Earth Endpoint

### GET /time-on-earth
**Get time alive statistics**

**Request:**
```
GET /api/time-on-earth
```

**Response (200 OK):**
```json
{
  "birthTimestamp": 807907200000,
  "currentTimestamp": 1707253920000,
  "totalSeconds": 899346720,
  "totalMinutes": 14989112,
  "totalDays": 10409,
  "formattedTime": "28 years, 5 months, 22 days, 3 hours, 2 minutes, 0 seconds"
}
```

---

## 📁 Projects Endpoints

### GET /projects
**Retrieve all projects**

**Request:**
```
GET /api/projects
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "AI Portfolio Generator",
    "shortDescription": "Automated portfolio website builder using machine learning",
    "description": "A sophisticated tool that generates personalized portfolio websites...",
    "techStack": ["React", "Node.js", "Python", "TensorFlow", "PostgreSQL"],
    "githubUrl": "https://github.com/username/project",
    "liveUrl": "https://example.com",
    "imageUrl": "https://example.com/image.jpg",
    "order": 1
  },
  {
    "id": 2,
    "title": "Real-time Collaboration Platform",
    "shortDescription": "WebSocket-based document collaboration tool",
    "description": "A Google Docs-like collaboration platform...",
    "techStack": ["React", "Node.js", "WebSocket", "PostgreSQL", "Redis"],
    "githubUrl": "https://github.com/username/collab",
    "liveUrl": "https://collab.example.com",
    "imageUrl": "https://example.com/collab.jpg",
    "order": 2
  }
]
```

---

### GET /projects/{id}
**Retrieve a specific project**

**Request:**
```
GET /api/projects/1
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "AI Portfolio Generator",
  "shortDescription": "Automated portfolio website builder using machine learning",
  "description": "A sophisticated tool...",
  "techStack": ["React", "Node.js", "Python"],
  "githubUrl": "https://github.com/username/project",
  "liveUrl": "https://example.com",
  "imageUrl": "https://example.com/image.jpg",
  "order": 1
}
```

**Error (404 Not Found):**
```json
{
  "error": "Project not found"
}
```

---

### POST /projects
**Create a new project (Admin only)**

**Request:**
```json
{
  "title": "New Project",
  "shortDescription": "Short description",
  "description": "Long description",
  "techStack": ["React", "Node.js"],
  "githubUrl": "https://github.com/...",
  "liveUrl": "https://...",
  "imageUrl": "https://...",
  "order": 5
}
```

**Response (200 OK):**
```json
{
  "id": 5,
  "title": "New Project",
  ...
}
```

---

## 🎯 Skills Endpoints

### GET /skills
**Retrieve all skills**

**Request:**
```
GET /api/skills
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Java",
    "category": "Backend",
    "proficiency": 5,
    "order": 1
  },
  {
    "id": 2,
    "name": "Spring Boot",
    "category": "Backend",
    "proficiency": 5,
    "order": 2
  },
  {
    "id": 10,
    "name": "React",
    "category": "Frontend",
    "proficiency": 5,
    "order": 1
  }
]
```

---

### GET /skills/category/{category}
**Retrieve skills by category**

**Request:**
```
GET /api/skills/category/Backend
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Java",
    "category": "Backend",
    "proficiency": 5,
    "order": 1
  },
  {
    "id": 2,
    "name": "Spring Boot",
    "category": "Backend",
    "proficiency": 5,
    "order": 2
  },
  {
    "id": 5,
    "name": "PostgreSQL",
    "category": "Backend",
    "proficiency": 5,
    "order": 5
  }
]
```

**Valid Categories:**
- `Backend`
- `Frontend`
- `Infra`
- `AI`

---

## 💬 Contact Endpoints

### POST /contact
**Submit a contact message (Requires Authentication)**

**Request:**
```
POST /api/contact
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "subject": "Project Inquiry",
  "message": "I loved your work on the AI system! Would love to collaborate."
}
```

**Response (200 OK):**
```json
{
  "message": "Message sent successfully"
}
```

**Error (400 Bad Request):**
```json
{
  "error": "Invalid token"
}
```

**Error (401 Unauthorized):**
```json
{
  "error": "Authorization header missing"
}
```

---

## 🔑 Authentication Headers

For any endpoint requiring authentication, include:

```
Authorization: Bearer <your_jwt_token>
```

**Example:**
```
GET /api/contact
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

---

## 📊 Response Codes

| Code | Meaning |
|------|---------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 400 | Bad Request - Invalid input or validation error |
| 401 | Unauthorized - Authentication required or failed |
| 404 | Not Found - Resource doesn't exist |
| 500 | Internal Server Error - Server-side issue |

---

## 🧪 cURL Examples

### Get Visitor Count
```bash
curl -X GET http://localhost:8080/api/visitors/count
```

### Get All Projects
```bash
curl -X GET http://localhost:8080/api/projects
```

### Get Skills by Category
```bash
curl -X GET http://localhost:8080/api/skills/category/Backend
```

### Sign Up
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "username": "testuser",
    "password": "password123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Send Contact Message
```bash
curl -X POST http://localhost:8080/api/contact \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "subject": "Great work!",
    "message": "I really enjoyed your portfolio. Can we connect?"
  }'
```

---

## 🔄 Request/Response Flow

### Authentication Flow
```
1. User fills signup form
2. Frontend calls POST /auth/signup
3. Backend validates, hashes password, creates user
4. Backend generates JWT token
5. Frontend stores token in localStorage
6. Token sent with subsequent requests in Authorization header
```

### Data Flow
```
1. Frontend loads
2. Calls GET /visitors/count (shows visitor count)
3. Calls POST /visitors/increment (tracks this visit)
4. Calls GET /projects (loads project data)
5. Calls GET /skills (loads skills data)
6. Calls GET /time-on-earth (starts time counter)
7. User interactions fetch data on-demand
```

---

## 💾 Data Types

### User
```typescript
{
  id: number;
  email: string;
  username: string;
  roles: string[]; // ["ROLE_USER", "ROLE_ADMIN"]
  createdAt: ISO8601DateTime;
  updatedAt: ISO8601DateTime;
}
```

### Project
```typescript
{
  id: number;
  title: string;
  description: string;
  shortDescription: string;
  techStack: string[];
  githubUrl?: string;
  liveUrl?: string;
  imageUrl?: string;
  order: number;
  createdAt: ISO8601DateTime;
}
```

### Skill
```typescript
{
  id: number;
  name: string;
  category: "Backend" | "Frontend" | "Infra" | "AI";
  proficiency: 1 | 2 | 3 | 4 | 5;
  order: number;
}
```

### Visitor
```typescript
{
  id: number;
  ipAddress: string;
  userAgent: string;
  visitedAt: ISO8601DateTime;
}
```

### ContactMessage
```typescript
{
  id: number;
  userId: number;
  subject: string;
  message: string;
  isRead: boolean;
  createdAt: ISO8601DateTime;
}
```

---

## 🔍 Query Parameters

Currently, the API supports basic endpoints without extensive query parameters. 

**Future enhancements could include:**
```
GET /api/projects?sort=order&limit=5&offset=0
GET /api/skills?category=Backend&minProficiency=4
GET /api/contact?read=false&userId=1&sort=createdAt
```

---

## 📡 WebSocket (Optional Enhancement)

For real-time features, WebSockets can be added:

```javascript
// Example: Real-time visitor count updates
const socket = new WebSocket('ws://localhost:8080/ws/visitors');
socket.onmessage = (event) => {
  const count = JSON.parse(event.data);
  updateVisitorDisplay(count);
};
```

---

## 🔒 CORS Headers

The API includes proper CORS headers:

```
Access-Control-Allow-Origin: http://localhost:3000
Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
Access-Control-Allow-Headers: Content-Type, Authorization
Access-Control-Allow-Credentials: true
Access-Control-Max-Age: 3600
```

---

## 📋 Rate Limiting (Future)

Consider adding rate limiting:
```properties
# Limit: 100 requests per minute per IP
ratelimit.max-requests=100
ratelimit.window-size=60
```

---

**API Version**: 1.0.0
**Last Updated**: February 2026
**Status**: Production Ready ✅
