package ECommerceFlowApi.auth.service;

import ECommerceFlowApi.auth.dto.request.LoginRequest;
import ECommerceFlowApi.auth.dto.request.RegisterRequest;
import ECommerceFlowApi.auth.dto.response.AuthResponse;
import ECommerceFlowApi.user.model.User;

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
