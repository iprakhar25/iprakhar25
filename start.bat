@echo off
REM Portfolio App - Quick Start Script for Windows
REM Usage: start.bat

setlocal enabledelayedexpansion
cd /d "%~dp0"

echo.
echo ======================================
echo   Portfolio App - Quick Start
echo ======================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo Error: Java not found. Please install Java 17 or higher.
    echo Download from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if errorlevel 1 (
    echo Error: Maven not found. Please install Maven 3.6 or higher.
    echo Download from: https://maven.apache.org/
    pause
    exit /b 1
)

echo [*] Java and Maven found. Building backend...
echo.

cd backend
echo [*] Running: mvn clean install -DskipTests
call mvn clean install -DskipTests
if errorlevel 1 (
    echo Error: Maven build failed!
    pause
    exit /b 1
)
cd ..

echo.
echo [+] Backend built successfully!
echo [*] Starting backend on port 8080...
echo.

start "Portfolio Backend" cmd /k "cd backend && mvn spring-boot:run"
timeout /t 5 /nobreak

echo [*] Starting frontend server on port 3000...
echo.
echo Available at:
echo   Frontend:   http://localhost:3000
echo   Backend:    http://localhost:8080
echo   H2 Console: http://localhost:8080/h2-console
echo.
echo [!] Backend window will open in a new terminal.
echo.

cd frontend

REM Try Python 3
python --version >nul 2>&1
if errorlevel 0 (
    echo [*] Starting Python HTTP server on port 3000...
    python -m http.server 3000
) else (
    REM Try Python 2
    python -version >nul 2>&1
    if errorlevel 0 (
        echo [*] Starting Python HTTP server on port 3000...
        python -m SimpleHTTPServer 3000
    ) else (
        echo.
        echo Error: Python not found!
        echo Please either:
        echo   1. Install Python from https://www.python.org/
        echo   2. Open frontend/index.html directly in your browser
        echo.
        pause
        exit /b 1
    )
)
