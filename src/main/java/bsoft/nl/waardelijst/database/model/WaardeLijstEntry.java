package bsoft.nl.waardelijst.database.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "WAARDELIJSTENTRY")
@Table(name = "WAARDELIJSTENTRY")
public class WaardeLijstEntry {
    private static final Logger logger = LoggerFactory.getLogger(WaardeLijstEntry.class);

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "WAARDELIJSTID")
    private long waardeLijstId;

    @Column(name = "CODED")
    private long code;

    @Column(name = "WAARDE")
    private String waarde;

    @Column(name = "VANAF")
    private LocalDate vanAf;

    @Column(name = "TOT")
    private LocalDate tot;

    @Column(name = "TOELICHTING")
    private String toelichting;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWaardeLijstId() {
        return waardeLijstId;
    }

    public void setWaardeLijstId(long waardeLijstId) {
        this.waardeLijstId = waardeLijstId;
    }

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
