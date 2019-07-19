package bsoft.nl.waardelijst.database.repository;

import bsoft.nl.waardelijst.database.model.WaardeLijstEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaardeLijstEntryRepo extends CrudRepository<WaardeLijstEntry, Long> {

    List<WaardeLijstEntry> findByWaardeLijstIdAndCode(Long waardeLijstId, Long code);


}
