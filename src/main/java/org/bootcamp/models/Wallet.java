package org.bootcamp.models;

import org.bootcamp.services.CryptoCurrencyService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Wallet {
    private BigDecimal fiatMoney;
    private final Map<CryptoCurrency, BigDecimal> myCryptoCurrencies;

    public Wallet() {
        this.fiatMoney = BigDecimal.ZERO;
        this.myCryptoCurrencies = new HashMap<>();
        CryptoCurrencyService service = CryptoCurrencyService.getInstance();
        for (CryptoCurrency cryptoCurrency : service.getAvailableCryptoCurrencies().keySet()) {
            myCryptoCurrencies.put(cryptoCurrency, BigDecimal.ZERO);
        }
    }

    public BigDecimal getFiatMoney() {
        return fiatMoney;
    }

    public Map<CryptoCurrency, BigDecimal> getMyCryptoCurrencies() {
        return myCryptoCurrencies;
    }

    public void addFiatMoney(BigDecimal value) {
        fiatMoney = fiatMoney.add(value);
    }

    public void subtractFiatMoney(BigDecimal value) {
        fiatMoney = fiatMoney.subtract(value);
    }

    /**
     * Recarga la criptomoneda con el valor indicado
     * @param cryptoCurrency que se encuentra en la billetera
     * @param quantity que se añadirá a la billetera
     * @return true Si la criptomoneda estaba presente, false caso contrario
     */
    public boolean rechargeCryptoCurrency(CryptoCurrency cryptoCurrency, BigDecimal quantity) {
        BigDecimal currentQuantity = myCryptoCurrencies.get(cryptoCurrency);
        if (currentQuantity != null) {
            BigDecimal newQuantity = currentQuantity.add(quantity);
            myCryptoCurrencies.replace(cryptoCurrency, newQuantity);
        }
        return currentQuantity != null;
    }

    /**
     * Descuenta el valor indicado para la criptomoneda correspondiente
     * @param cryptoCurrency que se encuentra en la billetera
     * @param quantity que se descontará de la billetera
     * @return true Si la criptomoneda estaba presente, false caso contrario
     */
    public boolean subtractCryptoCurrency(CryptoCurrency cryptoCurrency, BigDecimal quantity) throws CryptoCurrencyException {
        BigDecimal currentQuantity = myCryptoCurrencies.get(cryptoCurrency);
        if (currentQuantity != null) {
            BigDecimal newQuantity = currentQuantity.subtract(quantity);
            if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
                throw new CryptoCurrencyException("Fondos Insuficientes para completar la transacción. Valores negativos no están permitidos");
            }
            myCryptoCurrencies.replace(cryptoCurrency, newQuantity);
        }
        return currentQuantity != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wallet wallet)) return false;
        return Objects.equals(fiatMoney, wallet.fiatMoney) && Objects.equals(myCryptoCurrencies, wallet.myCryptoCurrencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiatMoney, myCryptoCurrencies);
    }

    @Override
    public String toString() {
        StringBuilder message = new StringBuilder("Account Balance: $" + fiatMoney +
                "\nCryptoCurrencies:\n");
        for(CryptoCurrency cryptoCurrency : myCryptoCurrencies.keySet()) {
            message.append(cryptoCurrency.getShorthandSymbol()).append(": ").append(myCryptoCurrencies.get(cryptoCurrency)).append('\n');
        }
        return message.toString();
    }
}
