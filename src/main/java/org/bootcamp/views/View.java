package org.bootcamp.views;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class View {
    protected static final String ERROR_CODE = "\u001B[31m";
    protected static final String DEFAULT_CODE = "\u001B[0m";
    protected static final String SUCCESS_CODE = "\u001B[32m";
    protected static final String INFO_CODE = "\u001B[34m";
    public static final int INVALID_CHOICE = -1;

    private final Scanner scanner = new Scanner(System.in);

    public void showError(String errorMessage) {
        System.out.println(ERROR_CODE + errorMessage + DEFAULT_CODE);
    }

    public void showInfo(String message) {
        System.out.println(INFO_CODE + message + DEFAULT_CODE);
    }

    public void requestMessage(String message) {
        System.out.print(INFO_CODE + message + DEFAULT_CODE);
    }

    public void showSuccessMessage(String message) {
        System.out.println(SUCCESS_CODE + message + DEFAULT_CODE);
    }

    public void close() {
        scanner.close();
    }

    public BigDecimal getBigDecimal() {
        try {
            return scanner.nextBigDecimal();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return BigDecimal.ZERO;
        }
    }

    public Integer getChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return INVALID_CHOICE;
        }
    }

    public String getString() {
        return scanner.nextLine();
    }

    public String getString(boolean singleWord) {
        return singleWord ? scanner.next() : getString();
    }

}