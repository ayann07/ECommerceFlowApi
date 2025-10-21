package ECommerceFlowApi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ECommerceFlowApi.user.model.User;

@Service
public class AuthenticationService {

    public User getUserFromJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new UsernameNotFoundException("No authenticated user found");

        return (User) authentication.getPrincipal();
    }
}
