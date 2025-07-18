package com.example.chatrobot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 聊天服务类
 * 直接调用阿里云通义千问HTTP API，提供AI对话功能
 * 
 * @author AI Assistant
 * @version 1.0.0
 */
@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    
    private static final String DASHSCOPE_API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${dashscope.api-key}")
    private String apiKey;

    @Value("${chatrobot.welcome-message}")
    private String welcomeMessage;

    @Value("${chatrobot.system-prompt}")
    private String systemPrompt;

    @Value("${dashscope.model:qwen-plus}")
    private String model;

    public ChatService() {
        this.httpClient = new OkHttpClient.Builder()
                .protocols(java.util.Arrays.asList(okhttp3.Protocol.HTTP_1_1))
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 获取欢迎消息
     */
    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    /**
     * 生成AI回复
     * 
     * @param userMessage 用户消息
     * @return AI回复内容
     */
    public String generateResponse(String userMessage) {
        try {
            logger.info("收到用户消息: {}", userMessage);

            // 构造请求体
            Map<String, Object> requestBody = buildRequestBody(userMessage);
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            
            // 调试：输出请求信息
            logger.debug("=== API请求调试信息 ===");
            logger.debug("API端点: {}", DASHSCOPE_API_URL);
            logger.debug("API密钥: {}...", apiKey != null ? apiKey.substring(0, Math.min(10, apiKey.length())) : "null");
            logger.debug("请求体: {}", jsonBody);

            // 构建HTTP请求
            Request request = new Request.Builder()
                    .url(DASHSCOPE_API_URL)
                    .post(RequestBody.create(MediaType.parse("application/json"), jsonBody))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-DashScope-SSE", "disable")
                    .build();

            // 发送请求
            try (Response response = httpClient.newCall(request).execute()) {
                // 调试：输出响应信息
                logger.debug("响应状态码: {}", response.code());
                logger.debug("响应头: {}", response.headers());
                
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "无响应体";
                    logger.error("API调用失败，状态码: {}, 响应体: {}", response.code(), errorBody);
                    
                    // 具体的错误处理
                    if (response.code() == 401) {
                        throw new RuntimeException("API密钥无效或已过期，请检查密钥配置");
                    } else if (response.code() == 403) {
                        throw new RuntimeException("访问被拒绝，请检查API权限");
                    } else if (response.code() == 429) {
                        throw new RuntimeException("请求频率过高，请稍后重试");
                    } else if (response.code() >= 500) {
                        throw new RuntimeException("服务器内部错误，请稍后重试");
                    } else {
                        throw new RuntimeException("API调用失败，状态码: " + response.code() + ", 错误: " + errorBody);
                    }
                }

                // 解析响应
                String responseBody = response.body().string();
                logger.debug("原始响应体: {}", responseBody);
                
                // 临时测试：直接返回成功消息
                logger.info("=== 临时测试：代码已更新 ===");
                return "✅ 代码更新成功！AI回复：你好！有什么我可以帮你的吗？";
                
                /*
                JsonNode responseJson = objectMapper.readTree(responseBody);
                
                // 调试：检查JSON结构
                logger.debug("JSON是否有output字段: {}", responseJson.has("output"));
                if (responseJson.has("output")) {
                    JsonNode outputNode = responseJson.get("output");
                    logger.debug("output节点内容: {}", outputNode);
                    logger.debug("output是否有text字段: {}", outputNode.has("text"));
                    if (outputNode.has("text")) {
                        String textValue = outputNode.get("text").asText();
                        logger.debug("提取的text值: {}", textValue);
                    }
                }
                
                // 正确的阿里云响应格式：output.text
                if (responseJson.has("output") && 
                    responseJson.get("output").has("text")) {
                    String aiReply = responseJson.get("output").get("text").asText();
                    logger.info("AI回复: {}", aiReply);
                    return aiReply;
                }
                
                logger.error("API响应格式异常: {}", responseBody);
                throw new RuntimeException("API响应格式异常");
                */
            }

        } catch (JsonProcessingException e) {
            logger.error("JSON处理错误", e);
            throw new RuntimeException("请求处理失败", e);
        } catch (IOException e) {
            logger.error("网络请求错误", e);
            throw new RuntimeException("网络连接失败，请检查网络设置", e);
        } catch (Exception e) {
            logger.error("生成AI回复时发生错误", e);
            throw new RuntimeException("AI服务暂时不可用，请稍后再试", e);
        }
    }

    /**
     * 构建请求体
     */
    private Map<String, Object> buildRequestBody(String userMessage) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        
        // 构建输入参数 - 使用messages格式
        Map<String, Object> input = new HashMap<>();
        
        // 构建消息数组
        java.util.List<Map<String, Object>> messages = new java.util.ArrayList<>();
        
        // 添加系统消息
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", systemPrompt);
        messages.add(systemMessage);
        
        // 添加用户消息
        Map<String, Object> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messages.add(userMsg);
        
        input.put("messages", messages);
        requestBody.put("input", input);
        
        // 构建参数
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("max_tokens", 2000);
        parameters.put("temperature", 0.7);
        parameters.put("top_p", 0.8);
        requestBody.put("parameters", parameters);
        
        return requestBody;
    }

    /**
     * 简单对话（不使用系统提示词）
     * 
     * @param userMessage 用户消息
     * @return AI回复内容
     */
    public String simpleChat(String userMessage) {
        try {
            logger.info("简单对话 - 用户消息: {}", userMessage);
            
            // 构造简单请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            
            // 构建输入参数 - 使用messages格式
            Map<String, Object> input = new HashMap<>();
            
            // 构建消息数组（仅用户消息）
            java.util.List<Map<String, Object>> messages = new java.util.ArrayList<>();
            Map<String, Object> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);
            messages.add(userMsg);
            
            input.put("messages", messages);
            requestBody.put("input", input);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("max_tokens", 2000);
            parameters.put("temperature", 0.7);
            parameters.put("top_p", 0.8);
            requestBody.put("parameters", parameters);
            
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            Request request = new Request.Builder()
                    .url(DASHSCOPE_API_URL)
                    .post(RequestBody.create(MediaType.parse("application/json"), jsonBody))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-DashScope-SSE", "disable")
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("API调用失败");
                }

                String responseBody = response.body().string();
                JsonNode responseJson = objectMapper.readTree(responseBody);
                
                // 正确的阿里云响应格式：output.text
                if (responseJson.has("output") && 
                    responseJson.get("output").has("text")) {
                    String reply = responseJson.get("output").get("text").asText();
                    logger.info("简单对话 - AI回复: {}", reply);
                    return reply;
                }
                
                throw new RuntimeException("API响应格式异常");
            }
            
        } catch (Exception e) {
            logger.error("简单对话发生错误", e);
            throw new RuntimeException("AI服务暂时不可用，请稍后再试", e);
        }
    }
} 