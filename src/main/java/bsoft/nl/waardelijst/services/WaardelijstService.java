package bsoft.nl.waardelijst.services;


import bsoft.nl.waardelijst.database.repository.WaardeLijstEntryRepo;
import bsoft.nl.waardelijst.database.repository.WaardeLijstRepo;
import bsoft.nl.waardelijst.model.WaardeLijst;
import bsoft.nl.waardelijst.model.WaardeLijstEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    /**
     * Return all entries for a specified waardelijstNaam
     * @param waardeLijstNaam
     * @return List of known entries for this waardeLijst (0..n) entries
     */
    @Cacheable(cacheNames = "allEntries", key = "#waardeLijstNaam")
    public List<WaardeLijstEntry> retrieveWaardeLijstEntries(final String waardeLijstNaam) {
        List<WaardeLijstEntry> waardeLijstEntries = new ArrayList<WaardeLijstEntry>();

        List<bsoft.nl.waardelijst.database.model.WaardeLijst> waardeLijst = waardeLijstRepo.findByName(waardeLijstNaam);
        List<bsoft.nl.waardelijst.database.model.WaardeLijstEntry> waardeLijstEntryDatabase = null;

        List<bsoft.nl.waardelijst.database.model.WaardeLijst> waardeLijstEntriesDatabase = waardeLijstRepo.findByName(waardeLijstNaam);

        if (waardeLijst != null) { // waardelijst onbekend
            if (waardeLijst.size() == 1) {
                waardeLijstEntryDatabase = waardeLijstEntryRepo.findByWaardeLijstIdOrderByCodeAsc(waardeLijst.get(0).getId());
                getWaardelijstEntries(waardeLijstNaam, waardeLijstEntries, waardeLijstEntryDatabase);
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

    /**
     * Return all known waardelistentries for a specified waardelijst with a specific waardelijstCode
     * @param waardeLijstNaam
     * @param waardeLijstCode
     * @return  List of known entries for this waardeLijst (0..n) entries
     * Mostly there will be only one entry for a waardeLijstNaam + waardeLijstCode,
     * but when history has been defined there will be more than one waardelijstentrie returned
     */
    @Cacheable(cacheNames = "allEntries", key = "{ #waardelijstNaam, #waardeLijstCode }")
    public List<WaardeLijstEntry> retrieveWaardeLijstEntries(final String waardeLijstNaam, final Long waardeLijstCode) {
        List<WaardeLijstEntry> waardeLijstEntries = new ArrayList<WaardeLijstEntry>();
        List<bsoft.nl.waardelijst.database.model.WaardeLijstEntry> waardeLijstEntryDatabase = null;

        List<bsoft.nl.waardelijst.database.model.WaardeLijst> waardeLijst = waardeLijstRepo.findByName(waardeLijstNaam);

        if (waardeLijst != null) { // found at least one
            if (waardeLijst.size() == 1) { // found exactly one as expected
                waardeLijstEntryDatabase = waardeLijstEntryRepo.findByWaardeLijstIdAndCodeOrderByCodeAsc(waardeLijst.get(0).getId(), waardeLijstCode);

                if (waardeLijstEntryDatabase != null) {
                    getWaardelijstEntries(waardeLijstNaam, waardeLijstEntries, waardeLijstEntryDatabase);
                }
            }
            else {
                logger.error("Waardelijst: {} {} keer gevonden, slechts 1 keer verwacht", waardeLijstNaam, waardeLijst.size());
                throw new WaardelijstNotFound("Waardelijst: " + waardeLijstNaam + " " + waardeLijst.size() + " keer gevonden, slechts 1 keer verwacht");
            }
        } else {
            logger.info("Waardelijst: {} niet gevonden", waardeLijstNaam);
            throw new WaardelijstNotFound("Waardelijst: " + waardeLijstNaam + " niet gevonden");
        }

        return waardeLijstEntries;
    }

    private void getWaardelijstEntries(String waardeLijstNaam, List<WaardeLijstEntry> waardeLijstEntries, List<bsoft.nl.waardelijst.database.model.WaardeLijstEntry> waardeLijstEntryDatabase) {
        if (waardeLijstEntryDatabase.size() > 0) {
            for (bsoft.nl.waardelijst.database.model.WaardeLijstEntry waardeLijstDatabase : waardeLijstEntryDatabase) {
                WaardeLijstEntry waardeLijstNew = convertFromDatabase(waardeLijstDatabase);
                waardeLijstEntries.add(waardeLijstNew);
            }
        } else {
            logger.info("Waardelijst: {} heeft nog geen inhoud", waardeLijstNaam);
            throw new WaardelijstNotFound("Waardelijst: " + waardeLijstNaam + " heeft nog geen inhoud");
        }
    }

    @Cacheable(cacheNames = "allEntries", key = "{ #waardeLijstNaam, #waardeLijstCode }")
    public WaardeLijstEntry retrieveWaardeLijstEntrie(final String waardeLijstNaam, final Long waardeLijstCode, final LocalDate peilDatum) {
        WaardeLijstEntry waardeLijstEntry = null;
        List<bsoft.nl.waardelijst.database.model.WaardeLijstEntry> waardeLijstEntryDatabase = null;

        List<bsoft.nl.waardelijst.database.model.WaardeLijst> waardeLijst = waardeLijstRepo.findByName(waardeLijstNaam);

        if (waardeLijst != null) { // found at least one
            if (waardeLijst.size() == 1) {
                waardeLijstEntryDatabase = waardeLijstEntryRepo.findByWaardeLijstIdAndCodeAndPeilDatum(waardeLijst.get(0).getId(), waardeLijstCode, peilDatum);

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

    /**
     * Return a list of all known waardelijsten
     * @return List of known waardelijsten (0..n)
     */
    @Cacheable(cacheNames = "allWaardelijsten" )
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

    @Cacheable(cacheNames = "allWaardelijsten", key ="#id" )
    public WaardeLijst retrieveOneWaardeLijsten(final Long id) {
        WaardeLijst waardeLijst = null;

        Optional<bsoft.nl.waardelijst.database.model.WaardeLijst> waardelijstDatabase = null;
        waardelijstDatabase = waardeLijstRepo.findById(id);

        if (waardelijstDatabase != null && waardelijstDatabase.isPresent()) {
            waardeLijst = convertFromDatabase(waardelijstDatabase.get());
        } else {
            throw new WaardelijstenNotFound("Waardelijsten met id: " + id + " niet gevonden");
        }

        return waardeLijst;
    }

    @Cacheable(cacheNames = "allWaardelijsten", key ="#name" )
    public List<WaardeLijst> retrieveNameWaardeLijsten(final String name) {
        List<WaardeLijst> waardeLijstList = new ArrayList<WaardeLijst>();

        Iterable<bsoft.nl.waardelijst.database.model.WaardeLijst> waardelijstDatabaseList = null;
        waardelijstDatabaseList = waardeLijstRepo.findByPartialName(name);

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

    @CacheEvict(cacheNames = "allWaardelijsten", allEntries = true)
    public WaardeLijst addWaardeLijst(WaardeLijst waardeLijst) {
        bsoft.nl.waardelijst.database.model.WaardeLijst waardeLijstDatabase = null;
        waardeLijstDatabase = convertToDatabase(waardeLijst);

        waardeLijstDatabase = waardeLijstRepo.save(waardeLijstDatabase);  // name is uniqe!!!

        WaardeLijst result = convertFromDatabase(waardeLijstDatabase);

        return result;
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "allWaardelijsten", allEntries = true),
            @CacheEvict(cacheNames = "allWaardelijsten", key="#waardeLijst.id"),
            @CacheEvict(cacheNames = "allWaardelijsten", key="#waardeLijst.name")
    })
    public WaardeLijst updateWaardeLijst(WaardeLijst waardeLijst) {
        bsoft.nl.waardelijst.database.model.WaardeLijst waardeLijstDatabase = null;
        waardeLijstDatabase = convertToDatabase(waardeLijst);

        waardeLijstDatabase = waardeLijstRepo.save(waardeLijstDatabase);  // name is uniqe!!!

        WaardeLijst result = convertFromDatabase(waardeLijstDatabase);

        return result;
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "allWaardelijsten", allEntries = true),
            @CacheEvict(cacheNames = "allWaardelijsten", key="#waardeLijst.id")
    })
    public void deleteWaardeLijst(Long id) {
        waardeLijstRepo.deleteById(id);
    }

    private bsoft.nl.waardelijst.database.model.WaardeLijst convertToDatabase(WaardeLijst waardeLijst) {
        bsoft.nl.waardelijst.database.model.WaardeLijst waardeLijstDatabase = new bsoft.nl.waardelijst.database.model.WaardeLijst();
        waardeLijstDatabase.setName(waardeLijst.getName());
        return waardeLijstDatabase;
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