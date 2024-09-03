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

public class MarketController implements Controller, ExchangeServiceSubscriber {
    private final MarketView view;
    private final ExchangeService exchangeService;
    private final TradingService tradingService;
    private final AccountService accountService;
    private boolean showNotifications;
    public static MarketController instance;

    private MarketController() {
        view = new MarketView();
        exchangeService = ExchangeService.getInstance();
        accountService = AccountService.getInstance();
        tradingService = TradingService.getInstance();
    }

    public static MarketController getInstance() {
        if (instance == null) {
            instance = new MarketController();
        }
        return instance;
    }

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

    private void back() {
        exchangeService.unSubscribe(this);
        router.navigateTo(Router.HOME);
    }

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
