package com.omnichannel.messagingplatform.repository;

import com.omnichannel.messagingplatform.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByUserIdAndChannel(Long userId, String channelId);
}
