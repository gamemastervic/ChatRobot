@echo off
chcp 65001 >nul 2>&1
cls
echo.
echo ==========================================
echo        ChatRobot Starting...
echo ==========================================
echo.
echo [1/3] Checking Java...
java -version

echo.
echo [2/3] Starting Spring Boot Application...
echo        Port: 9090
echo        URL:  http://localhost:9090
echo.

mvn spring-boot:run

echo.
echo [3/3] Application stopped.
pause 