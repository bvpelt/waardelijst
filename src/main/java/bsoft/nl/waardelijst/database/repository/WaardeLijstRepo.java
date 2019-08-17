package bsoft.nl.waardelijst.database.repository;

import bsoft.nl.waardelijst.database.model.WaardeLijst;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WaardeLijstRepo extends CrudRepository<WaardeLijst, Long>, WaardeLijstRepoCustom {

    List<WaardeLijst> findByName(String waardeLijstNaam);
}
