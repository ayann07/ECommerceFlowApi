package ECommerceFlowApi.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ECommerceFlowApi.auth.dto.request.LoginRequest;
import ECommerceFlowApi.auth.dto.request.RegisterRequest;
import ECommerceFlowApi.auth.dto.response.AuthResponse;
import ECommerceFlowApi.exception.sub_exceptions.BadRequestException;
import ECommerceFlowApi.security.JwtTokenProvider;
import ECommerceFlowApi.user.enums.Role;
import ECommerceFlowApi.user.model.User;
import ECommerceFlowApi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

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
