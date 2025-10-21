package ECommerceFlowApi.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ECommerceFlowApi.common.BaseModel;
import ECommerceFlowApi.user.enums.Role;

import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
// Generates all the boilerplate code: getters, setters, toString(), equals(),
// and hashCode().
@Builder
// Implements the Builder design pattern for easy object creation.
@NoArgsConstructor
// Generates a constructor with no arguments. Required by JPA.
@AllArgsConstructor
// Generates a constructor with all arguments.
@Entity
// JPA annotation : Marks this class as a database entity that will be mapped to
// a table.
@Table(name = "app_user")
// Specifies the actual table name in the database. "app_user" is used to avoid
// conflicts with the reserved SQL keyword "user".
public class User extends BaseModel implements UserDetails {
    // By implementing UserDetails, this class can be used directly by Spring
    // Security.

    @Id
    // Marks this field as the primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Configures the primary key generation strategy to be auto-incrementing.
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    // Specifies that this enum should be stored as a String (e.g., "USER", "ADMIN")
    // in the database.
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    // --- UserDetails Interface Methods ---

    /**
     * Returns the authorities (roles) granted to the user.
     * Spring Security uses this for authorization.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * Returns the user's password.
     * Spring Security uses this to verify the user's credentials during
     * authentication.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username used to authenticate the user.
     * In our case, we are using the email as the unique identifier.
     */
    @Override
    public String getUsername() {
        return email;
    }

}