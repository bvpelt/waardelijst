package bsoft.nl.waardelijst.database.repository;

import bsoft.nl.waardelijst.database.model.User;
import bsoft.nl.waardelijst.database.model.WaardeLijst;
import bsoft.nl.waardelijst.database.model.WaardeLijstEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User, Long>  {
    User findByUsername(String username);
}
