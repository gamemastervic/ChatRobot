package com.example.chatrobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ChatRobot 应用启动类
 * 基于Spring AI框架的聊天机器人，集成阿里Qwen模型
 * 
 * @author AI Assistant
 * @version 1.0.0
 */
@SpringBootApplication
public class ChatRobotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatRobotApplication.class, args);
        System.out.println("🤖 ChatRobot 聊天机器人启动成功！");
        System.out.println("🌐 访问地址: http://localhost:9090");
        System.out.println("📝 请确保已配置 DASHSCOPE_API_KEY 环境变量");
    }
} 