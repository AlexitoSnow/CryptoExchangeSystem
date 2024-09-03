package org.bootcamp.views;

import org.bootcamp.models.Wallet;
import java.math.BigDecimal;

/**
 * Represents the home view in the application.
 * This class extends the base View class and provides various functionalities
 * for interacting with the user's wallet and choices.
 * @see View
 */
public class HomeView extends View {

    /**
     * Prompts the user to choose an option from a list of available actions.<br>
     * The options include depositing money, viewing wallet balance, viewing transaction history,
     * going to the market, and logging out.
     *
     * @return The user's choice as an integer.
     */
    public int getUserChoice() {
        showInfo("Choose one option to continue:");
        showInfo("1. Deposit money");
        showInfo("2. View wallet balance");
        showInfo("3. View transaction history");
        showInfo("4. Go to market");
        showInfo("5. Logout");
        System.out.print("Enter your choice: ");
        return getChoice();
    }

    /**
     * Displays the details of the user's wallet.<br>
     * This method shows the wallet information using a success message.
     *
     * @param wallet The wallet object containing the user's wallet details.
     */
    public void showWallet(Wallet wallet) {
        showInfo("My Wallet");
        showSuccessMessage(wallet.toString());
    }

    /**
     * Prompts the user to input an amount of money.<br>
     * The method requests the amount and returns it as a BigDecimal.
     *
     * @return The amount of money entered by the user as a BigDecimal.
     */
    public BigDecimal getAmountMoneyInput() {
        requestMessage("Amount: $");
        return getBigDecimal();
    }

}
