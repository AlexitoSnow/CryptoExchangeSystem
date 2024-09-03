package org.bootcamp;

import org.bootcamp.services.AccountService;
import org.bootcamp.services.CryptoCurrencyService;
import org.bootcamp.services.TradingService;

public class CryptoExchangeApp {

    public static void main(String[] args) {
        init();
        Router router = Router.getInstance();
        router.navigateTo(Router.ROOT);
    }

    /**
     * Inicializa los servicios de la plataforma
     */
    public static void init() {
        CryptoCurrencyService.getInstance();
        AccountService.getInstance();
        TradingService.getInstance();
    }
}