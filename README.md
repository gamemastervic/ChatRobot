# ChatRobot - AI聊天机器人 (Java 8版本)

🤖 基于Spring Boot框架的智能聊天机器人，直接集成阿里云通义千问(Qwen)模型HTTP API，兼容Java 8。

## ✨ 特性

- 🚀 基于Spring Boot 2.7.18，兼容Java 8
- 🧠 直接调用阿里云通义千问(Qwen) HTTP API
- 💬 现代化的聊天界面，支持实时对话
- 📱 响应式设计，支持移动端访问
- ⚡ 高性能、低延迟的AI响应
- 🔧 简单易用的配置和部署

## 🏗️ 技术栈

- **后端框架**: Spring Boot 2.7.18
- **HTTP客户端**: OkHttp 4.9.3
- **AI模型**: 阿里云通义千问 (Qwen)
- **前端**: HTML5 + CSS3 + JavaScript
- **模板引擎**: Thymeleaf
- **构建工具**: Maven
- **Java版本**: JDK 8+

## 📦 项目结构

```
ChatRobot/
├── src/
│   └── main/
│       ├── java/com/example/chatrobot/
│       │   ├── ChatRobotApplication.java          # 主启动类
│       │   ├── controller/
│       │   │   └── ChatController.java           # 聊天控制器
│       │   ├── service/
│       │   │   └── ChatService.java              # 聊天服务（HTTP API调用）
│       │   └── dto/
│       │       ├── ChatRequest.java              # 请求DTO
│       │       └── ChatResponse.java             # 响应DTO
│       └── resources/
│           ├── application.yml                   # 应用配置
│           └── templates/
│               └── chat.html                     # 聊天界面
├── pom.xml                                       # Maven配置
└── README.md                                     # 项目说明
```

## 🚀 快速开始

### 前置条件

1. **JDK 8+**: 确保已安装JDK 8或更高版本
2. **Maven 3.6+**: 用于构建项目
3. **阿里云API Key**: 需要获取通义千问的API密钥

### 获取阿里云API Key

1. 访问[阿里云控制台](https://dashscope.console.aliyun.com/)
2. 开通通义千问服务
3. 获取API Key

### 安装部署

1. **检查Java版本**
```bash
java -version
# 确保显示Java 8或更高版本
```

2. **配置API Key**

方式一：设置环境变量（推荐）
```bash
# Windows
set DASHSCOPE_API_KEY=your-api-key-here

# Linux/macOS
export DASHSCOPE_API_KEY=your-api-key-here
```

方式二：修改配置文件
编辑 `src/main/resources/application.yml`：
```yaml
dashscope:
  api-key: your-api-key-here
```

3. **运行项目**

如果已安装Maven：
```bash
# 构建项目
mvn clean compile

# 运行应用
mvn spring-boot:run
```

如果没有Maven，可以使用Maven Wrapper：
```bash
# Windows
./mvnw.cmd spring-boot:run

# Linux/macOS
./mvnw spring-boot:run
```

4. **访问应用**
打开浏览器访问：http://localhost:8080

## 🎯 使用说明

### Web界面

1. 访问 http://localhost:8080 打开聊天界面
2. 在输入框中输入您的问题
3. 点击"发送"按钮或按回车键发送消息
4. AI将实时回复您的问题

### API接口

#### 发送聊天消息
```http
POST /api/chat
Content-Type: application/json

{
  "message": "你好，请介绍一下你自己"
}
```

响应：
```json
{
  "message": "你好！我是基于阿里通义千问模型的AI助手...",
  "success": true,
  "timestamp": 1635724800000
}
```

#### 健康检查
```http
GET /api/health
```

响应：
```text
ChatRobot 运行正常
```

## ⚙️ 配置说明

### application.yml 配置详解

```yaml
# 阿里云通义千问API配置
dashscope:
  api-key: ${DASHSCOPE_API_KEY:your-api-key-here}  # API密钥
  model: qwen-plus                                  # 模型选择

chatrobot:
  welcome-message: "欢迎消息"      # 自定义欢迎消息
  system-prompt: "系统提示词"       # 自定义系统提示词
```

### 模型选择

- **qwen-turbo**: 响应速度快，适合简单对话
- **qwen-plus**: 平衡性能和质量（推荐）
- **qwen-max**: 最高质量，适合复杂任务

## 🔧 开发指南

### 添加自定义功能

1. **创建新的服务类**
```java
@Service
public class CustomService {
    // 自定义业务逻辑
}
```

2. **扩展控制器**
```java
@RestController
public class CustomController {
    @PostMapping("/api/custom")
    public ResponseEntity<?> customEndpoint(@RequestBody CustomRequest request) {
        // 处理自定义请求
        return ResponseEntity.ok(response);
    }
}
```

### 自定义提示词

修改 `ChatService.java` 中的 `buildRequestBody` 方法：

```java
private Map<String, Object> buildRequestBody(String userMessage) {
    // ... 现有代码 ...
    input.put("prompt", "自定义提示词前缀 " + userMessage + " 自定义提示词后缀");
    // ... 现有代码 ...
}
```

## 🐛 常见问题

### Q: 启动时报错 "API Key not found"
**A**: 请确保正确设置了 `DASHSCOPE_API_KEY` 环境变量或在配置文件中配置了API密钥。

### Q: 编译时报错 "java.version"
**A**: 请确保使用Java 8或更高版本：
```bash
java -version
javac -version
```

### Q: Maven命令不存在
**A**: 
- 下载并安装Maven 3.6+
- 或使用项目自带的Maven Wrapper: `./mvnw` (Linux/macOS) 或 `./mvnw.cmd` (Windows)

### Q: AI回复很慢或不回复
**A**: 请检查：
1. 网络连接是否正常
2. API密钥是否正确
3. 是否有API调用次数限制

### Q: 前端页面显示异常
**A**: 请确保：
1. 端口8080未被其他程序占用
2. 浏览器支持现代CSS和JavaScript特性

### Q: 如何修改端口
**A**: 在 `application.yml` 中修改：
```yaml
server:
  port: 9090  # 修改为您希望的端口
```

## 💡 运行命令总结

```bash
# 检查Java版本
java -version

# 设置API Key (Windows)
set DASHSCOPE_API_KEY=your-api-key-here

# 设置API Key (Linux/macOS)
export DASHSCOPE_API_KEY=your-api-key-here

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run

# 访问应用
# 浏览器打开: http://localhost:8080
```

## 📄 许可证

本项目基于 MIT 许可证开源。

## 🤝 贡献

欢迎提交Issue和Pull Request来改进这个项目！

---

⭐ 如果这个项目对您有帮助，请给我们一个Star！
