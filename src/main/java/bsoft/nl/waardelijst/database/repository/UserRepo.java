package bsoft.nl.waardelijst.database.repository;

import bsoft.nl.waardelijst.database.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
