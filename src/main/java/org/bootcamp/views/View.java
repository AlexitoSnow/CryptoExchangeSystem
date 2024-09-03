package org.bootcamp.views;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class View {
    /**
     * ANSI escape code for displaying error messages in red.
     */
    protected static final String ERROR_CODE = "\u001B[31m";
    /**
     * ANSI escape code for resetting to the default color.
     */
    protected static final String DEFAULT_CODE = "\u001B[0m";
    /**
     * ANSI escape code for displaying success messages in green.
     */
    protected static final String SUCCESS_CODE = "\u001B[32m";
    /**
     * ANSI escape code for displaying informational messages in blue.
     */
    protected static final String INFO_CODE = "\u001B[34m";
    /**
     * Constant representing an invalid choice.
     */
    public static final int INVALID_CHOICE = -1;

    /**
     * Scanner object for reading user input from the console.
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays an error message in red.
     *
     * @param errorMessage The error message to be displayed.
     */
    public void showError(String errorMessage) {
        System.out.println(ERROR_CODE + errorMessage + DEFAULT_CODE);
    }

    /**
     * Displays an informational message in blue.
     *
     * @param message The informational message to be displayed.
     */
    public void showInfo(String message) {
        System.out.println(INFO_CODE + message + DEFAULT_CODE);
    }

    /**
     * Prompts the user with a message in blue.
     *
     * @param message The message to be displayed.
     */
    public void requestMessage(String message) {
        System.out.print(INFO_CODE + message + DEFAULT_CODE);
    }

    /**
     * Displays a success message in green.
     *
     * @param message The success message to be displayed.
     */
    public void showSuccessMessage(String message) {
        System.out.println(SUCCESS_CODE + message + DEFAULT_CODE);
    }

    /**
     * Closes the scanner object.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Reads a BigDecimal value from the user input.
     * If the input is not a valid BigDecimal, returns BigDecimal.ZERO.
     *
     * @return The BigDecimal value entered by the user, or <code>BigDecimal.ZERO</code> if the input is invalid.
     */
    public BigDecimal getBigDecimal() {
        try {
            return scanner.nextBigDecimal();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return BigDecimal.ZERO;
        }
    }

    /**
     * Reads an integer choice from the user input.
     * If the input is not a valid integer, returns INVALID_CHOICE.
     *
     * @return The integer choice entered by the user, or INVALID_CHOICE if the input is invalid.
     */
    public Integer getChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            return INVALID_CHOICE;
        } finally {
            scanner.nextLine();
        }
    }

    /**
     * Reads a line of text from the user input.
     *
     * @return The line of text entered by the user.
     */
    public String getString() {
        return scanner.nextLine();
    }

    /**
     * Reads a word or a line of text from the user input based on the parameter.
     *
     * @param singleWord If true, reads a single word; otherwise, reads a line of text.
     * @return The word or line of text entered by the user.
     */
    public String getString(boolean singleWord) {
        return singleWord ? scanner.next() : getString();
    }

}