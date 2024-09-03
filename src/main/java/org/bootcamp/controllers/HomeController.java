package org.bootcamp.controllers;

import org.bootcamp.Router;
import org.bootcamp.models.MarketOrder;
import org.bootcamp.models.Transaction;
import org.bootcamp.services.AccountService;
import org.bootcamp.services.TradingService;
import org.bootcamp.views.HomeView;
import org.bootcamp.views.TradingServiceSubscriber;

import java.math.BigDecimal;
import java.util.List;

public class HomeController implements Controller, TradingServiceSubscriber {
    private final HomeView view;
    private final AccountService accountService;
    private final TradingService tradingService;
    public static HomeController instance;

    private HomeController() {
        view = new HomeView();
        accountService = AccountService.getInstance();
        tradingService = TradingService.getInstance();
        tradingService.subscribe(this);
    }

    public static HomeController getInstance() {
        if (instance == null) {
            instance = new HomeController();
        }
        return instance;
    }

    @Override
    public void run() {
        int choice = view.getUserChoice();
        switch (choice) {
            case 1:
                depositAction();
                break;
            case 2:
                showWallet();
                break;
            case 3:
                showTransactionHistory();
                break;
            case 4:
                goToMarket();
                break;
            case 5:
                logout();
                break;
            default:
                view.showError("Invalid option. Please try again.");
        }
        router.navigateTo(Router.HOME);
    }

    private void logout() {
        accountService.logout();
        view.showSuccessMessage("Logging out...");
        tradingService.unSubscribe(this);
        router.navigateTo(Router.ROOT);
    }

    private void showTransactionHistory() {
        List<Transaction> transactions = accountService.getCurrentUser().getTransactions();
        if (transactions.isEmpty()){
            view.showInfo("You don't have transactions yet!");
        } else {
            view.showInfo("Type\tCrypto\tAmount\tPrice");
            transactions.forEach(transaction -> view.showInfo(transaction.toString()));
        }
    }

    private void goToMarket() {
        tradingService.unSubscribe(this);
        router.navigateTo(Router.MARKET);
    }

    private void showWallet() {
        view.showWallet(accountService.getCurrentUser().getWallet());
    }

    private void depositAction() {
        BigDecimal amount = view.getAmountMoneyInput();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            view.showError("Enter positive numbers only");
        } else {
            accountService.getCurrentUser().depositFiatMoney(amount);
            view.showSuccessMessage("Balance updated!");
            showWallet();
        }
    }

    @Override
    public void update(MarketOrder buyOrder, MarketOrder sellingOrder) {
        if (buyOrder.getUser().equals(accountService.getCurrentUser())) {
            view.showSuccessMessage("Congrats, your buy order " + buyOrder.getOrderID() + " is completed");
            view.showSuccessMessage("Check your wallet!");
        }
        if (sellingOrder.getUser().equals(accountService.getCurrentUser())) {
            view.showSuccessMessage("Congrats, your selling order " + sellingOrder.getOrderID() + " is completed");
            view.showSuccessMessage("Check your wallet!");
        }
    }

}
