package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {

    /**
     * Save a user to the database.
     *
     * @param user The user object to be saved.
     * @return User
     */
    public User saveUser(User user);

    /**
     * Delete the given user from the database.
     *
     * @param user The user to be deleted.
     */
    public void deleteUser(User user);

    /**
     * Get all the users.
     *
     * @return A list of all the users in the database.
     */
    public List<User> getAllUser();

    /**
     * Get a user by its id.
     *
     * @param id The id of the user to be retrieved.
     * @return A User object.
     */
    public User getUserById(Integer id);
}
