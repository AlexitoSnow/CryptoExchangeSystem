package org.bootcamp.services;

import org.bootcamp.models.*;
import java.math.BigDecimal;
import java.util.*;

public class CryptoCurrencyService {
    private final Map<CryptoCurrency, BigDecimal> cryptoCurrencies;
    private static CryptoCurrencyService instance;

    private CryptoCurrencyService() {
        cryptoCurrencies = new HashMap<>();
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

    public void buyFromExchange(User user, CryptoCurrency cryptoCurrency, BigDecimal quantity) throws CryptoCurrencyException, AccountServiceException {
        if (cryptoCurrencies.get(cryptoCurrency).compareTo(quantity) >= 0) {
            BigDecimal currentCryptoValue = cryptoCurrency.getCurrentValue();
            boolean success = user.getWallet().subtractFiatMoney(currentCryptoValue.multiply(quantity));
            if (success) {
                user.getWallet().rechargeCryptoCurrency(cryptoCurrency, quantity);
                subtractCryptoCurrency(cryptoCurrency, quantity);
                Transaction transaction = new Transaction(TransactionAction.EXCHANGE, cryptoCurrency, quantity, currentCryptoValue.multiply(quantity));
                user.recordTransaction(transaction);
            } else {
                throw new AccountServiceException("User has not enough funds to complete the transaction.");
            }
        } else {
            throw new CryptoCurrencyException("Exchange has not enough funds to complete the transaction.");
        }
    }

    private void subtractCryptoCurrency(CryptoCurrency cryptoCurrency, BigDecimal value) {
        BigDecimal oldValue = cryptoCurrencies.get(cryptoCurrency);
        System.out.println(oldValue);
        cryptoCurrencies.replace(cryptoCurrency, oldValue.subtract(value));
        System.out.println(cryptoCurrencies.get(cryptoCurrency));
    }

    /**
     * Las criptomonedas fluctuan cada 6 segundos
     * BTC fluctua cada 6 segundos aprox, puede:
     * bajar (entre 0.0008 o 0.0001)%, subir (entre 0.0001 o 0.0007)% o mantenerse
     * ETH fluctua cada 7 segundos aprox, puede:
     * bajar (entre 0.0004 o 0.0001)%, subir (entre 0.0001 o 0.0012)% o mantenerse
     * Para calcular, hay que tener registrado el precio original, en base a ese
     * se realiza el precio de fluctuación
     */
    public void fluctuateCryptoCurrencyValues() {
        Random random = new Random();
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
        }));
        //view.showInfo("*".repeat(message.length()));
    }
}