package org.bootcamp.models;

import java.math.BigDecimal;
import java.util.Objects;

public class User {
    private String name;
    private String email;
    private String password;
    private String userID;
    private final Wallet wallet;

    public User(String name, String email, String password) {
        assert(name != null && email != null && password != null);
        this.name = name;
        this.email = email;
        this.password = password;
        wallet = new Wallet();
        generateUserID();
    }

    private void generateUserID() {
        userID = email.split("@")[0] +
                "@" +
                hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void depositFiatMoney(BigDecimal amount) {
        wallet.addFiatMoney(amount);
    }

    @Override
    public String toString() {
        return "User " + userID + '\n' +
                "Name: " + name + '\n' +
                "Email: " + email + '\n' +
                wallet;
    }
}
