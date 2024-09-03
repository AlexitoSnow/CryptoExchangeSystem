package org.bootcamp.views;

import org.bootcamp.models.CryptoCurrency;

import java.util.List;

public interface ExchangeServiceSubscriber {
    void update(List<CryptoCurrency> cryptoCurrencies);
}
