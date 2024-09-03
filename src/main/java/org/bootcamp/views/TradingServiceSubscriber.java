package org.bootcamp.views;

import org.bootcamp.models.MarketOrder;

/**
 * Interface for subscribers to a trading service.<br>
 * Implementing classes should define the behavior for updating with new market orders.
 */
public interface TradingServiceSubscriber {
    /**
     * Updates with new market orders.
     *
     * @param buyOrder The market order for buying.
     * @param sellingOrder The market order for selling.
     */
    void update(MarketOrder buyOrder, MarketOrder sellingOrder);
}
