package com.example.chatrobot.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 聊天请求DTO
 * 封装前端发送的聊天消息
 * 
 * @author AI Assistant
 * @version 1.0.0
 */
public class ChatRequest {

    @NotBlank(message = "消息内容不能为空")
    @Size(max = 1000, message = "消息长度不能超过1000个字符")
    private String message;

    public ChatRequest() {}

    public ChatRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatRequest{" +
                "message='" + message + '\'' +
                '}';
    }
} 