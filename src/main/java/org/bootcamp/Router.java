package org.bootcamp;

import org.bootcamp.controllers.HomeController;
import org.bootcamp.controllers.RootController;
import org.bootcamp.controllers.MarketController;
import java.util.Map;

public abstract class Router {
    public static final String HOME = "home";
    public static final String ROOT = "root";
    public static final String MARKET = "market";

    public static void navigateTo(String route, Map<String, Object> parameters) {
        switch (route) {
            case HOME: new HomeController().run();
            break;
            case MARKET: new MarketController((Integer) parameters.get("marketAction")).run();
            case ROOT:
            default: new RootController().run();
        }
    }

    public static void navigateTo(String route) {
        navigateTo(route, null);
    }
}
