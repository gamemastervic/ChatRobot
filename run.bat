@echo off
echo ===========================================
echo    ChatRobot - AI聊天机器人 (Java 8版本)
echo ===========================================
echo.

echo 正在检查Java环境...
java -version
if %errorlevel% neq 0 (
    echo 错误：未找到Java，请安装Java 8或更高版本
    pause
    exit /b 1
)

echo.
echo 正在检查Maven环境...
mvn -version 2>nul
if %errorlevel% neq 0 (
    echo Maven未安装，将尝试使用Maven Wrapper...
    if exist mvnw.cmd (
        echo 使用Maven Wrapper运行项目...
        mvnw.cmd spring-boot:run
    ) else (
        echo 请安装Maven或下载Maven Wrapper
        echo 下载地址: https://maven.apache.org/download.cgi
        pause
        exit /b 1
    )
) else (
    echo Maven已安装，使用Maven运行项目...
    mvn spring-boot:run
)

echo.
echo 应用启动完成，请在浏览器中访问: http://localhost:8080
pause 