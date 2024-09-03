package org.bootcamp.controllers;

import org.bootcamp.Router;
import org.bootcamp.models.User;
import org.bootcamp.services.AccountService;
import org.bootcamp.services.AccountServiceException;
import org.bootcamp.views.RootView;

public class RootController implements Controller {
    private final RootView view;
    private final AccountService accountService;

    public RootController() {
        this.view = new RootView();
        accountService = AccountService.getInstance();
    }

    public void run() {
        int choice = view.getUserChoice();
        switch (choice) {
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            case 3:
                view.close();
                System.exit(0);
            default:
                view.showError("Invalid option. Please try again.");
                router.navigateTo(Router.ROOT);
        }
    }

    private void register() {
        String name = view.getNameInput().trim();
        String email = view.getEmailInput().trim();
        String password = view.getPasswordInput().trim();
        try {
            User verified = accountService.registerUser(name, email, password);
            if (verified != null) {
                view.showSuccessMessage("Usuario registrado exitosamente");
                router.navigateTo(Router.HOME);
            }
        } catch (AccountServiceException e) {
            view.showError(e.getMessage());
            router.navigateTo(Router.ROOT);
        }
    }

    private void login() {
        String email = view.getEmailInput().trim();
        String password = view.getPasswordInput().trim();
        User user = null;
        try {
            user = accountService.login(email, password);
        } catch (AccountServiceException e) {
            view.showError(e.getMessage());
        }
        if (user != null) {
            view.showSuccessMessage("Logged as " + user.getEmail());
            router.navigateTo(Router.HOME);
        } else {
            router.navigateTo(Router.ROOT);
        }
    }
}