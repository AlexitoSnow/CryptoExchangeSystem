package org.bootcamp.views;

import org.bootcamp.models.MarketOrder;

public interface TradingServiceSubscriber {
    void update(MarketOrder buyOrder, MarketOrder sellingOrder);
}
