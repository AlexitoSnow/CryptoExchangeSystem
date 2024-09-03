package org.bootcamp.services;

import org.bootcamp.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * The AccountService class provides functionalities to manage user accounts.
 * It supports user registration, login, and logout operations, and maintains a list of registered users.
 * This class follows the singleton pattern to ensure only one instance is used throughout the application.
 */
public class AccountService {
    /**
     * A list of registered users.
     */
    private final List<User> users;
    /**
     * Singleton instance of the AccountService class.
     */
    private static AccountService instance;
    /**
     * The currently logged-in user.
     */
    private User user;

    /**
     * Private constructor to initialize the AccountService.
     * Initializes the list of users.
     */
    private AccountService() {
        this.users = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the AccountService.<br>
     * If the instance does not exist, it creates a new one.
     *
     * @return the singleton instance of AccountService
     */
    public static AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    /**
     * Returns the currently logged-in user.
     *
     * @return the current user
     */
    public User getCurrentUser() {
        return user;
    }

    /**
     * Registers a new user with the specified name, email, and password.
     *
     * @param name the name of the user
     * @param email the email of the user
     * @param password the password of the user
     * @return the newly registered user
     * @throws AccountServiceException if the email is already in use
     */
    public User registerUser(String name, String email, String password) throws AccountServiceException {
        for (User user : users) {
            if (user.getEmail().equals(email)){
                throw new AccountServiceException("Este correo se encuentra en uso");
            }
        }
        User newUser = new User(name, email, password);
        users.add(newUser);
        user = newUser;
        return user;
    }

    /**
     * Logs in a user with the specified email and password.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return the logged-in user
     * @throws AccountServiceException if the user is not found
     */
    public User login(String email, String password) throws AccountServiceException {
        int index = users.indexOf(new User("temp", email, password));
        if (index >= 0) {
            user = users.get(index);
            return user;
        }
        throw new AccountServiceException("User not found");
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        user = null;
    }

}
