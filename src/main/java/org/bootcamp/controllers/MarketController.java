package org.bootcamp.controllers;

import org.bootcamp.Router;
import org.bootcamp.models.*;
import org.bootcamp.services.AccountService;
import org.bootcamp.services.AccountServiceException;
import org.bootcamp.services.ExchangeService;
import org.bootcamp.services.TradingService;
import org.bootcamp.views.ExchangeServiceSubscriber;
import org.bootcamp.views.MarketView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller class for managing market-related actions and interactions.
 * Implements the Controller and ExchangeServiceSubscriber interfaces to handle
 * user choices, market updates, and notifications.
 * @see MarketView
 * @see Controller
 * @see ExchangeService
 * @see ExchangeServiceSubscriber
 */
public class MarketController implements Controller, ExchangeServiceSubscriber {
    /**
     * The view associated with the market controller, responsible for user interactions in the market.
     */
    private final MarketView view;

    /**
     * The exchange service used for buying and selling cryptocurrencies.
     */
    private final ExchangeService exchangeService;

    /**
     * The trading service used for placing market orders.
     */
    private final TradingService tradingService;

    /**
     * The account service used for user authentication and account management.
     */
    private final AccountService accountService;

    /**
     * Flag indicating whether notifications for cryptocurrency fluctuations are enabled.
     */
    private boolean showNotifications;

    /**
     * The singleton instance of the MarketController.
     */
    public static MarketController instance;

    /**
     * Private constructor for the MarketController. Initializes the view, exchange service,
     * account service, and trading service.
     */
    private MarketController() {
        view = new MarketView();
        exchangeService = ExchangeService.getInstance();
        accountService = AccountService.getInstance();
        tradingService = TradingService.getInstance();
    }

    /**
     * Returns the singleton instance of the MarketController. If the instance does not exist,
     * it is created.
     *
     * @return the singleton instance of the MarketController
     */
    public static MarketController getInstance() {
        if (instance == null) {
            instance = new MarketController();
        }
        return instance;
    }

    /**
     * Runs the main loop of the market controller, handling user choices for various market actions
     * such as toggling notifications, buying from the exchange, placing buy and sell orders, and navigating back.
     * @see Controller#run()
     */
    @Override
    public void run() {
        int choice = view.getUserChoice();
        switch (choice) {
            case 0:
                toggleNotifications();
                break;
            case 1:
                buyFromExchange();
                break;
            case 2:
                placeBuyOrder();
                break;
            case 3:
                placeSellingOrder();
                break;
            case 4:
                back();
                break;
            default:
                view.showError("Invalid option. Please try again.");
        }
        router.navigateTo(Router.MARKET);
    }

    /**
     * Navigates back to the home screen and unsubscribes from the exchange service.
     * @see ExchangeService
     */
    private void back() {
        exchangeService.unSubscribe(this);
        router.navigateTo(Router.HOME);
    }

    /**
     * Toggles the state of notifications for cryptocurrency fluctuations.
     * Subscribes or unsubscribes from the exchange service based on the new state,
     * and displays the current notification status.
     * @see ExchangeService
     */
    private void toggleNotifications() {
        showNotifications = !showNotifications;
        if (showNotifications) {
            exchangeService.subscribe(this);
            view.showInfo("Fluctuations notifications: ON");
        } else {
            exchangeService.unSubscribe(this);
            view.showInfo("Fluctuations notifications: OFF");
        }
    }

    /**
     * Buys cryptocurrency from the exchange for the current user. The method retrieves the user's available cryptocurrencies,
     * prompts the user to select a cryptocurrency and specify the quantity, and then attempts to buy the cryptocurrency from the exchange.<br>
     * If the purchase is successful, a success message is displayed; otherwise, an error message is shown.
     * @see User
     * @see CryptoCurrency
     */
    private void buyFromExchange() {
        User user = accountService.getCurrentUser();
        List<CryptoCurrency> cryptoCurrencies = user.getWallet().getMyCryptoCurrencies().keySet().stream().toList();
        CryptoCurrency selected = view.getCryptoCurrencyType(cryptoCurrencies);
        if (selected != null) {
            view.showInfo("Current Price: " + selected.getShorthandSymbol() + " (" + selected.getCurrentValue() + ")");
            view.showError("Price up to date at " + LocalDateTime.now());
            BigDecimal quantity = view.getQuantityCryptoCurrencyInput();
            if (quantity.compareTo(BigDecimal.ZERO) > 0) {
                try {
                    exchangeService.buyFromExchange(user, selected, quantity);
                    view.showSuccessMessage("+" + quantity + " " + selected.getShorthandSymbol() + " added successfully");
                } catch (CryptoCurrencyException | AccountServiceException e) {
                    view.showError(e.getMessage());
                }
            } else {
                view.showError("Quantity must be positive");
            }
        } else {
            view.showError("Not cryptocurrency selected");
        }
    }

    /**
     * Places a buy order for the current user. The method retrieves the user's available cryptocurrencies,
     * prompts the user to select a cryptocurrency and specify the quantity and price, and then attempts to place
     * the order in the market. If the user has sufficient funds, the order is placed; otherwise, an error
     * message is displayed.
     * @see User
     * @see CryptoCurrency
     */
    private void placeBuyOrder() {
        User user = accountService.getCurrentUser();
        List<CryptoCurrency> cryptoCurrencies = user.getWallet().getMyCryptoCurrencies().keySet().stream().toList();
        CryptoCurrency selected = view.getCryptoCurrencyType(cryptoCurrencies);
        if (selected != null) {
            view.showInfo("Current Price: " + selected.getShorthandSymbol() + " (" + selected.getCurrentValue() + ")");
            view.showError("Price up to date at " + LocalDateTime.now());
            BigDecimal quantity = view.getQuantityCryptoCurrencyInput();
            if (quantity.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal price = view.getPurchasingPriceInput();
                if (price.compareTo(BigDecimal.ZERO) > 0) {
                    if (user.subtractFiatMoney(price)) {
                        MarketOrder marketOrder = new MarketOrder(OrderType.BUY, user, selected, quantity, price);
                        tradingService.putOrder(marketOrder);
                        view.showSuccessMessage("Buy order placed to the Market");
                    } else {
                        view.showError("User has not enough funds to complete the transaction.");
                    }
                } else {
                    view.showError("Price must be positive");
                }
            } else {
                view.showError("Quantity must be positive");
            }
        } else {
            view.showError("Not cryptocurrency selected");
        }
    }

    /**
     * Places a selling order for the current user. The method retrieves the user's available cryptocurrencies,
     * prompts the user to select a cryptocurrency and specify the quantity and price, and then attempts to place
     * the order in the market. If the user has sufficient cryptocurrency, the order is placed; otherwise, an error
     * message is displayed.
     * @see User
     * @see CryptoCurrency
     */
    private void placeSellingOrder() {
        User user = accountService.getCurrentUser();
        List<CryptoCurrency> cryptoCurrencies = user.getWallet().getMyCryptoCurrencies().keySet().stream().toList();
        CryptoCurrency selected = view.getCryptoCurrencyType(cryptoCurrencies);
        if (selected != null) {
            view.showInfo("Current Price: " + selected.getShorthandSymbol() + " (" + selected.getCurrentValue() + ")");
            view.showError("Price up to date at " + LocalDateTime.now());
            BigDecimal quantity = view.getQuantityCryptoCurrencyInput();
            if (quantity.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal price = view.getSellingPriceInput();
                if (price.compareTo(BigDecimal.ZERO) > 0) {
                    if (user.subtractCryptoCurrency(selected, quantity)) {
                        MarketOrder marketOrder = new MarketOrder(OrderType.SELLING, user, selected, quantity, price);
                        tradingService.putOrder(marketOrder);
                        view.showSuccessMessage("Selling order placed to the Market");
                    } else {
                        view.showError("User has not enough cryptocurrencies to complete the transaction.");
                    }
                } else {
                    view.showError("Price must be positive");
                }
            } else {
                view.showError("Quantity must be positive");
            }
        } else {
            view.showError("Not cryptocurrency selected");
        }
    }

    /**
     * Updates the view with the latest cryptocurrency market changes. Displays a message indicating
     * that the market has changed, followed by the updated values of each cryptocurrency. If the current
     * value of a cryptocurrency is higher than its original value, a success message is shown; otherwise,
     * an error message is displayed.
     *
     * @param cryptoCurrencies the list of cryptocurrencies with updated values
     */
    @Override
    public void update(List<CryptoCurrency> cryptoCurrencies) {
        String message = "*** THE MARKET HAS CHANGED ***";
        view.showInfo('\n' + message);
        cryptoCurrencies.forEach(cryptoCurrency -> {
            BigDecimal originalValue = cryptoCurrency.getOriginalValue();
            BigDecimal currentValue = cryptoCurrency.getCurrentValue();
            BigDecimal differenceValue;
            if (currentValue.compareTo(originalValue) >= 0) {
                differenceValue = currentValue.subtract(originalValue).setScale(2, RoundingMode.HALF_UP);
                view.showSuccessMessage("%s (+%s)".formatted(cryptoCurrency, differenceValue));
            } else {
                differenceValue = originalValue.subtract(currentValue).setScale(2, RoundingMode.HALF_UP);
                view.showError("%s (-%s)".formatted(cryptoCurrency, differenceValue));
            }
        });
        view.showInfo("*".repeat(message.length()));
    }
}
