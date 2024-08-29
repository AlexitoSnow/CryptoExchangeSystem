package org.bootcamp.views;

import java.util.InputMismatchException;

public class RootView extends View {

    public int getUserChoice() {
        showInfo("Crypto Exchange System");
        showInfo("Choose one option to continue:");
        showInfo("1. Register");
        showInfo("2. Login");
        showInfo("3. Quit");
        System.out.print("Enter your choice: ");
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            return INVALID_CHOICE;
        } finally {
            scanner.nextLine();
        }
    }

    public String getNameInput() {
        requestMessage("Name: ");
        return scanner.nextLine();
    }

    public String getPasswordInput() {
        requestMessage("Password: ");
        return scanner.next();
    }

    public String getEmailInput() {
        requestMessage("Email: ");
        return scanner.next();
    }
}
