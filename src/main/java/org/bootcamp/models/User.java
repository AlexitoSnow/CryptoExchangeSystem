package org.bootcamp.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private String name;
    private String email;
    private String password;
    private String userID;
    private final Wallet wallet;
    private List<Transaction> transactions;

    public User(String name, String email, String password) {
        assert(name != null && email != null && password != null);
        this.name = name;
        this.email = email;
        this.password = password;
        wallet = new Wallet();
        transactions = new ArrayList<>();
        generateUserID();
    }

    public void recordTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
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

    public boolean subtractFiatMoney(BigDecimal amount) {
        return wallet.subtractFiatMoney(amount);
    }

    public boolean subtractCryptoCurrency(CryptoCurrency cryptoCurrency, BigDecimal quantity) {
        return wallet.subtractCryptoCurrency(cryptoCurrency, quantity);
    }

    public void depositFiatMoney(BigDecimal amount) {
        wallet.addFiatMoney(amount);
    }

    public void rechargeCryptoCurrency(CryptoCurrency cryptoCurrency, BigDecimal quantity) {
        wallet.rechargeCryptoCurrency(cryptoCurrency, quantity);
    }

    @Override
    public String toString() {
        return "User " + userID + '\n' +
                "Name: " + name + '\n' +
                "Email: " + email + '\n' +
                wallet;
    }
}
