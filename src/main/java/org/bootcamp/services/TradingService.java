package org.bootcamp.services;

import org.bootcamp.models.*;
import org.bootcamp.views.TradingServiceSubscriber;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The trading service uses the observer pattern and the singleton pattern.
 * It hosts buy orders, searches for suitable buy and sell matches every 5 seconds.
 * When buy and sell matches are found, requests are processed and subscribers are notified.
 * @see MarketOrder
 */
public class TradingService {
    /**
     * List of market orders.
     */
    private final List<MarketOrder> orders;

    /**
     * Singleton instance of the TradingService.
     */
    private static TradingService instance;

    /**
     * List of subscribers to the trading service.
     */
    private final List<TradingServiceSubscriber> exchangeServiceSubscribers;

    /**
     * Private constructor to initialize the TradingService.<br>
     * Initializes the list of orders and subscribers, and schedules a task to match orders at fixed intervals.
     */
    private TradingService() {
        orders = new ArrayList<>();
        exchangeServiceSubscribers = new ArrayList<>();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable tarea = this::matchOrders;

        executor.scheduleAtFixedRate(tarea, 5, 5, TimeUnit.SECONDS);
    }

    /**
     * Subscribes a new subscriber to the trading service.
     *
     * @param exchangeServiceSubscriber The subscriber to be added.
     */
    public void subscribe(TradingServiceSubscriber exchangeServiceSubscriber) {
        exchangeServiceSubscribers.add(exchangeServiceSubscriber);
    }

    /**
     * Unsubscribes a subscriber from the trading service.
     *
     * @param exchangeServiceSubscriber The subscriber to be removed.
     */
    public void unSubscribe(TradingServiceSubscriber exchangeServiceSubscriber) {
        exchangeServiceSubscribers.remove(exchangeServiceSubscriber);
    }

    /**
     * Notifies all subscribers with the matched market orders.
     *
     * @param buyOrder The market order for buying.
     * @param sellingOrder The market order for selling.
     */
    public void notifySubscribers(MarketOrder buyOrder, MarketOrder sellingOrder) {
        exchangeServiceSubscribers.forEach(exchangeServiceSubscriber -> exchangeServiceSubscriber.update(buyOrder, sellingOrder));
    }


    /**
     * Returns the singleton instance of the TradingService.
     * If the instance is null, it initializes a new TradingService.
     *
     * @return The singleton instance of the TradingService.
     */
    public static TradingService getInstance() {
        if (instance == null) {
            instance = new TradingService();
        }
        return instance;
    }

    /**
     * Adds a new market order to the list of orders.
     *
     * @param order The market order to be added.
     */
    public void putOrder(MarketOrder order) {
        orders.add(order);
    }
    
    /**
     * Matches buy and sell orders from the list of orders.
     * Filters and sorts the orders by type, then iterates through buy orders to find matching sell orders.
     * If a match is found, processes the orders and notifies subscribers.
     * @see #processOrders(MarketOrder, MarketOrder)
     * @see #notifySubscribers(MarketOrder, MarketOrder) 
     */
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
                        match = true;
                        processOrders(buyOrder, sellingOrder);
                        notifySubscribers(buyOrder, sellingOrder);
                    }
                }
            }
        }
    }

    /**
     * Processes matched buy and sell orders.
     * Creates and records transactions for both the buyer and seller.
     * Updates the users' fiat money and cryptocurrency balances.
     * Removes the processed orders from the list of orders.
     *
     * @param buyOrder The market order for buying.
     * @param sellingOrder The market order for selling.
     * @see User#recordTransaction(Transaction)
     * @see User#depositFiatMoney(BigDecimal)
     * @see User#rechargeCryptoCurrency(CryptoCurrency, BigDecimal)
     * @see MarketOrder
     * @see Transaction
     */
    private void processOrders(MarketOrder buyOrder, MarketOrder sellingOrder) {
        Transaction buyerTransaction = new Transaction(TransactionAction.BUY, buyOrder.getCryptoCurrency(), buyOrder.getAmount(), sellingOrder.getPrice());
        Transaction sellerTransaction = new Transaction(TransactionAction.SELL, sellingOrder.getCryptoCurrency(), sellingOrder.getAmount(), sellingOrder.getPrice());

        sellingOrder.getUser().recordTransaction(sellerTransaction);
        buyOrder.getUser().recordTransaction(buyerTransaction);

        sellingOrder.getUser().depositFiatMoney(sellingOrder.getPrice());
        buyOrder.getUser().rechargeCryptoCurrency(buyOrder.getCryptoCurrency(), buyOrder.getAmount());

        if (sellingOrder.getPrice().compareTo(buyOrder.getPrice()) < 0) {
            buyOrder.getUser().depositFiatMoney(buyOrder.getPrice().subtract(sellingOrder.getPrice()));
        }

        orders.remove(buyOrder);
        orders.remove(sellingOrder);
    }
}