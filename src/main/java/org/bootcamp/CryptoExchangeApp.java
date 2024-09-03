package org.bootcamp;

import org.bootcamp.services.AccountService;
import org.bootcamp.services.ExchangeService;
import org.bootcamp.services.TradingService;

/**
 * It is the starting point of the application,
 * initializes the consumed services and sets
 * the initial navigation route.
 * @see Router
 * @see ExchangeService
 * @see AccountService
 * @see TradingService
 */
public class CryptoExchangeApp {

    public static void main(String[] args) {
        init();
        Router router = Router.getInstance();
        router.navigateTo(Router.ROOT);
    }

    /**
     * Initializes the platform services
     * @see ExchangeService
     * @see AccountService
     * @see TradingService
     */
    public static void init() {
        ExchangeService.getInstance();
        AccountService.getInstance();
        TradingService.getInstance();
    }
}