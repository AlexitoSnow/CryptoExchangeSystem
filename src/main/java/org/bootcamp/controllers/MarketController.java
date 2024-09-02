package org.bootcamp.controllers;

import org.bootcamp.Router;
import org.bootcamp.models.*;
import org.bootcamp.services.AccountService;
import org.bootcamp.services.AccountServiceException;
import org.bootcamp.services.CryptoCurrencyService;
import org.bootcamp.views.MarketView;

import java.math.BigDecimal;
import java.util.List;

public class MarketController extends Controller{
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
        MarketOrder marketOrder;
        if (selected != null) {
            BigDecimal quantity = view.getQuantityCryptoCurrencyInput();
            BigDecimal price;
            switch (marketAction) {
                //Buy from Exchange
                case 0:
                    try {
                        cryptoCurrencyService.buyFromExchange(user, selected, quantity);
                        view.showSuccessMessage("+" + quantity + " " + selected.getShorthandSymbol() + " added successfully");
                    } catch (CryptoCurrencyException | AccountServiceException e) {
                        view.showError(e.getMessage());
                    }
                    break;
                    // Buy Order
                case 1:
                    price = view.getPurchasingPriceInput();
                    marketOrder = new MarketOrder(OrderType.BUY, user, selected, quantity, price);
                    break;
                // Selling Order
                case 2:
                    price = view.getSellingPriceInput();
                    marketOrder = new MarketOrder(OrderType.SELLING, user, selected, quantity, price);
            }
        } else {
            view.showError("Error: Not cryptocurrency selected!");
        }
        Router.navigateTo(Router.HOME);
    }
}
