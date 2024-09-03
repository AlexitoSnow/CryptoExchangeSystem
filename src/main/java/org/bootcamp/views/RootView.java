package org.bootcamp.views;

/**
 * Represents the main view in the application.<br>
 * This class extends the base View class and provides various functionalities
 * for interacting with the user during the registration and login processes.
 */
public class RootView extends View {

    /**
     * Prompts the user to choose an option from a list of
     * available actions in the Crypto Exchange System.<br>
     * The options include registering, logging in, and quitting the application.
     *
     * @return The user's choice as an integer.
     */
    public int getUserChoice() {
        showInfo("Crypto Exchange System");
        showInfo("Choose one option to continue:");
        showInfo("1. Register");
        showInfo("2. Login");
        showInfo("3. Quit");
        System.out.print("Enter your choice: ");
        return getChoice();
    }

    /**
     * Prompts the user to input their name.<br>
     * The method requests the name and returns it as a String.
     *
     * @return The name entered by the user as a String.
     */
    public String getNameInput() {
        requestMessage("Name: ");
        return getString();
    }

    /**
     * Prompts the user to input their password.<br>
     * The method requests the password and returns it as a String.
     *
     * @return The password entered by the user as a String.
     */
    public String getPasswordInput() {
        requestMessage("Password: ");
        return getString(true);
    }

    /**
     * Prompts the user to input their email address.<br>
     * The method requests the email and returns it as a String.
     *
     * @return The email address entered by the user as a String.
     */
    public String getEmailInput() {
        requestMessage("Email: ");
        return getString(true);
    }
}
