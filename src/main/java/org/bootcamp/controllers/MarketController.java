package org.bootcamp.controllers;

import org.bootcamp.Router;
import org.bootcamp.models.*;
import org.bootcamp.services.AccountService;
import org.bootcamp.services.AccountServiceException;
import org.bootcamp.services.CryptoCurrencyService;
import org.bootcamp.views.MarketView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class MarketController implements Controller, ExchangeServiceSubscriber {
    private final MarketView view;
    private final CryptoCurrencyService cryptoCurrencyService;
    private final AccountService accountService;
    private final int marketAction;

    public MarketController(int marketAction) {
        view = new MarketView();
        cryptoCurrencyService = CryptoCurrencyService.getInstance();
        accountService = AccountService.getInstance();
        this.marketAction = marketAction;
    }

    @Override
    public void run() {
        User user = accountService.getCurrentUser();
        List<CryptoCurrency> cryptoCurrencies = user.getWallet().getMyCryptoCurrencies().keySet().stream().toList();
        CryptoCurrency selected = view.getCryptoCurrencyType(cryptoCurrencies);
        if (selected != null) {
            view.showInfo("Current Price: " + selected.getShorthandSymbol() + " (" + selected.getCurrentValue() + ")");
            view.showError("Price up to date at " + LocalDateTime.now());

            BigDecimal quantity = view.getQuantityCryptoCurrencyInput();
            if (quantity.compareTo(BigDecimal.ZERO) > 0) {
                switch (marketAction) {
                    //Buy from Exchange
                    case 0:
                        buyFromExchange(user, selected, quantity);
                        break;
                    // Buy Order
                    case 1:
                        placeBuyOrder(user, selected, quantity);
                        break;
                    // Selling Order
                    case 2:
                        placeSellingOrder(user, selected, quantity);
                        break;
                }
            } else {
                view.showError("Quantity must be positive");
            }
        } else {
            view.showError("Not cryptocurrency selected!");
        }
        Router.navigateTo(Router.HOME);
    }

    private void buyFromExchange(User user, CryptoCurrency cryptoCurrency, BigDecimal quantity) {
        try {
            cryptoCurrencyService.buyFromExchange(user, cryptoCurrency, quantity);
            view.showSuccessMessage("+" + quantity + " " + cryptoCurrency.getShorthandSymbol() + " added successfully");
        } catch (CryptoCurrencyException | AccountServiceException e) {
            view.showError(e.getMessage());
        }
    }

    private void placeBuyOrder(User user, CryptoCurrency cryptoCurrency, BigDecimal quantity) {
        BigDecimal price = view.getPurchasingPriceInput();
        if (price.compareTo(BigDecimal.ZERO) > 0) {
            if (user.subtractFiatMoney(price)) {
                MarketOrder marketOrder = new MarketOrder(OrderType.BUY, user, cryptoCurrency, quantity, price);
                cryptoCurrencyService.putOrder(marketOrder);
                view.showSuccessMessage("Buy order placed to the Market");
            } else {
                view.showError("User has not enough funds to complete the transaction.");
            }
        } else {
            view.showError("Price must be positive");
        }
    }

    private void placeSellingOrder(User user, CryptoCurrency cryptoCurrency, BigDecimal quantity) {
        BigDecimal price = view.getSellingPriceInput();
        if (price.compareTo(BigDecimal.ZERO) > 0) {
            if (user.subtractCryptoCurrency(cryptoCurrency, quantity)) {
                MarketOrder marketOrder = new MarketOrder(OrderType.SELLING, user, cryptoCurrency, quantity, price);
                cryptoCurrencyService.putOrder(marketOrder);
                view.showSuccessMessage("Selling order placed to the Market");
            } else {
                view.showError("User has not enough cryptocurrencies to complete the transaction.");
            }
        } else {
            view.showError("Price must be positive");
        }
    }
}
