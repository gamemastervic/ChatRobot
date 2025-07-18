package com.example.chatrobot.controller;

import com.example.chatrobot.dto.ChatRequest;
import com.example.chatrobot.dto.ChatResponse;
import com.example.chatrobot.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 聊天控制器
 * 处理用户的聊天请求和响应
 * 
 * @author AI Assistant
 * @version 1.0.0
 */
@Controller
@RequestMapping("/")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 首页 - 聊天界面
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("welcomeMessage", chatService.getWelcomeMessage());
        return "chat";
    }

    /**
     * 处理聊天消息 - REST API
     */
    @PostMapping("/api/chat")
    @ResponseBody
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        try {
            String response = chatService.generateResponse(request.getMessage());
            return ResponseEntity.ok(new ChatResponse(response, true));
        } catch (Exception e) {
            return ResponseEntity.ok(new ChatResponse("抱歉，我现在无法回答您的问题，请稍后再试。", false));
        }
    }

    /**
     * API连接测试接口
     */
    @GetMapping("/api/test")
    @ResponseBody
    public ResponseEntity<String> testApi() {
        try {
            String testResponse = chatService.simpleChat("测试连接");
            return ResponseEntity.ok("✅ API连接成功! AI回复: " + testResponse);
        } catch (Exception e) {
            return ResponseEntity.ok("❌ API连接失败: " + e.getMessage());
        }
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/api/health")
    @ResponseBody
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ChatRobot 运行正常");
    }
} 