package bsoft.nl.waardelijst.database.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity(name = "USERS")
@Table(name = "USERS")
public class User implements Serializable  {
    static final long serialVersionUID=1236L;
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "AUTHDATA")
    private String authdata;

    @Column(name = "EMAIL")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(joinColumns = @JoinColumn(name = "USER_ID"),inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAuthdata() {
        return authdata;
    }

    public void setAuthdata(String authdata) {
        this.authdata = authdata;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id: ");
        sb.append(this.id);
        sb.append(" username: ");
        sb.append(username);
        sb.append(" password: ");
        sb.append(password);
        sb.append(" firstName: ");
        sb.append(firstName);
        sb.append(" lastName: ");
        sb.append(lastName);
        sb.append(" authdata: ");
        sb.append(authdata);
        sb.append(" email: ");
        sb.append(email);

        return sb.toString();
    }
}
