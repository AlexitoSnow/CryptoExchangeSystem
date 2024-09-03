package org.bootcamp;

import org.bootcamp.controllers.Controller;
import org.bootcamp.controllers.HomeController;
import org.bootcamp.controllers.RootController;
import org.bootcamp.controllers.MarketController;

/**
 * The Router class uses the strategy pattern and singleton
 * to establish navigability in the application.<br>
 * Consider using the constants attributes of the class
 * to navigate between routes using the navigateTo() method,
 * which is responsible for calling to execute() the view
 * controller automatically.
 * @see #navigateTo(String)
 * @see #execute()
 */
public class Router {
    /**
     * Pass it to the navigateTo() method to go to the HomeController
     * @see #navigateTo(String)
     * @see HomeController
     */
    public static final String HOME = "home";
    /**
     * Pass it to the navigateTo() method to go to the RootController
     * @see #navigateTo(String)
     * @see RootController
     */
    public static final String ROOT = "root";
    /**
     * Pass it to the navigateTo() method to go to the MarketController
     * @see #navigateTo(String)
     * @see MarketController
     */
    public static final String MARKET = "market";
    /**
     * Represents the current strategy of the strategy pattern.<br>
     * Allows you to execute the controller when you navigate to a route.
     * @see Controller
     * @see #navigateTo(String)
     */
    private Controller controller;
    /**
     * Represents the instance of the singleton pattern
     */
    public static Router instance;

    /**
     * The constructor is hidden to follow the singleton pattern
     */
    private Router() {}

    /**
     * Router uses the singleton pattern
     * @return the singleton instance
     */
    public static Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }

    /**
     * Navigates to the specified route
     * @param route If the route is not in the list, navigates to the default route
     * @see #execute()
     * @see HomeController
     * @see MarketController
     * @see RootController
     */
    public void navigateTo(String route) {
        switch (route) {
            case HOME: controller = HomeController.getInstance();
            break;
            case MARKET: controller = MarketController.getInstance();
            break;
            case ROOT:
            default: controller = new RootController();
        }
        execute();
    }

    /**
     * Calls to execute the specified controller according
     * to the strategy pattern
     * @see #navigateTo(String)
     */
    public void execute() {
        controller.run();
    }
}
