package bsoft.nl.waardelijst.services;


import bsoft.nl.waardelijst.database.model.User;
import bsoft.nl.waardelijst.database.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepo userRepo = null;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Get list of all known users
     *
     * @return
     */
    @Cacheable(cacheNames = "allUsers")
    public List<User> retrieveUsers() {
        List<User> userList = new ArrayList<User>();

        Iterable<User> users = null;
        users = userRepo.findAll();

        int count = 0;
        for (User u : users) {
            count++;
            userList.add(u);
        }

        if ((userList == null) || (count == 0)) {
            throw new UserNotFound("Users niet gevonden");
        }

        return userList;
    }

    /**
     * Get user by id
     *
     * @param id
     * @return Null of exactly one user
     */
    @Cacheable(cacheNames = "UserId", key = "#id")
    public User findUserById(final Long id) {

        Optional<User> user = userRepo.findById(id);

        User u = null;

        if (user.isPresent()) {
            u = user.get();
        }

        return u;
    }

    public User findUserByUsernameAndPassword(final String username, final String password) {

        Optional<User> user = userRepo.findByUsernameAndPassword(username, password);

        User u = null;

        if (user.isPresent()) {
            u = user.get();
        }

        return u;
    }

    /**
     * Get user by username
     *
     * @param name
     * @return Null of exactly one user
     */
    @Cacheable(cacheNames = "UserName", key = "#name")
    public User findUserByName(final String name) {

        Optional<User> user = userRepo.findByUsername(name);

        User u = null;

        if (user.isPresent()) {
            u = user.get();
        }

        return u;
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "UserId", key = "#user.id"),
            @CacheEvict(cacheNames = "UserName", key = "#user.username")
    })
    public User addUser(User user) {
        User changedUser;

        changedUser = userRepo.save(user);  // name is uniqe!!!

        return changedUser;
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "UserId", key = "#user.id"),
            @CacheEvict(cacheNames = "UserName", key = "#user.username")
    })
    public User updateUser(User user) {
        User changedUser;

        changedUser = userRepo.save(user);  // name is uniqe!!!

        return changedUser;
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "UserId", key = "#user.id"),
            @CacheEvict(cacheNames = "UserName", key = "#user.username")
    })
    public void deleteUser(User user) {
        userRepo.deleteById(user.getId());
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "UserId", key = "#user.id"),
            @CacheEvict(cacheNames = "UserName", allEntries = true)
    })
    public void deleteUser(final Long id) {
        userRepo.deleteById(id);
    }
}
