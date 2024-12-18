package com.omnichannel.messagingplatform.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MessageResponse {
    private Long id;
    private String channel;    // "email" or "sms"
    private String content;    // The content of the message
    private String status;     // "Sent", "Failed", etc.
    private LocalDateTime timestamp;

    public MessageResponse(String content, String sent) {
        this.content = content;
        this.status = sent;
    }

    public MessageResponse(Long id, String channel, String content, String status, LocalDateTime timestamp) {
        this.id = id;
        this.channel = channel;
        this.content = content;
        this.status = status;
        this.timestamp = timestamp;
    }
}
