package com.omnichannel.messagingplatform.service;

import com.omnichannel.messagingplatform.dto.MessageRequest;
import com.omnichannel.messagingplatform.dto.MessageResponse;
import com.omnichannel.messagingplatform.exception.RateLimitExceededException;
import com.omnichannel.messagingplatform.model.Message;
import com.omnichannel.messagingplatform.model.User;
import com.omnichannel.messagingplatform.repository.MessageRepository;
import com.omnichannel.messagingplatform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RateLimiterService rateLimiterService;


    public MessageService(MessageRepository messageRepository, UserRepository userRepository, RateLimiterService rateLimiterService) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.rateLimiterService = rateLimiterService;
    }

    public MessageResponse sendMessage(Long userId, MessageRequest request) {
        // Rate limiting logic, checking the number of messages sent in the last minute
        if (rateLimiterService.canSendMessage(userId)) {
            throw new RateLimitExceededException("Rate limit exceeded, please try again later.");
        }
        // Create and save the message
        Message message = new Message();
        message.setUser(new User(userId));
        message.setChannel(request.getChannel());
        message.setContent(request.getContent());
        message.setStatus("Sent");
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);

        return new MessageResponse("Message Sent", "Sent");
    }


    public List<MessageResponse> getMessages(Long userId, String channel) {
        Optional<User> user=userRepository.findById(userId);
       if (user.isPresent()) {
           return messageRepository.findAllByUserIdAndChannel(1L, channel).stream().map(message -> new MessageResponse(message.getId(),
                   message.getChannel(),
                   message.getContent(),
                   message.getStatus(),
                   message.getTimestamp())).collect(Collectors.toList());
       }else {
           return new ArrayList<>();
       }
    }
}