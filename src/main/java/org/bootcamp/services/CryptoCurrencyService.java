package org.bootcamp.services;

import org.bootcamp.models.CryptoCurrency;

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
        cryptoCurrencies.forEach(((cryptoCurrency, quantity) -> {
            BigDecimal currentValue = cryptoCurrency.getCurrentValue();
            BigDecimal newValue = BigDecimal.ZERO;
            // Increase price
            if (random.nextBoolean()){
                newValue = currentValue.add(new BigDecimal(random.nextInt(100)));
            // Decrease price
            } else {
                newValue = currentValue.subtract(new BigDecimal(random.nextInt(100)));
            }
            cryptoCurrency.updateValue(newValue);
        }));
    }
}