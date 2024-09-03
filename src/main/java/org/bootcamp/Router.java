package org.bootcamp;

import org.bootcamp.controllers.Controller;
import org.bootcamp.controllers.HomeController;
import org.bootcamp.controllers.RootController;
import org.bootcamp.controllers.MarketController;

public class Router {
    public static final String HOME = "home";
    public static final String ROOT = "root";
    public static final String MARKET = "market";
    private Controller controller;
    public static Router instance;

    private Router() {}

    public static Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }

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

    public void execute() {
        controller.run();
    }
}
