package com.exemple.auth.user;

import com.exemple.auth.security.JWTService;
import com.exemple.auth.security.dtos.JwtAuthDto;
import com.exemple.auth.security.dtos.RefreshTokenDto;
import com.exemple.auth.security.dtos.UserCredentialsDto;
import com.exemple.auth.user.dtos.UserMapper;
import com.exemple.auth.user.dtos.UserRequestDto;
import com.exemple.auth.user.dtos.UserResponseDto;
import com.exemple.auth.user.exception.UserNotFoundByIdException;
import com.exemple.auth.user.exception.UserNotFoundByUsername;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        User user = findByCredentials(userCredentialsDto);

        return jwtService.generateAuthToken(user.getUsername());
    }

    public JwtAuthDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception{
        String refreshToken = refreshTokenDto.getRefreshToken();

        if(refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
            User user = findByUsername(jwtService.getUserNameFromToken(refreshToken));
            return jwtService.refreshBaseToken(user.getUsername(), refreshToken);
        }
        throw new AuthenticationException("Invalid refresh token.");
    }

    @Transactional
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundByIdException(id));
        return UserMapper.toDto(user);
    }

    @Transactional
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundByUsername(username));
        return UserMapper.toDto(user);
    }

    public UserResponseDto addUser(UserRequestDto userRequest){
        User user = UserMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    private User findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException{
        Optional<User> optionalUser = userRepository.findByUsername(userCredentialsDto.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())) {
                return  user;
            }
        }
        throw new AuthenticationException("Username or password is not correct!");
    }

    private  User findByUsername(String username) throws Exception{
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundByUsername(username));
    }
}

