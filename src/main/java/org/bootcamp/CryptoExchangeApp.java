package org.bootcamp;

import org.bootcamp.controllers.RootController;
import org.bootcamp.services.AccountService;
import org.bootcamp.services.CryptoCurrencyService;

public class CryptoExchangeApp {
    //TODO: IMPLEMENTAR OBSERVER PARA LAS FLUCTUACIONES
    //TODO: IMPLEMENTAR STRATEGY PARA LA SELECCIÓN DE OPCIONES

    public static void main(String[] args) {
        init();
        RootController controller = new RootController();
        controller.run();
    }

    /**
     * Inicializa los servicios de la plataforma
     */
    public static void init() {
        CryptoCurrencyService.getInstance();
        AccountService.getInstance();
    }
}