package bsoft.nl.waardelijst.database.repository;

import bsoft.nl.waardelijst.database.model.WaardeLijst;
import bsoft.nl.waardelijst.database.model.WaardeLijstEntry;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class WaardeLijstRepoCustomImpl implements  WaardeLijstRepoCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<WaardeLijst> findByPartialName(String name) {
        Query query = entityManager.createNativeQuery("SELECT we.* FROM WaardeLijst as we " +
                "WHERE LOWER(we.name) like ?", WaardeLijst.class);
        query.setParameter(1, name.toLowerCase() + '%');
        List<WaardeLijst> result = query.getResultList();
        return result;
    }
}
