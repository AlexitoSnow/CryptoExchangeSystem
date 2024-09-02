package org.bootcamp.views;

import org.bootcamp.models.CryptoCurrency;
import java.math.BigDecimal;
import java.util.List;

public class MarketView extends View {
    public CryptoCurrency getCryptoCurrencyType(List<CryptoCurrency> cryptoCurrencies) {
        showInfo("Select the cryptocurrency type:");
        for (int i = 0; i < cryptoCurrencies.size(); i++){
            CryptoCurrency currency = cryptoCurrencies.get(i);
            showInfo((i + 1) + ". " + currency.getDisplayName() + "(" + currency.getCurrentValue() + ")");
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
        requestMessage("Selling Price: $");
        return getBigDecimal();
    }

    public BigDecimal getPurchasingPriceInput() {
        requestMessage("Purchasing Price: $");
        return getBigDecimal();
    }

    public BigDecimal getQuantityCryptoCurrencyInput() {
        requestMessage("Amount: ");
        return getBigDecimal();
    }


}
