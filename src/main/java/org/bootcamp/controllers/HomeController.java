package org.bootcamp.controllers;

import org.bootcamp.Router;
import org.bootcamp.services.AccountService;
import org.bootcamp.views.HomeView;

import java.math.BigDecimal;

public class HomeController extends Controller {
    private final HomeView view;
    private final AccountService service;

    public HomeController() {
        view = new HomeView();
        service = AccountService.getInstance();
    }

    @Override
    public void run() {
        view.showSuccessMessage("Welcome back, " + service.getCurrentUser().getName() + '!');
        int choice = view.getUserChoice();
        switch (choice) {
            case 1:
                depositAction();
                break;
            case 2:
                showWallet();
                break;
            case 3:
                buyAction();
                break;
            case 4:
                sellAction();
            case 5:
                logout();
            default:
                view.showError("Invalid option. Please try again.");
        }
        Router.navigateTo(Router.HOME);
    }

    private void logout() {
        service.logout();
        view.showSuccessMessage("Logging out...");
        Router.navigateTo(Router.ROOT);
    }

    private void showWallet() {
        view.showWallet(service.getCurrentUser().getWallet());
    }

    private void sellAction() {

    }

    private void buyAction() {

    }

    private void depositAction() {
        BigDecimal amount = view.getAmountMoneyInput();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            view.showError("Enter positive numbers only");
        } else {
            boolean response = service.depositFiatMoney(amount);
            if (response) {
                view.showSuccessMessage("Balance updated!");
                showWallet();
            } else {
                view.showError("Unexpected error while updating your balance");
            }
        }
    }
}
