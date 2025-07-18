@echo off
chcp 65001 >nul 2>&1
echo ====================================
echo   DashScope API Key Validation
echo ====================================
echo.

set API_KEY=sk-6ba1613d893f44799f16b19da7e7c67b

echo [1/3] Testing API key format...
if "%API_KEY:~0,3%"=="sk-" (
    echo [OK] Key format is correct (starts with sk-)
    echo [OK] Key length: %API_KEY:~3,32%
) else (
    echo [ERROR] Invalid key format
    goto :end
)

echo.
echo [2/3] Testing API endpoint connectivity...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" ^
  -H "Authorization: Bearer %API_KEY%" ^
  -H "Content-Type: application/json" ^
  -H "X-DashScope-SSE: disable" ^
  -X POST ^
  -d "{\"model\":\"qwen-turbo\",\"input\":{\"messages\":[{\"role\":\"user\",\"content\":\"test\"}]}}" ^
  https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation

echo.
echo [3/3] Checking detailed response...
curl -s ^
  -H "Authorization: Bearer %API_KEY%" ^
  -H "Content-Type: application/json" ^
  -H "X-DashScope-SSE: disable" ^
  -X POST ^
  -d "{\"model\":\"qwen-turbo\",\"input\":{\"messages\":[{\"role\":\"user\",\"content\":\"Hello\"}]}}" ^
  https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation

:end
echo.
echo ====================================
echo If you see HTTP 401: API key invalid
echo If you see HTTP 403: Permission denied  
echo If you see HTTP 429: Rate limit exceeded
echo If you see HTTP 200: API key is working!
echo ====================================
pause 