package bsoft.nl.waardelijst.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


public class WaardeLijstEntry implements Serializable {
    static final long serialVersionUID=1235L;

    private static final Logger logger = LoggerFactory.getLogger(WaardeLijstEntry.class);

    private long code;

    private String waarde;

    private LocalDate vanAf;

    private LocalDate tot;

    private String toelichting;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getWaarde() {
        return waarde;
    }

    public void setWaarde(String waarde) {
        this.waarde = waarde;
    }

    public LocalDate getVanAf() {
        return vanAf;
    }

    public void setVanAf(LocalDate vanAf) {
        this.vanAf = vanAf;
    }

    public LocalDate getTot() {
        return tot;
    }

    public void setTot(LocalDate tot) {
        this.tot = tot;
    }

    public String getToelichting() {
        return toelichting;
    }

    public void setToelichting(String toelichting) {
        this.toelichting = toelichting;
    }
}
