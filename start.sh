#!/bin/bash

# Portfolio App - Quick Start Script
# Usage: ./start.sh

set -e

echo "🚀 Starting Portfolio App..."

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo -e "${YELLOW}⚠️  Java not found. Please install Java 17+${NC}"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo -e "${YELLOW}⚠️  Maven not found. Please install Maven 3.6+${NC}"
    exit 1
fi

# Build backend
echo -e "${BLUE}📦 Building backend...${NC}"
cd backend
mvn clean install -DskipTests
cd ..

# Start backend
echo -e "${BLUE}🔧 Starting backend service...${NC}"
cd backend
mvn spring-boot:run &
BACKEND_PID=$!
cd ..

# Wait for backend to start
echo -e "${BLUE}⏳ Waiting for backend to start...${NC}"
sleep 5

# Check if backend is running
if ! kill -0 $BACKEND_PID 2>/dev/null; then
    echo -e "${YELLOW}⚠️  Backend failed to start${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Backend running on http://localhost:8080${NC}"

# Start frontend
echo -e "${BLUE}🌐 Starting frontend...${NC}"

# Check if Python is available for simple server
if command -v python3 &> /dev/null; then
    cd frontend
    python3 -m http.server 3000 &
    FRONTEND_PID=$!
    cd ..
    echo -e "${GREEN}✅ Frontend running on http://localhost:3000${NC}"
elif command -v python &> /dev/null; then
    cd frontend
    python -m SimpleHTTPServer 3000 &
    FRONTEND_PID=$!
    cd ..
    echo -e "${GREEN}✅ Frontend running on http://localhost:3000${NC}"
else
    echo -e "${YELLOW}⚠️  Python not found. Please open frontend/index.html manually in your browser${NC}"
fi

echo ""
echo -e "${GREEN}================================${NC}"
echo -e "${GREEN}Portfolio App Started!${NC}"
echo -e "${GREEN}================================${NC}"
echo ""
echo -e "Frontend:  ${BLUE}http://localhost:3000${NC}"
echo -e "Backend:   ${BLUE}http://localhost:8080${NC}"
echo -e "H2 Console: ${BLUE}http://localhost:8080/h2-console${NC}"
echo ""
echo "Press Ctrl+C to stop all services"
echo ""

# Keep script running
wait $BACKEND_PID

# Cleanup
kill $FRONTEND_PID 2>/dev/null || true
trap "kill $BACKEND_PID $FRONTEND_PID 2>/dev/null || true" EXIT
