package com.dvo.user_service.web.controller;

import com.dvo.user_service.aop.CheckAccessToUser;
import com.dvo.user_service.entity.User;
import com.dvo.user_service.mapper.UserMapper;
import com.dvo.user_service.service.UserService;
import com.dvo.user_service.web.model.filter.UserFilter;
import com.dvo.user_service.web.model.request.UpdateUserRequest;
import com.dvo.user_service.web.model.request.UpsertUserRequest;
import com.dvo.user_service.web.model.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all users by filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserResponse>> findAllByFilter(@Valid UserFilter filter) {
        List<User> users = userService.findAllByFilter(filter);
        return ResponseEntity.ok(users
                .stream()
                .map(userMapper::userToResponse)
                .toList()
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user by ID")
    @CheckAccessToUser
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                userMapper.userToResponse(
                        userService.findById(id)
                )
        );
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user")
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UpsertUserRequest request) {
        return ResponseEntity.ok(
                userMapper.userToResponse(
                        userService.save(
                                userMapper.requestToUser(request)
                        )
                )
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user by ID")
    @CheckAccessToUser
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @RequestBody @Valid UpdateUserRequest request) {
        return ResponseEntity.ok(
                userMapper.userToResponse(
                        userService.update(id, request)
                )
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user by ID")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/telegramid")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get telegram id by user ID")
    public ResponseEntity<String> getTelegramId(@PathVariable Long id) {
        return ResponseEntity.ok(
                userService.findById(id).getTelegramId()
        );
    }
}
