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

/**
 * Controller class for managing the home view and user interactions on the home screen.
 * Implements a singleton pattern to ensure only one instance of HomeController exists.
 * Subscribes to the trading service for updates.
 * @see Controller
 * @see TradingServiceSubscriber
 * @see TradingService
 */
public class HomeController implements Controller, TradingServiceSubscriber {
    /**
     * The view associated with the home controller, responsible for user interactions on the home screen.
     */
    private final HomeView view;
    /**
     * The account service used for user authentication and account management.
     */
    private final AccountService accountService;
    /**
     * The trading service used for placing market orders and receiving updates.
     */
    private final TradingService tradingService;
    /**
     * The singleton instance of the HomeController.
     */
    public static HomeController instance;

    /**
     * Private constructor for the HomeController. Initializes the view, account service,
     * and trading service, and subscribes to the trading service for updates.
     */
    private HomeController() {
        view = new HomeView();
        accountService = AccountService.getInstance();
        tradingService = TradingService.getInstance();
        tradingService.subscribe(this);
    }

    /**
     * Returns the singleton instance of the HomeController. If the instance does not exist,
     * it is created.
     *
     * @return the singleton instance of the HomeController
     */
    public static HomeController getInstance() {
        if (instance == null) {
            instance = new HomeController();
        }
        return instance;
    }

    /**
     * Runs the main loop of the home controller, handling user choices for various actions
     * such as depositing funds, showing the wallet, viewing transaction history, going to the market, and logging out.
     */
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

    /**
     * Logs out the current user, shows a success message, unsubscribes from the trading service,
     * and navigates to the root view.
     */
    private void logout() {
        accountService.logout();
        view.showSuccessMessage("Logging out...");
        tradingService.unSubscribe(this);
        router.navigateTo(Router.ROOT);
    }

    /**
     * Displays the transaction history of the current user. If there are no transactions,
     * an informational message is shown. Otherwise, the details of each transaction are displayed.
     */
    private void showTransactionHistory() {
        List<Transaction> transactions = accountService.getCurrentUser().getTransactions();
        if (transactions.isEmpty()){
            view.showInfo("You don't have transactions yet!");
        } else {
            view.showInfo("Type\tCrypto\tAmount\tPrice");
            transactions.forEach(transaction -> view.showInfo(transaction.toString()));
        }
    }

    /**
     * Navigates to the market view, unsubscribes from the trading service, and updates the router.
     */
    private void goToMarket() {
        tradingService.unSubscribe(this);
        router.navigateTo(Router.MARKET);
    }

    /**
     * Displays the current user's wallet information in the view.
     */
    private void showWallet() {
        view.showWallet(accountService.getCurrentUser().getWallet());
    }

    /**
     * Handles the deposit action by prompting the user for an amount, validating the input,
     * updating the user's balance, and displaying the updated wallet information.
     */
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

    /**
     * Updates the view with the status of completed market orders. If the current user's buy order
     * is completed, a success message is displayed. Similarly, if the current user's selling order
     * is completed, a success message is shown.
     *
     * @param buyOrder the completed buy order
     * @param sellingOrder the completed selling order
     */
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
