@echo off
chcp 65001 >nul 2>&1
echo ====================================
echo   Testing Alibaba DashScope API
echo ====================================
echo.

echo [1/4] Testing network connectivity...
ping -n 1 dashscope.aliyuncs.com > nul
if %errorlevel% equ 0 (
    echo [OK] Network connection successful
) else (
    echo [ERROR] Cannot reach dashscope.aliyuncs.com
    echo Please check your network connection
    pause
    exit /b 1
)

echo.
echo [2/4] Testing HTTPS connectivity...
curl -s -I https://dashscope.aliyuncs.com > nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] HTTPS connection successful
) else (
    echo [WARNING] HTTPS test failed, but this might be normal
)

echo.
echo [3/4] Checking API key configuration...
if defined DASHSCOPE_API_KEY (
    echo [OK] Environment variable set
    if "%DASHSCOPE_API_KEY:~0,3%"=="sk-" (
        echo [OK] API key format looks correct
    ) else (
        echo [WARNING] API key format may be incorrect
    )
) else (
    echo [INFO] Using API key from application.yml
)

echo.
echo [4/4] Ready to test with your application!
echo.
echo Start your ChatRobot application and test:
echo 1. Run: mvn spring-boot:run
echo 2. Visit: http://localhost:9090
echo 3. Send a test message
echo.
echo ====================================
pause 