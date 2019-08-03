package bsoft.nl.waardelijst.model;

import java.io.Serializable;

public class WaardeLijst implements Serializable {
    static final long serialVersionUID=1234L;

    private long id;

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
