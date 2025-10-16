package modules.auth.service;

import org.springframework.http.ResponseEntity;

import modules.auth.dto.request.LoginRequest;
import modules.auth.dto.request.RegisterRequest;
import modules.auth.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    ResponseEntity<?> register(RegisterRequest registerRequest);
}
