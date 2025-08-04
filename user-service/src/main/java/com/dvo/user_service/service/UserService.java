package com.dvo.user_service.service;

import com.dvo.user_service.entity.User;
import com.dvo.user_service.web.model.filter.UserFilter;
import com.dvo.user_service.web.model.request.UpdateUserRequest;

import java.util.List;

public interface UserService {
    List<User> findAllByFilter(UserFilter filter);

    User findById(Long id);

    User findByUsername(String username);

    User save(User user);

    User update(Long id, UpdateUserRequest request);

    void deleteById(Long id);

    String getTelegramId(Long id);
}
