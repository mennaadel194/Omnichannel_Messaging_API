package com.omnichannel.messagingplatform.controller;

import com.omnichannel.messagingplatform.dto.MessageRequest;
import com.omnichannel.messagingplatform.dto.MessageResponse;
import com.omnichannel.messagingplatform.security.JwtUtil;
import com.omnichannel.messagingplatform.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;
    private final JwtUtil jwtUtil;

    public MessageController(MessageService messageService, JwtUtil jwtUtil) {
        this.messageService = messageService;
        this.jwtUtil = jwtUtil;

    }

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(@RequestHeader("Authorization") String token, @Valid @RequestBody MessageRequest messageRequest) {
        MessageResponse response = messageService.sendMessage(jwtUtil.extractUserId(token), messageRequest);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Rate limit exceeded", "Failed"));
    }


    @GetMapping
    public ResponseEntity<List<MessageResponse>> getMessages(@RequestHeader("Authorization") String token, @RequestParam String channel) {
        List<MessageResponse> messages = messageService.getMessages(jwtUtil.extractUserId(token), channel);
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }
}
