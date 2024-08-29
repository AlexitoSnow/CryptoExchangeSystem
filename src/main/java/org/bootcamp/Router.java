package org.bootcamp;

import org.bootcamp.controllers.HomeController;
import org.bootcamp.controllers.RootController;

public abstract class Router {
    public static final String HOME = "home";
    public static final String ROOT = "root";

    public static void navigateTo(String route) {
        switch (route) {
            case HOME: new HomeController().run();
            break;
            case ROOT:
            default: new RootController().run();
        }
    }
}
