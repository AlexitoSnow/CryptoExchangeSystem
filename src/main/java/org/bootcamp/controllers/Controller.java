package org.bootcamp.controllers;

import org.bootcamp.Router;

/**
 * Interface representing a generic controller in the system.
 * Provides a method to run the controller and a router for navigation.
 */
public interface Controller {
    /**
     * Router instance allows controllers to implement navigation quickly
      */
    Router router = Router.getInstance();
    /**
     * Runs the main loop of the controller, handling user interactions and actions.
     */
    void run();
}
