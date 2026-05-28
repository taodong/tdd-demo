package com.example.auth.mcp;

import com.example.auth.dto.UserResponse;
import com.example.auth.entity.User;
import com.example.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class McpToolService {

    private final UserService userService;

    public UserResponse register(String name, String email, String password) {
        User user = userService.register(name, email, password);
        return new UserResponse(user.getName(), user.getEmail());
    }

    public UserResponse login(String email, String password) {
        User user = userService.login(email, password);
        return new UserResponse(user.getName(), user.getEmail());
    }
}
