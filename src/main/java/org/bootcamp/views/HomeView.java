package org.bootcamp.views;

import org.bootcamp.models.Wallet;
import java.math.BigDecimal;

public class HomeView extends View {

    /**
     * Options:
     * 1. Depositar dinero
     * 2. Revisar billetera virtual
     * 3. Revisar historial de transacciones
     * 4. Ir al mercado
     * 5. Cerrar sesión
     * @return opción escogida
     */
    public int getUserChoice() {
        showInfo("Choose one option to continue:");
        showInfo("1. Deposit money");
        showInfo("2. View wallet balance");
        showInfo("3. View transaction history");
        showInfo("4. Go to market");
        showInfo("5. Logout");
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
