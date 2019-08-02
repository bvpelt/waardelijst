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
public class WaardeLijstEntryRepoCustomImpl  implements  WaardeLijstEntryRepoCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<WaardeLijstEntry> findByWaardeLijstIdAndCodeAndVanAf(Long waardeLijstId, Long code, LocalDate vanAf) {

        Query query = entityManager.createNativeQuery("SELECT we.* FROM WaardeLijstEntry as we " +
                "WHERE we.waardeLijstId = ? and we.code = ? and we.vanAf <= ? and we.tot is null", WaardeLijstEntry.class);
        query.setParameter(1, waardeLijstId);
        query.setParameter(2, code);
        query.setParameter(3, vanAf);
        List<WaardeLijstEntry> result = query.getResultList();
        return result;
    }
}