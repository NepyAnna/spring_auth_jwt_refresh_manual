package com.exemple.auth.user;

import com.exemple.auth.user.dtos.UserRequestDto;
import com.exemple.auth.user.dtos.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("auth/registration")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userDto) {
        UserResponseDto userResponseDto = userService.addUser(userDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public UserResponseDto getUserById(@PathVariable Long id)  {
        return userService.getUserById(id);
    }

    @GetMapping("/users/username/{username}")
    public UserResponseDto getUserByEmail(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }
}

