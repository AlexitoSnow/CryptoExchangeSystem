package org.bootcamp.views;

import org.bootcamp.models.CryptoCurrency;
import java.math.BigDecimal;
import java.util.List;

public class MarketView extends View {

    /**
     * Options:
     * 0. Enable/Disable notifications about cryptocurrencies fluctuations (in this view)
     * 1. Comprar por intercambio
     * 2. Enviar orden de compra
     * 3. Enviar orden de venta
     * 4. Volver a mi cuenta
     * @return opción escogida
     */
    public int getUserChoice() {
        showInfo("Choose one option to continue:");
        showInfo("0. Enable/Disable notifications about cryptocurrencies fluctuations (in this view)");
        showInfo("1. Buy from exchange");
        showInfo("2. Place buy order");
        showInfo("3. Place sell order");
        showInfo("4. Back to profile");
        System.out.print("Enter your choice: ");
        return getChoice();
    }

    public CryptoCurrency getCryptoCurrencyType(List<CryptoCurrency> cryptoCurrencies) {
        showInfo("Select the cryptocurrency type:");
        for (int i = 0; i < cryptoCurrencies.size(); i++){
            CryptoCurrency currency = cryptoCurrencies.get(i);
            showInfo((i + 1) + ". " + currency.getDisplayName());
        }
        System.out.print("Enter your choice: ");
        int choice = getChoice();
        if (choice > 0 && choice <= cryptoCurrencies.size()) {
            return cryptoCurrencies.get(--choice);
        } else {
            return null;
        }
    }

    public BigDecimal getSellingPriceInput() {
        showInfo("This is the price you are willing to accept");
        requestMessage("Selling Price: $");
        return getBigDecimal();
    }

    public BigDecimal getPurchasingPriceInput() {
        showInfo("This is the maximum price you are willing to pay");
        requestMessage("Purchasing Price: $");
        return getBigDecimal();
    }

    public BigDecimal getQuantityCryptoCurrencyInput() {
        requestMessage("Amount: ");
        return getBigDecimal();
    }


}
