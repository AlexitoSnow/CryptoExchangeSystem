package org.bootcamp.services;

import org.bootcamp.models.*;
import org.bootcamp.views.ExchangeServiceSubscriber;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The ExchangeService class provides functionalities to manage and trade cryptocurrencies.<br>
 * It maintains a list of available cryptocurrencies, allows users to subscribe for updates,
 * and simulates the fluctuation of cryptocurrency values.
 */
public class ExchangeService {
    /**
     * A map storing the available cryptocurrencies and their corresponding values.
     */
    private final Map<CryptoCurrency, BigDecimal> cryptoCurrencies;

    /**
     * Singleton instance of the ExchangeService class.
     */
    private static ExchangeService instance;

    /**
     * A list of subscribers to the exchange service.
     */
    private final List<ExchangeServiceSubscriber> exchangeServiceSubscribers;

    /**
     * Private constructor to initialize the ExchangeService.<br>
     * Initializes the cryptocurrency map with predefined values and sets up a scheduled task
     * to fluctuate cryptocurrency values at fixed intervals.
     */
    private ExchangeService() {
        cryptoCurrencies = new HashMap<>();
        CryptoCurrency bitcoin = new CryptoCurrency("Bitcoin", "BTC", new BigDecimal(50000));
        CryptoCurrency ethereum = new CryptoCurrency("Ethereum", "ETH", new BigDecimal(3000));
        cryptoCurrencies.put(bitcoin, new BigDecimal(100));
        cryptoCurrencies.put(ethereum, new BigDecimal(500));
        exchangeServiceSubscribers = new ArrayList<>();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable tarea = this::fluctuateCryptoCurrencyValues;

        executor.scheduleAtFixedRate(tarea, 5, 5, TimeUnit.SECONDS);
    }

    /**
     * Subscribes a new subscriber to the exchange service.
     *
     * @param exchangeServiceSubscriber the subscriber to be added
     */
    public void subscribe(ExchangeServiceSubscriber exchangeServiceSubscriber) {
        exchangeServiceSubscribers.add(exchangeServiceSubscriber);
    }

    /**
     * Unsubscribes an existing subscriber from the exchange service.
     *
     * @param exchangeServiceSubscriber the subscriber to be removed
     */
    public void unSubscribe(ExchangeServiceSubscriber exchangeServiceSubscriber) {
        exchangeServiceSubscribers.remove(exchangeServiceSubscriber);
    }

    /**
     * Notifies all subscribers with the current list of cryptocurrencies.
     */
    public void notifySubscribers() {
        exchangeServiceSubscribers.forEach(exchangeServiceSubscriber -> exchangeServiceSubscriber.update(cryptoCurrencies.keySet().stream().toList()));
    }

    /**
     * Returns the singleton instance of the ExchangeService.<br>
     * If the instance does not exist, it creates a new one.
     *
     * @return the singleton instance of ExchangeService
     */
    public static ExchangeService getInstance() {
        if (instance == null) {
            instance = new ExchangeService();
        }
        return instance;
    }

    /**
     * Returns an unmodifiable view of the available cryptocurrencies and their corresponding values.
     *
     * @return an unmodifiable map of available cryptocurrencies
     */
    public Map<CryptoCurrency, BigDecimal> getAvailableCryptoCurrencies() {
        return Collections.unmodifiableMap(cryptoCurrencies);
    }

    /**
     * Facilitates the purchase of a specified quantity of cryptocurrency from the exchange by a user.
     *
     * @param user the user making the purchase
     * @param cryptoCurrency the cryptocurrency to be purchased
     * @param quantity the quantity of cryptocurrency to be purchased
     * @throws CryptoCurrencyException if the exchange does not have enough of the specified cryptocurrency
     * @throws AccountServiceException if the user does not have enough funds to complete the transaction
     */
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

    /**
     * Subtracts a specified value from the current amount of a given cryptocurrency.
     *
     * @param cryptoCurrency the cryptocurrency to be updated
     * @param value the value to be subtracted
     */
    private void subtractCryptoCurrency(CryptoCurrency cryptoCurrency, BigDecimal value) {
        BigDecimal oldValue = cryptoCurrencies.get(cryptoCurrency);
        cryptoCurrencies.replace(cryptoCurrency, oldValue.subtract(value));
    }

    /**
     * Simulates the fluctuation of cryptocurrency values at random intervals.<br>
     * Updates the current value of each cryptocurrency and notifies subscribers of the changes.
     */
    public void fluctuateCryptoCurrencyValues() {
        Random random = new Random();
        cryptoCurrencies.forEach(((cryptoCurrency, quantity) -> {
            BigDecimal originalValue = cryptoCurrency.getOriginalValue();
            BigDecimal newValue;
            if (random.nextBoolean()){
                newValue = originalValue.add(BigDecimal.valueOf(random.nextDouble(originalValue.doubleValue() * 0.0012)));
            } else {
                newValue = originalValue.subtract(BigDecimal.valueOf(random.nextDouble(originalValue.doubleValue() * 0.0004)));
            }
            cryptoCurrency.updateCurrentValue(newValue);
        }));
        notifySubscribers();
    }
}