package org.bootcamp.views;

import org.bootcamp.models.Wallet;
import java.math.BigDecimal;

public class HomeView extends View {

    /**
     * Options:
     * 1. Depositar dinero
     * 2. Revisar billetera virtual
     * 3. Revisar historial de transacciones
     * 4. Comprar por intercambio
     * 5. Enviar orden de compra
     * 6. Enviar orden de venta
     * 7. Cerrar sesión
     * @return opción escogida
     */
    public int getUserChoice() {
        showInfo("Choose one option to continue:");
        showInfo("1. Deposit money");
        showInfo("2. View wallet balance");
        showInfo("3. View transaction history");
        showInfo("4. Buy from exchange");
        showInfo("5. Place buy order");
        showInfo("6. Place sell order");
        showInfo("7. Logout");
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
