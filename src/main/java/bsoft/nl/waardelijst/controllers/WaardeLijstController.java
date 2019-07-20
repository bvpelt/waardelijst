package bsoft.nl.waardelijst.controllers;

import bsoft.nl.waardelijst.model.WaardeLijst;
import bsoft.nl.waardelijst.model.WaardeLijstEntry;
import bsoft.nl.waardelijst.services.WaardelijstService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WaardeLijstController  {
    private static final Logger logger = LoggerFactory.getLogger(WaardeLijstController.class);

    @Autowired
    WaardelijstService waardelijstService;


    @GetMapping("/waardelijst/{waardeLijstNaam}")
    public List<WaardeLijstEntry> retrieveWaardeLijstEntries(@PathVariable String waardeLijstNaam) {
        logger.info("Received request for waardelijst {}", waardeLijstNaam);
        return waardelijstService.retrieveWaardeLijstEntries(waardeLijstNaam);
    }

    @GetMapping("/waardelijst/{waardeLijstNaam}/{waardeLijstCode}")
    public WaardeLijstEntry retrieveWaardeLijstEntry(@PathVariable String waardeLijstNaam, @PathVariable Long waardeLijstCode) {
        logger.info("Received request for waardelijst {} code {}", waardeLijstNaam, waardeLijstCode);
        return waardelijstService.retrieveWaardeLijstEntrie(waardeLijstNaam, waardeLijstCode);
    }


    @GetMapping("/waardelijsten")
    public List<WaardeLijst> retrieveWaardelijsten() {
        logger.info("Received request for waardelijsten");
        return waardelijstService.retrieveWaardeLijsten();
    }
}
