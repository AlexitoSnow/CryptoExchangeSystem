package org.bootcamp.views;

import org.bootcamp.models.CryptoCurrency;
import org.bootcamp.models.Wallet;

import java.util.InputMismatchException;
import java.util.Map;

public class HomeView extends View {

    /**
     * Options:
     * 1. Depositar dinero
     * 2. Revisar billetera virtual
     * 3. Comprar por intercambio
     * 4. Enviar orden de compra
     * 5. Cerrar sesión
     * @return opción escogida
     */
    public int getUserChoice() {
        showInfo("Choose one option to continue:");
        showInfo("1. Deposit money");
        showInfo("2. View wallet balance");
        showInfo("3. Buy from exchange");
        showInfo("4. Place sell order");
        showInfo("5. Logout");
        System.out.print("Enter your choice: ");
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return INVALID_CHOICE;
        }
    }

    public void showWallet(Wallet wallet) {
        showInfo("My Wallet");
        showSuccessMessage(wallet.toString());
    }
}
