@echo off
chcp 65001 >nul 2>&1
echo ====================================
echo   ChatRobot API Key Configuration Check
echo ====================================
echo.

if defined DASHSCOPE_API_KEY (
    echo [OK] Environment variable set
    echo    Variable: DASHSCOPE_API_KEY
    echo    Key preview: %DASHSCOPE_API_KEY:~0,20%...
    if "%DASHSCOPE_API_KEY:~0,3%"=="sk-" (
        echo [OK] API key format looks correct (starts with sk-)
    ) else (
        echo [ERROR] API key format may be incorrect (should start with sk-)
    )
) else (
    echo [ERROR] Environment variable not set
    echo    Using default value from config: your-api-key-here
    echo    WARNING: This is a placeholder, replace with real API key
)

echo.
echo ====================================
echo If you don't have an API key yet:
echo 1. Visit: https://dashscope.console.aliyun.com/
echo 2. Enable Tongyi Qianwen service
echo 3. Get your API key
echo ====================================
pause 