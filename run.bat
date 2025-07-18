@echo off
chcp 65001 >nul 2>&1
echo ===========================================
echo    ChatRobot - AI Chat Bot (Java 8)
echo ===========================================
echo.

echo Checking Java environment...
java -version
if %errorlevel% neq 0 (
    echo Error: Java not found, please install Java 8+
    pause
    exit /b 1
)

echo.
echo Checking Maven environment...
mvn -version 2>nul
if %errorlevel% neq 0 (
    echo Maven not installed, trying Maven Wrapper...
    if exist mvnw.cmd (
        echo Starting project with Maven Wrapper...
        mvnw.cmd spring-boot:run
    ) else (
        echo Please install Maven or download Maven Wrapper
        echo Download: https://maven.apache.org/download.cgi
        pause
        exit /b 1
    )
) else (
    echo Maven found, starting project...
    mvn spring-boot:run
)

echo.
echo Application started successfully!
echo Visit: http://localhost:9090
pause 