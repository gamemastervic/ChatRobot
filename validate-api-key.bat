@echo off
chcp 65001 >nul 2>&1
echo ================================================
echo            API Key Validation Tool
echo ================================================
echo.

echo Testing current API key...
echo Key: sk-6ba1613d893f44799f16b19da7e7c67b
echo.

echo Sending test request to DashScope API...
curl -s -w "HTTP Status: %%{http_code}\n" ^
  -H "Authorization: Bearer sk-6ba1613d893f44799f16b19da7e7c67b" ^
  -H "Content-Type: application/json" ^
  -H "X-DashScope-SSE: disable" ^
  -X POST ^
  -d "{\"model\":\"qwen-turbo\",\"input\":{\"messages\":[{\"role\":\"user\",\"content\":\"test\"}]}}" ^
  https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation

echo.
echo ================================================
echo Status Code Meanings:
echo   200 - Success (API key is valid)
echo   401 - Invalid API key
echo   403 - Permission denied
echo   429 - Rate limit exceeded
echo   500 - Server error
echo ================================================
echo.
echo If you see 401, you need to:
echo 1. Go to https://dashscope.console.aliyun.com/
echo 2. Check your account status
echo 3. Create a new API key
echo 4. Update your configuration
echo ================================================
pause 