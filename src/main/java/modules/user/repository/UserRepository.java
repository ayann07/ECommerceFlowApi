package modules.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import modules.user.model.UserModel;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    // A custom method to find a user by their unique email address.
    // Spring Data JPA automatically implements this method based on its name.

    // @param email The email to search for.
    // @return An Optional containing the found user, or an empty Optional if not
    // found.
    Optional<UserModel> findByEmail(String email);
}
