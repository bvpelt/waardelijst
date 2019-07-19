package bsoft.nl.waardelijst.database.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity(name = "WAARDELIJST")
@Table(name = "WAARDELIJST")
public class WaardeLijst {
    private static final Logger logger = LoggerFactory.getLogger(WaardeLijst.class);

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "NAME")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
