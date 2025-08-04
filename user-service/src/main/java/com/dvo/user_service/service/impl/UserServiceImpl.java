package com.dvo.user_service.service.impl;

import com.dvo.user_service.entity.User;
import com.dvo.user_service.event.UserEvent;
import com.dvo.user_service.exception.EntityExistsException;
import com.dvo.user_service.exception.EntityNotFoundException;
import com.dvo.user_service.mapper.UserMapper;
import com.dvo.user_service.repository.UserRepository;
import com.dvo.user_service.service.UserService;
import com.dvo.user_service.web.model.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<User> findAll() {
        log.info("Call findAll in UserServiceImpl");
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        log.info("Call findById in UserServiceImpl with ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID: " + id + " not found"));
    }

    @Override
    public User findByUsername(String username) {
        log.info("Call findByUsername in UserServiceImpl with username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username: " + username + " not found"));
    }

    @Override
    @Transactional
    public User save(User user) {
        log.info("Call save in UserServiceImpl with user: {}", user);

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new EntityExistsException("User with username: " + user.getUsername() + " already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EntityExistsException("User with email: " + user.getEmail() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);

        UserEvent event = UserEvent.builder()
                .userId(newUser.getId())
                .telegramId(newUser.getTelegramId())
                .action("create")
                .build();

        kafkaTemplate.send("user-topic", event);
        log.info("Send kafka topic with register event: {}", event);

        return newUser;
    }

    @Override
    @Transactional
    public User update(Long id, UpdateUserRequest request) {
        log.info("Call update in UserServiceImpl for ID: {}, with user request: {}", id, request);

        User existedUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID: " + id + " not found"));

        if (request.getUsername() != null && !request.getUsername().equals(existedUser.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            throw new EntityExistsException("User with username: " + request.getUsername() + " already exists");
        }

        if (request.getEmail() != null && !request.getEmail().equals(existedUser.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("User with email: " + request.getEmail() + " already exists");
        }

        userMapper.updateRequestToUser(request, existedUser);
        existedUser.setPassword(passwordEncoder.encode(existedUser.getPassword()));
        existedUser.setUpdatedAt(LocalDateTime.now());

        UserEvent event = UserEvent.builder()
                .userId(existedUser.getId())
                .telegramId(existedUser.getTelegramId())
                .action("update")
                .build();

        kafkaTemplate.send("user-topic", event);
        log.info("Send kafka topic with update event: {}", event);

        return userRepository.save(existedUser);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Call deleteById in UserServiceImpl with ID: {}", id);

        UserEvent event = UserEvent.builder()
                .userId(id)
                .action("delete")
                .build();
        kafkaTemplate.send("user-topic", event);
        log.info("Send kafka topic with delete event: {}", event);

        userRepository.deleteById(id);
    }

    @Override
    public String getTelegramId(Long id) {
        log.info("Call getTelegramId in UserServiceImpl with userId: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID: " + id + " not found"));
        return user.getTelegramId();
    }
}
