package org.bootcamp.controllers;

import org.bootcamp.Router;

public interface Controller {
    Router router = Router.getInstance();
    void run();
}
