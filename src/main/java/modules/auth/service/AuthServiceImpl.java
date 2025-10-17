package modules.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import exception.sub_exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import modules.auth.dto.request.LoginRequest;
import modules.auth.dto.request.RegisterRequest;
import modules.auth.dto.response.AuthResponse;
import modules.user.enums.Role;
import modules.user.model.User;
import modules.user.repository.UserRepository;
import security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        User user = (User) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(token);
    }

    @Override
    public User register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent())
            throw new BadRequestException("Email already exists");
        User user = User.builder().firstName(registerRequest.getFirstName()).lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail()).password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER).build();
        return userRepository.save(user);
    }

}
