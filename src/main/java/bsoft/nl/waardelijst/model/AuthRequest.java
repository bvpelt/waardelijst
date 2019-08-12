package bsoft.nl.waardelijst.model;

import java.io.Serializable;

public class AuthRequest implements Serializable {
    static final long serialVersionUID = 1239L;

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
