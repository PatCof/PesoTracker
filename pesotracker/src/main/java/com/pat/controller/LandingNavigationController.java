package com.pat.controller;

import com.pat.view.screens.LandingPanel;

public class LandingNavigationController {
    private final LandingPanel landingPanel;
    
    public LandingNavigationController(LandingPanel landingPanel){
        this.landingPanel = landingPanel;
    }

    public void showScreen(String pageName){
        landingPanel.showScreen(pageName);
    }

}
