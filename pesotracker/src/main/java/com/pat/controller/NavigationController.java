package com.pat.controller;

import com.pat.App;

public class NavigationController {
    private final App app;
    public NavigationController(App app){
        this.app = app;
    }

    public void showScreen(String pageName){
        app.switchScreen(pageName);
    }

}
