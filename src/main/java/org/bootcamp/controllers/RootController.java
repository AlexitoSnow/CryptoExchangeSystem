package org.bootcamp.controllers;

import org.bootcamp.Router;
import org.bootcamp.models.User;
import org.bootcamp.services.AccountService;
import org.bootcamp.services.AccountServiceException;
import org.bootcamp.views.RootView;

public class RootController extends Controller {
    private final RootView view;
    private final AccountService service;

    public RootController() {
        this.view = new RootView();
        service = AccountService.getInstance();
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
                Router.navigateTo(Router.ROOT);
        }
    }

    private void register() {
        String name = view.getNameInput().trim();
        String email = view.getEmailInput().trim();
        String password = view.getPasswordInput().trim();
        try {
            User verified = service.registerUser(name, email, password);
            if (verified != null) {
                view.showSuccessMessage("Usuario registrado exitosamente");
                Router.navigateTo(Router.HOME);
            }
        } catch (AccountServiceException e) {
            view.showError(e.getMessage());
            Router.navigateTo(Router.ROOT);
        }
    }

    private void login() {
        String email = view.getEmailInput().trim();
        String password = view.getPasswordInput().trim();
        User user = null;
        try {
            user = service.login(email, password);
        } catch (AccountServiceException e) {
            view.showError(e.getMessage());
        }
        if (user != null) {
            view.showSuccessMessage("Logged as " + user.getEmail());
            Router.navigateTo(Router.HOME);
        } else {
            Router.navigateTo(Router.ROOT);
        }
    }

}