package com.omnichannel.messagingplatform.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    @NotEmpty(message = "Channel cannot be empty")
    private String channel;
    @NotEmpty(message = "Message content cannot be empty")
    private String content;
}
