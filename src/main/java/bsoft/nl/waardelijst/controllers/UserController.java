package bsoft.nl.waardelijst.controllers;

import bsoft.nl.waardelijst.database.model.User;
import bsoft.nl.waardelijst.model.AuthRequest;
import bsoft.nl.waardelijst.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*",
        allowCredentials = "true",
        allowedHeaders = {"*"},
        methods = {RequestMethod.GET,
                RequestMethod.DELETE,
                RequestMethod.POST,
                RequestMethod.OPTIONS
        })
@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${maxCacheTime}")
    private int maxAge;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> retrieveUsers() {
        logger.info("Received request for users");
        List<User> result = userService.retrieveUsers();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(maxAge, TimeUnit.SECONDS).cachePublic())
                .body(result);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        User user1 = userService.findUserById(id);
        logger.info("User: {} found", user1.toString());
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(maxAge, TimeUnit.SECONDS).cachePublic())
                .body(user1);
    }

    @GetMapping("/users/{name}")
    public ResponseEntity<User> findUserByName(@PathVariable String name) {
        User user1 = userService.findUserByName(name);
        logger.info("User: {} found", user1.toString());
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(maxAge, TimeUnit.SECONDS).cachePublic())
                .body(user1);
    }

    @PostMapping("/users/authenticate")
    public ResponseEntity<User> authenticate(@RequestBody AuthRequest authRequest) {
        User user1 = userService.findUserByUsernameAndPassword(authRequest.getUsername(), authRequest.getPassword());
        logger.info("User: {} found", user1.toString());
        return ResponseEntity.ok(user1);
//                .body(user1);
    }

    @PostMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User user1 = userService.updateUser(user);
        logger.info("User: {} updated", user.getUsername());
        return ResponseEntity.ok(user1);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        String message = "User met id " + id + " is verwijderd";
        return ResponseEntity.ok(message);
    }

}
