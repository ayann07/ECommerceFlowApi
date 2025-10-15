package config;

import lombok.RequiredArgsConstructor;
import security.JwtAuthenticationFilter;
import security.UserDetailsServiceImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
/**
 * This allows us to secure individual methods in our controllers, not just
 * URLs.
 * For example, you could say "@PreAuthorize("hasRole('ADMIN')")" on a method.
 */
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // These are the custom components our security system needs.
    // Spring will automatically provide them to us because they are beans.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Think of this as the main rulebook for your API's security.
     * It defines how to handle every request that comes into your application.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // We turn off CSRF protection. This is because our API is "stateless"
                // and doesn't use cookies, which is what CSRF attacks target.
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())

                // We tell Spring "don't remember anyone". Every request is treated as a new
                // stranger.
                // They must show their ticket (JWT) every single time. This is what "stateless"
                // means.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Finally, we build these rules into a usable object.
        return http.build();
    }

    /**
     * This creates the tool that scrambles passwords into a secure hash.
     * We use BCrypt, which is the gold standard for password hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This is our custom "ID checker". It connects two important pieces:
     * 1. The `UserDetailsService` (how to find a user in our database).
     * 2. The `PasswordEncoder` (how to check if their submitted password is
     * correct).
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * This is the main "manager" that handles the entire login process.
     * We need to make it available as a bean so our login page (AuthController) can
     * use it.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}