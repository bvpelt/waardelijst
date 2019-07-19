package bsoft.nl.waardelijst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories("bsoft.nl.waardelijst.database.repository")
//@EntityScan("bsoft.nl.waardelijst.database.model")
public class WaardelijstApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaardelijstApplication.class, args);
    }

}
