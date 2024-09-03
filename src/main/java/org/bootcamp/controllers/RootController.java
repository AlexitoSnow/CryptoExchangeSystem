package org.bootcamp.controllers;

import org.bootcamp.Router;
import org.bootcamp.models.User;
import org.bootcamp.services.AccountService;
import org.bootcamp.services.AccountServiceException;
import org.bootcamp.views.RootView;

/**
 * Controller class for handling the root view and user interactions.
 * Manages user registration, login, and navigation based on user choices.
 * @see Controller
 * @see RootView
 */
public class RootController implements Controller {
    /**
     * The view associated with the root controller, responsible for user interactions.
     */
    private final RootView view;
    /**
     * The account service used for user registration and login operations.
     */
    private final AccountService accountService;

    /**
     * Constructs a new RootController, initializing the view and account service.
     */
    public RootController() {
        this.view = new RootView();
        accountService = AccountService.getInstance();
    }

    /**
     * Runs the main loop of the application, handling user choices for registration, login, and exit.
     * @see Controller
     */
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

    /**
     * Handles the user registration process by collecting input from the view,
     * registering the user through the account service, and navigating based on the result.
     */
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

    /**
     * Handles the user login process by collecting input from the view,
     * logging in the user through the account service, and navigating based on the result.
     */
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