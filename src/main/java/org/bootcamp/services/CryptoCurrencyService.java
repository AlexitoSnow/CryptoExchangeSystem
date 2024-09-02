package org.bootcamp.services;

import org.bootcamp.models.*;
import java.math.BigDecimal;
import java.util.*;

public class CryptoCurrencyService {
    private final Map<CryptoCurrency, BigDecimal> cryptoCurrencies;
    private static CryptoCurrencyService instance;

    private CryptoCurrencyService() {
        this.cryptoCurrencies = new HashMap<>();
        CryptoCurrency bitcoin = new CryptoCurrency("Bitcoin", "BTC", new BigDecimal(50000));
        CryptoCurrency ethereum = new CryptoCurrency("Ethereum", "ETH", new BigDecimal(3000));
        cryptoCurrencies.put(bitcoin, new BigDecimal(100));
        cryptoCurrencies.put(ethereum, new BigDecimal(500));
    }

    public static CryptoCurrencyService getInstance() {
        if (instance == null) {
            instance = new CryptoCurrencyService();
        }
        return instance;
    }

    public Map<CryptoCurrency, BigDecimal> getAvailableCryptoCurrencies() {
        return Collections.unmodifiableMap(cryptoCurrencies);
    }

    public void fluctuateCryptoCurrencyValues() {
        Random random = new Random();
        //RootView view = new RootView();
        String message = "*** THE MARKET HAS CHANGED ***";
        //view.showInfo('\n' + message);
        cryptoCurrencies.forEach(((cryptoCurrency, quantity) -> {
            BigDecimal originalValue = cryptoCurrency.getOriginalValue();
            BigDecimal newValue;
            // Increase price
            if (random.nextBoolean()){
                newValue = originalValue.add(BigDecimal.valueOf(random.nextDouble(originalValue.doubleValue() * 0.0012)));
            // Decrease price
            } else {
                newValue = originalValue.subtract(BigDecimal.valueOf(random.nextDouble(originalValue.doubleValue() * 0.0004)));
            }
            cryptoCurrency.updateCurrentValue(newValue);
            // Fluctuación aumentó
            BigDecimal updatedValue = newValue.subtract(originalValue).setScale(2, RoundingMode.HALF_UP);
            if (newValue.compareTo(originalValue) > 0) {
                //view.showSuccessMessage("%s (+%s)".formatted(cryptoCurrency, updatedValue));
            // Fluctuación bajó
            } else if (newValue.compareTo(originalValue) < 0) {
                //view.showError("%s (-%s)".formatted(cryptoCurrency, originalValue.subtract(newValue).setScale(2, RoundingMode.HALF_UP)));
            // No fluctuó
            } else {
                //view.showInfo("%s (%s)".formatted(cryptoCurrency, updatedValue));
            }
        }));
        //view.showInfo("*".repeat(message.length()));
    }
}