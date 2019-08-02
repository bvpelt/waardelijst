package bsoft.nl.waardelijst.controllers;

import bsoft.nl.waardelijst.model.WaardeLijst;
import bsoft.nl.waardelijst.model.WaardeLijstEntry;
import bsoft.nl.waardelijst.services.WaardelijstService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class WaardeLijstController  {
    private static final Logger logger = LoggerFactory.getLogger(WaardeLijstController.class);

    @Value("${maxCacheTime}")
    private int maxAge;

    @Autowired
    private WaardelijstService waardelijstService;

    @GetMapping("/waardelijsten")
    public ResponseEntity<List<WaardeLijst>> retrieveWaardelijsten() {
        logger.info("Received request for waardelijsten");
        List<WaardeLijst> result = waardelijstService.retrieveWaardeLijsten();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(maxAge, TimeUnit.SECONDS).cachePublic())
                .body(result);
    }

    @GetMapping("/waardelijst/{waardeLijstNaam}")
    public ResponseEntity<List<WaardeLijstEntry>> retrieveWaardeLijstEntries(@PathVariable String waardeLijstNaam) {
        logger.info("Received request for waardelijst {}", waardeLijstNaam);
        List<WaardeLijstEntry> result = waardelijstService.retrieveWaardeLijstEntries(waardeLijstNaam);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(maxAge, TimeUnit.SECONDS).cachePublic())
                .body(result);
    }

    @GetMapping("/waardelijst/{waardeLijstNaam}/{waardeLijstCode}")
    public ResponseEntity<List<WaardeLijstEntry>> retrieveWaardeLijstEntries(@PathVariable String waardeLijstNaam, @PathVariable Long waardeLijstCode) {
        logger.info("Received request for waardelijst {} code {}", waardeLijstNaam, waardeLijstCode);
        List<WaardeLijstEntry> result = waardelijstService.retrieveWaardeLijstEntries(waardeLijstNaam, waardeLijstCode);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(maxAge, TimeUnit.SECONDS).cachePublic())
                .body(result);
    }

    /**
     * Get a single value
     * @param waardeLijstNaam
     * @param waardeLijstCode
     * @param date format 'yyyy-mm-dd'
     * @return
     */
    @GetMapping("/waardelijst/{waardeLijstNaam}/{waardeLijstCode}/{date}")
    public ResponseEntity<WaardeLijstEntry> retrieveWaardeLijstEntry(@PathVariable String waardeLijstNaam, @PathVariable Long waardeLijstCode, @PathVariable String date) {
        logger.info("Received request for waardelijst {} code {} date {}", waardeLijstNaam, waardeLijstCode, date);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyymmdd");
        final LocalDate checkDate = LocalDate.parse(date, dateTimeFormatter);

        WaardeLijstEntry result = waardelijstService.retrieveWaardeLijstEntrie(waardeLijstNaam, waardeLijstCode, checkDate);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(maxAge, TimeUnit.SECONDS).cachePublic())
                .body(result);
    }

}
