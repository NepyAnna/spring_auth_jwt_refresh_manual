package com.exemple.auth.user.dtos;

import com.exemple.auth.user.User;

public class UserMapper {
    public static UserResponseDto  toDto(User user) {
        return new UserResponseDto(user.getId(),user.getUsername(),user.getEmail());
    }

    public static User toEntity(UserRequestDto userRequest) {
        User user = new User();
        user.setUsername(userRequest.username());
        user.setEmail(userRequest.email());
        return user;
    }
}