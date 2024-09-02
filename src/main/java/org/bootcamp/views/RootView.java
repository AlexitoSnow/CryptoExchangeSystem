package org.bootcamp.views;

public class RootView extends View {

    public int getUserChoice() {
        showInfo("Crypto Exchange System");
        showInfo("Choose one option to continue:");
        showInfo("1. Register");
        showInfo("2. Login");
        showInfo("3. Quit");
        System.out.print("Enter your choice: ");
        return getChoice();
    }

    public String getNameInput() {
        requestMessage("Name: ");
        return getString();
    }

    public String getPasswordInput() {
        requestMessage("Password: ");
        return getString(true);
    }

    public String getEmailInput() {
        requestMessage("Email: ");
        return getString(true);
    }
}
