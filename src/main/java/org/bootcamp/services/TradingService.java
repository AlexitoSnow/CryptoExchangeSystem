package org.bootcamp.services;

import org.bootcamp.models.*;
import org.bootcamp.views.TradingServiceSubscriber;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TradingService {
    private final List<MarketOrder> orders;
    private static TradingService instance;
    private final List<TradingServiceSubscriber> exchangeServiceSubscribers;

    private TradingService() {
        orders = new ArrayList<>();
        exchangeServiceSubscribers = new ArrayList<>();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable tarea = this::matchOrders;

        executor.scheduleAtFixedRate(tarea, 5, 5, TimeUnit.SECONDS);
    }

    public void subscribe(TradingServiceSubscriber exchangeServiceSubscriber) {
        exchangeServiceSubscribers.add(exchangeServiceSubscriber);
    }

    public void unSubscribe(TradingServiceSubscriber exchangeServiceSubscriber) {
        exchangeServiceSubscribers.remove(exchangeServiceSubscriber);
    }

    public void notifySubscribers(MarketOrder buyOrder, MarketOrder sellingOrder) {
        exchangeServiceSubscribers.forEach(exchangeServiceSubscriber -> exchangeServiceSubscriber.update(buyOrder, sellingOrder));
    }

    public static TradingService getInstance() {
        if (instance == null) {
            instance = new TradingService();
        }
        return instance;
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
                        match = true;
                        processOrders(buyOrder, sellingOrder);
                        notifySubscribers(buyOrder, sellingOrder);
                    }
                }
            }
        }
    }

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