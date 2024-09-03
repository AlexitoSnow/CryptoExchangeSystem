package org.bootcamp.views;

import org.bootcamp.models.CryptoCurrency;
import java.util.List;

/**
 * Interface for subscribers to an exchange service.
 * Implementing classes should define the behavior for updating the list of cryptocurrencies.
 */
public interface ExchangeServiceSubscriber {

    /**
     * Updates the list of cryptocurrencies.
     *
     * @param cryptoCurrencies The list of cryptocurrencies to update.
     */
    void update(List<CryptoCurrency> cryptoCurrencies);
}
