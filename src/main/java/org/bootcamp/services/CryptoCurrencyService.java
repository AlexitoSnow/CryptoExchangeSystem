package org.bootcamp.services;

import org.bootcamp.models.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CryptoCurrencyService {
    private final Map<CryptoCurrency, BigDecimal> cryptoCurrencies;
    private final List<MarketOrder> orders;
    private static CryptoCurrencyService instance;

    private CryptoCurrencyService() {
        cryptoCurrencies = new HashMap<>();
        orders = new ArrayList<>();
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

    public void putOrder(MarketOrder order) {
        orders.add(order);
    }

    public void matchOrders() {
        List<MarketOrder> sellingOrders = orders.stream().filter((order) -> order.getOrderType() == OrderType.SELLING).sorted().toList();
        List<MarketOrder> buyOrders = orders.stream().filter((order) -> order.getOrderType() == OrderType.BUY).sorted().toList();

        for (MarketOrder buyOrder : buyOrders) {
            boolean match = false;
            Iterator<MarketOrder> sellingOrdersIterator = sellingOrders.iterator();
            while (!match && sellingOrdersIterator.hasNext()){
                MarketOrder sellingOrder = sellingOrdersIterator.next();
                if (!buyOrder.getUser().equals(sellingOrder.getUser())){
                    boolean exactAmount = sellingOrder.getAmount().compareTo(buyOrder.getAmount()) == 0;
                    boolean sellingPriceMatches = sellingOrder.getPrice().compareTo(buyOrder.getPrice()) <= 0;
                    if (exactAmount && sellingPriceMatches) {
                        System.out.println("Matched found");
                        System.out.print(buyOrder.getOrderID());
                        System.out.println(" with " + sellingOrder.getOrderID());
                        match = true;
                        Transaction buyerTransaction = new Transaction(TransactionAction.BUY, buyOrder.getCryptoCurrency(), buyOrder.getAmount(), sellingOrder.getPrice());
                        Transaction sellerTransaction = new Transaction(TransactionAction.SELL, sellingOrder.getCryptoCurrency(), sellingOrder.getAmount(), sellingOrder.getPrice());
                        sellingOrder.getUser().recordTransaction(sellerTransaction);
                        buyOrder.getUser().recordTransaction(buyerTransaction);
                        sellingOrder.getUser().depositFiatMoney(sellingOrder.getPrice());
                        buyOrder.getUser().rechargeCryptoCurrency(buyOrder.getCryptoCurrency(), buyOrder.getAmount());
                        if (sellingOrder.getPrice().compareTo(buyOrder.getPrice()) < 0) {
                            buyOrder.getUser().depositFiatMoney(buyOrder.getPrice().subtract(sellingOrder.getPrice()));
                            System.out.println("Funds restored");
                        }
                        System.out.println("Transaction completed");
                        orders.remove(buyOrder);
                        orders.remove(sellingOrder);
                    }
                }
            }
        }


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