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
        this.httpClient = new OkHttpClient();
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

            // 构建HTTP请求
            Request request = new Request.Builder()
                    .url(DASHSCOPE_API_URL)
                    .post(RequestBody.create(MediaType.parse("application/json"), jsonBody))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            // 发送请求
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    logger.error("API调用失败，状态码: {}", response.code());
                    throw new RuntimeException("API调用失败");
                }

                // 解析响应
                String responseBody = response.body().string();
                JsonNode responseJson = objectMapper.readTree(responseBody);
                
                if (responseJson.has("output") && 
                    responseJson.get("output").has("text")) {
                    String aiReply = responseJson.get("output").get("text").asText();
                    logger.info("AI回复: {}", aiReply);
                    return aiReply;
                } else {
                    logger.error("API响应格式异常: {}", responseBody);
                    throw new RuntimeException("API响应格式异常");
                }
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
        
        // 构建输入参数
        Map<String, Object> input = new HashMap<>();
        input.put("prompt", systemPrompt + "\n\n用户问题: " + userMessage + "\n\n请回答:");
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
            
            Map<String, Object> input = new HashMap<>();
            input.put("prompt", userMessage);
            requestBody.put("input", input);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("max_tokens", 2000);
            requestBody.put("parameters", parameters);
            
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            Request request = new Request.Builder()
                    .url(DASHSCOPE_API_URL)
                    .post(RequestBody.create(MediaType.parse("application/json"), jsonBody))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("API调用失败");
                }

                String responseBody = response.body().string();
                JsonNode responseJson = objectMapper.readTree(responseBody);
                
                if (responseJson.has("output") && 
                    responseJson.get("output").has("text")) {
                    String reply = responseJson.get("output").get("text").asText();
                    logger.info("简单对话 - AI回复: {}", reply);
                    return reply;
                } else {
                    throw new RuntimeException("API响应格式异常");
                }
            }
            
        } catch (Exception e) {
            logger.error("简单对话发生错误", e);
            throw new RuntimeException("AI服务暂时不可用，请稍后再试", e);
        }
    }
} 