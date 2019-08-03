package bsoft.nl.waardelijst.database.repository;

import bsoft.nl.waardelijst.database.model.WaardeLijstEntry;

import java.time.LocalDate;
import java.util.List;

public interface WaardeLijstEntryRepoCustom {
    List<WaardeLijstEntry> findByWaardeLijstIdAndCodeAndPeilDatum(Long waardeLijstId, Long code, LocalDate peilDatum );
}
