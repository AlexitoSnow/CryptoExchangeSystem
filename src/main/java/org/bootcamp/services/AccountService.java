package org.bootcamp.services;

import org.bootcamp.models.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountService {
    private final List<User> users;
    private static AccountService instance;
    private User user;

    private AccountService() {
        this.users = new ArrayList<>();
    }

    public static AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    /**
     * Retorna el usuario registrado o que ha iniciado sesión recientemente
     * @return si está registrado, devuelve el usuario, o nulo caso contrario
     */
    public User getCurrentUser() {
        return user;
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    /**
     * Registra un nuevo usuario con los datos recibidos
     * @param name del nuevo usuario
     * @param email del nuevo usuario, no debe estar en uso
     * @param password del nuevo usuario
     * @return las credenciales del usuario registrado
     * @throws AccountServiceException si el correo se encuentra en uso
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
     * Inicia sesión en la cuenta con el correo y contraseña ingresados
     * @param email del usuario previamente registrado
     * @param password del usuario previamente registrado
     * @return el usuario registrado, o null si no se encuentra
     */
    public User login(String email, String password) throws AccountServiceException {
        if (isValidEmail(email)) {
            int index = users.indexOf(new User("temp", email, password));
            if (index >= 0) {
                user = users.get(index);
                return user;
            }
            throw new AccountServiceException("User not found");
        }
        throw new AccountServiceException("Invalid email");
    }

    public void logout() {
        user = null;
    }

    private boolean isStrongPassword(String password) {
        return true;
    }

    private boolean isValidEmail(String email) {
        return true;
    }
}
