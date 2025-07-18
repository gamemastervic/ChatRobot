@echo off
chcp 65001 >nul 2>&1
echo ==========================================
echo        ChatRobot Clean Build & Run
echo ==========================================
echo.

echo [1/4] Stopping any running instances...
taskkill /f /im java.exe 2>nul >nul

echo [2/4] Cleaning project...
if exist target rmdir /s /q target
echo Target directory cleaned.

echo [3/4] Compiling project...
mvn clean compile -q
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo [4/4] Starting application...
echo.
echo ==========================================
echo   Application starting on port 9090
echo   Visit: http://localhost:9090
echo ==========================================
echo.

mvn spring-boot:run 