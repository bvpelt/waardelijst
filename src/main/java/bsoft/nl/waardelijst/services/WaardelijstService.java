package bsoft.nl.waardelijst.services;


import bsoft.nl.waardelijst.database.repository.WaardeLijstEntryRepo;
import bsoft.nl.waardelijst.database.repository.WaardeLijstRepo;
import bsoft.nl.waardelijst.model.WaardeLijst;
import bsoft.nl.waardelijst.model.WaardeLijstEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class WaardelijstService {
    private static final Logger logger = LoggerFactory.getLogger(WaardelijstService.class);

    private WaardeLijstEntryRepo waardeLijstEntryRepo = null;
    private WaardeLijstRepo waardeLijstRepo = null;

    @Autowired
    public WaardelijstService(WaardeLijstRepo waardeLijstRepo, WaardeLijstEntryRepo waardeLijstEntryRepo) {
        this.waardeLijstRepo = waardeLijstRepo;
        this.waardeLijstEntryRepo = waardeLijstEntryRepo;
    }

    public WaardeLijstEntry retrieveWaardeLijstEntrie(final String waardeLijstNaam, final Long waardeLijstCode) {
        WaardeLijstEntry waardeLijstEntry = null;
        List<bsoft.nl.waardelijst.database.model.WaardeLijstEntry> waardeLijstEntryDatabase = null;

        List<bsoft.nl.waardelijst.database.model.WaardeLijst> waardeLijst = waardeLijstRepo.findByName(waardeLijstNaam);

        if (waardeLijst != null) { // found at least one
            if (waardeLijst.size() == 1) {
                waardeLijstEntryDatabase = waardeLijstEntryRepo.findByWaardeLijstIdAndCode(waardeLijst.get(0).getId(), waardeLijstCode);

                if (waardeLijstEntryDatabase != null) {
                    if (waardeLijstEntryDatabase.size() == 1) {
                        waardeLijstEntry = convertFromDatabase(waardeLijstEntryDatabase.get(0));
                    } else {
                        logger.error("Waardelijstcode: {} voor waardelijst: {} {} keer gevonden, slechts 1 keer verwacht", waardeLijstCode, waardeLijstNaam, waardeLijstEntryDatabase.size());
                    }
                } else {
                    logger.info("Waardelijstcode: {} voor waardelijst: {} niet gevonden", waardeLijstCode, waardeLijstNaam);
                }
            } else {
                logger.error("Waardelijst: {} {} keer gevonden, slechts 1 keer verwacht", waardeLijstNaam, waardeLijst.size());
            }
        } else {
            logger.info("Waardelijst: {} niet gevonden", waardeLijstNaam);
        }

        return waardeLijstEntry;
    }

    public List<WaardeLijst> retrieveWaardeLijsten() {
        List<WaardeLijst> waardeLijstList = new ArrayList<WaardeLijst>();

       Iterable<bsoft.nl.waardelijst.database.model.WaardeLijst> waardelijstDatabaseList = null;
        waardelijstDatabaseList = waardeLijstRepo.findAll();

        for (bsoft.nl.waardelijst.database.model.WaardeLijst waardeLijstDatabase: waardelijstDatabaseList) {
            WaardeLijst waardeLijst = null;

            waardeLijst = convertFromDatabase(waardeLijstDatabase);

            waardeLijstList.add(waardeLijst);
        }

        return waardeLijstList;
    }

    private WaardeLijstEntry convertFromDatabase(bsoft.nl.waardelijst.database.model.WaardeLijstEntry databaseEntry) {
        WaardeLijstEntry waardeLijstEntry = new WaardeLijstEntry();

        waardeLijstEntry.setCode(databaseEntry.getCode());
        waardeLijstEntry.setToelichting(databaseEntry.getToelichting());
        waardeLijstEntry.setTot(databaseEntry.getTot());
        waardeLijstEntry.setVanAf(databaseEntry.getVanAf());
        waardeLijstEntry.setWaarde(databaseEntry.getWaarde());

        return waardeLijstEntry;
    }

    private WaardeLijst convertFromDatabase(bsoft.nl.waardelijst.database.model.WaardeLijst databaseEntry) {
        WaardeLijst waardeLijst = new WaardeLijst();

        waardeLijst.setId(databaseEntry.getId());
        waardeLijst.setName(databaseEntry.getName());

        return waardeLijst;
    }
}
