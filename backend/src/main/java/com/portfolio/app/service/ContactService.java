package com.portfolio.app.service;

import com.portfolio.app.entity.ContactMessage;
import com.portfolio.app.entity.User;
import com.portfolio.app.repository.ContactMessageRepository;
import com.portfolio.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Autowired
    private UserRepository userRepository;

    public ContactMessage sendMessage(Long userId, String subject, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ContactMessage contactMessage = ContactMessage.builder()
                .user(user)
                .subject(subject)
                .message(message)
                .isRead(false)
                .build();

        return contactMessageRepository.save(contactMessage);
    }

    public List<ContactMessage> getUserMessages(Long userId) {
        return contactMessageRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
