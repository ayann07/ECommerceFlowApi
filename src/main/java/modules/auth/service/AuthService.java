package modules.auth.service;

import modules.auth.dto.request.LoginRequest;
import modules.auth.dto.request.RegisterRequest;
import modules.auth.dto.response.AuthResponse;
import modules.user.model.User;

public interface AuthService {

    /**
     * Authenticates a user and returns a response containing the JWT.
     */
    AuthResponse login(LoginRequest loginRequest);

    /**
     * Registers a new user and returns the created user entity.
     */
    User register(RegisterRequest registerRequest);
}
