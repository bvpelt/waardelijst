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

    public List<WaardeLijstEntry> retrieveWaardeLijstEntries(final String waardeLijstNaam) {
        List<WaardeLijstEntry> waardeLijstEntries = new ArrayList<WaardeLijstEntry>();

        List<bsoft.nl.waardelijst.database.model.WaardeLijst> waardeLijst = waardeLijstRepo.findByName(waardeLijstNaam);
        List<bsoft.nl.waardelijst.database.model.WaardeLijstEntry> waardeLijstEntryDatabase = null;

        List<bsoft.nl.waardelijst.database.model.WaardeLijst> waardeLijstEntriesDatabase = waardeLijstRepo.findByName(waardeLijstNaam);

        if (waardeLijst != null) { // found at least one
            if (waardeLijst.size() == 1) {
                waardeLijstEntryDatabase = waardeLijstEntryRepo.findByWaardeLijstId(waardeLijst.get(0).getId());
                if (waardeLijstEntryDatabase.size() > 0) {
                    for (bsoft.nl.waardelijst.database.model.WaardeLijstEntry waardeLijstDatabase : waardeLijstEntryDatabase) {
                        WaardeLijstEntry waardeLijstNew = convertFromDatabase(waardeLijstDatabase);
                        waardeLijstEntries.add(waardeLijstNew);
                    }
                } else {
                    logger.info("Waardelijst: {} heeft nog geen inhoud", waardeLijstNaam);
                    throw new WaardelijstNotFound("Waardelijst: " + waardeLijstNaam + " heeft nog geen inhoud");
                }
            } else {
                logger.error("Waardelijst: {} {} keer gevonden, slechts 1 keer verwacht", waardeLijstNaam, waardeLijst.size());
                throw new WaardelijstNotFound("Waardelijst: " + waardeLijstNaam + " " + waardeLijst.size() + " keer gevonden, slechts 1 keer verwacht");
            }
        } else {
            logger.info("Waardelijst: {} niet gevonden", waardeLijstNaam);
            throw new WaardelijstNotFound("Waardelijst: " + waardeLijstNaam + " niet gevonden");
        }

        return waardeLijstEntries;
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
                        if (waardeLijstEntryDatabase.size() == 0) {
                            throw new WaardelijstEntryNotFound("Waardelijstcode: " + waardeLijstCode + " voor waardelijst: " + waardeLijstNaam + " niet gevonden");
                        } else {
                            throw new WaardelijstEntryNotFound("Waardelijstcode: " + waardeLijstCode + " voor waardelijst: " + waardeLijstNaam + " te veel entries");
                        }
                    }
                } else {
                    logger.info("Waardelijstcode: {} voor waardelijst: {} niet gevonden", waardeLijstCode, waardeLijstNaam);
                    throw new WaardelijstEntryNotFound("Waardelijstcode: " + waardeLijstCode + " voor waardelijst: " + waardeLijstNaam + " niet gevonden");
                }
            } else {
                logger.error("Waardelijst: {} {} keer gevonden, slechts 1 keer verwacht", waardeLijstNaam, waardeLijst.size());
                if (waardeLijst.size() == 0) {
                    throw new WaardelijstNotFound("Waardelijst: " + waardeLijstNaam + " niet gevonden");
                } else {
                    throw new WaardelijstNotFound("Waardelijst: " + waardeLijstNaam + " te veel entries");
                }
            }
        } else {
            logger.info("Waardelijst: {} niet gevonden", waardeLijstNaam);
            throw new WaardelijstNotFound("Waardelijst: " + waardeLijstNaam + " niet gevonden");
        }

        return waardeLijstEntry;
    }

    public List<WaardeLijst> retrieveWaardeLijsten() {
        List<WaardeLijst> waardeLijstList = new ArrayList<WaardeLijst>();

        Iterable<bsoft.nl.waardelijst.database.model.WaardeLijst> waardelijstDatabaseList = null;
        waardelijstDatabaseList = waardeLijstRepo.findAll();

        int count = 0;
        for (bsoft.nl.waardelijst.database.model.WaardeLijst waardeLijstDatabase : waardelijstDatabaseList) {
            WaardeLijst waardeLijst = null;

            waardeLijst = convertFromDatabase(waardeLijstDatabase);

            waardeLijstList.add(waardeLijst);
            count++;
        }

        if ((waardelijstDatabaseList == null) || (count == 0)) {
            throw new WaardelijstenNotFound("Waardelijsten niet gevonden");
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
