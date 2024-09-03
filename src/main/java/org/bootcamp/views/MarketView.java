package org.bootcamp.views;

import org.bootcamp.models.CryptoCurrency;
import java.math.BigDecimal;
import java.util.List;

/**
 * Represents the market view in the application.<br>
 * This class extends the base View class and provides various functionalities
 * for interacting with the market and handling user inputs related to cryptocurrency transactions.
 */
public class MarketView extends View {

    /**
     * Prompts the user to choose an option from a list of available actions.<br>
     * The options include enabling/disabling notifications about cryptocurrency fluctuations,
     * buying from the exchange, placing buy or sell orders, and going back to the profile.
     *
     * @return The user's choice as an integer.
     */
    public int getUserChoice() {
        showInfo("Choose one option to continue:");
        showInfo("0. Enable/Disable notifications about cryptocurrencies fluctuations (in this view)");
        showInfo("1. Buy from exchange");
        showInfo("2. Place buy order");
        showInfo("3. Place sell order");
        showInfo("4. Back to profile");
        System.out.print("Enter your choice: ");
        return getChoice();
    }

    /**
     * Prompts the user to select a type of cryptocurrency from a provided list.<br>
     * Displays the available cryptocurrencies and returns the selected one.
     *
     * @param cryptoCurrencies The list of available cryptocurrencies.
     * @return The selected cryptocurrency, or null if the choice is invalid.
     */
    public CryptoCurrency getCryptoCurrencyType(List<CryptoCurrency> cryptoCurrencies) {
        showInfo("Select the cryptocurrency type:");
        for (int i = 0; i < cryptoCurrencies.size(); i++){
            CryptoCurrency currency = cryptoCurrencies.get(i);
            showInfo((i + 1) + ". " + currency.getDisplayName());
        }
        System.out.print("Enter your choice: ");
        int choice = getChoice();
        if (choice > 0 && choice <= cryptoCurrencies.size()) {
            return cryptoCurrencies.get(--choice);
        } else {
            return null;
        }
    }

    /**
     * Prompts the user to input the selling price they are willing to accept.<br>
     * The method requests the selling price and returns it as a BigDecimal.
     *
     * @return The selling price entered by the user as a BigDecimal.
     */
    public BigDecimal getSellingPriceInput() {
        showInfo("This is the price you are willing to accept");
        requestMessage("Selling Price: $");
        return getBigDecimal();
    }

    /**
     * Prompts the user to input the maximum price they are willing to pay.<br>
     * The method requests the purchasing price and returns it as a BigDecimal.
     *
     * @return The purchasing price entered by the user as a BigDecimal.
     */
    public BigDecimal getPurchasingPriceInput() {
        showInfo("This is the maximum price you are willing to pay");
        requestMessage("Purchasing Price: $");
        return getBigDecimal();
    }

    /**
     * Prompts the user to input the amount of cryptocurrency.<br>
     * The method requests the amount and returns it as a BigDecimal.
     *
     * @return The amount of cryptocurrency entered by the user as a BigDecimal.
     */
    public BigDecimal getQuantityCryptoCurrencyInput() {
        requestMessage("Amount: ");
        return getBigDecimal();
    }
}
