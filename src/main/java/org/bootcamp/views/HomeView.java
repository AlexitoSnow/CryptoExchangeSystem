package org.bootcamp.views;

import org.bootcamp.models.Wallet;
import java.math.BigDecimal;

public class HomeView extends View {

    /**
     * Options:
     * 1. Depositar dinero
     * 2. Revisar billetera virtual
     * 3. Comprar por intercambio
     * 4. Enviar orden de compra
     * 5. Enviar orden de venta
     * 6. Cerrar sesión
     * @return opción escogida
     */
    public int getUserChoice() {
        showInfo("Choose one option to continue:");
        showInfo("1. Deposit money");
        showInfo("2. View wallet balance");
        showInfo("3. Buy from exchange");
        showInfo("4. Place buy order");
        showInfo("5. Place sell order");
        showInfo("6. Logout");
        System.out.print("Enter your choice: ");
        return getChoice();
    }

    public void showWallet(Wallet wallet) {
        showInfo("My Wallet");
        showSuccessMessage(wallet.toString());
    }

    public BigDecimal getAmountMoneyInput() {
        requestMessage("Amount: $");
        return getBigDecimal();
    }

}
