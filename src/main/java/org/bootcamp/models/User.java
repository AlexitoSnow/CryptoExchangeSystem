package org.bootcamp.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a user in the system with a unique ID, name, email, and password.
 * Each user has an associated wallet for managing fiat money and cryptocurrencies,
 * as well as a list of transactions they have performed.
 */
public class User {
    private final String name;
    private final String email;
    private final String password;
    private String userID;
    private final Wallet wallet;
    private final List<Transaction> transactions;

    /**
     * Constructs a new User with the specified name, email, and password.
     * Initializes the user's wallet and transaction list, and generates a unique user ID.
     *
     * @param name the name of the user, must not be null
     * @param email the email of the user, must not be null
     * @param password the password of the user, must not be null
     */
    public User(String name, String email, String password) {
        assert(name != null && email != null && password != null);
        this.name = name;
        this.email = email;
        this.password = password;
        wallet = new Wallet();
        transactions = new ArrayList<>();
        generateUserID();
    }

    /**
     * Records a transaction for the user by adding it to the user's transaction list.
     *
     * @param transaction the transaction to be recorded
     */
    public void recordTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Generates a unique user ID based on the user's email and the object's hash code.
     * The user ID is a combination of the email's local part and the object's hash code.
     */
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

    public String getEmail() {
        return email;
    }

    public Wallet getWallet() {
        return wallet;
    }

    /**
     * Subtracts a specified amount of fiat money from the user's wallet.
     *
     * @param amount the amount of fiat money to subtract
     * @return true if the subtraction was successful, false otherwise
     */
    public boolean subtractFiatMoney(BigDecimal amount) {
        return wallet.subtractFiatMoney(amount);
    }

    /**
     * Subtracts a specified quantity of cryptocurrency from the user's wallet.
     *
     * @param cryptoCurrency the type of cryptocurrency to subtract
     * @param quantity the quantity of cryptocurrency to subtract
     * @return true if the subtraction was successful, false otherwise
     */
    public boolean subtractCryptoCurrency(CryptoCurrency cryptoCurrency, BigDecimal quantity) {
        return wallet.subtractCryptoCurrency(cryptoCurrency, quantity);
    }

    /**
     * Deposits a specified amount of fiat money into the user's wallet.
     *
     * @param amount the amount of fiat money to deposit
     */
    public void depositFiatMoney(BigDecimal amount) {
        wallet.addFiatMoney(amount);
    }

    /**
     * Recharges the user's wallet with a specified quantity of cryptocurrency.
     *
     * @param cryptoCurrency the type of cryptocurrency to recharge
     * @param quantity the quantity of cryptocurrency to add
     */
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
