package com.pat.view.screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JPanel;

import com.pat.App;
import com.pat.controller.LandingNavigationController;
import com.pat.controller.NavigationController;
import com.pat.controller.UserController;
import com.pat.view.components.NavPanel;
public class LandingPanel extends JPanel {
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private final LandingNavigationController landingNavController;

    public LandingPanel(App app, NavigationController navController, UserController userController) {
        setLayout(new BorderLayout());
        landingNavController = new LandingNavigationController(this);
        NavPanel nav = new NavPanel(app,navController, landingNavController, this);
        DashboardFrame dashboardPanel = new DashboardFrame(app.getCurrentUser());
        ExpenseFrame expensePanel = new ExpenseFrame(app.getCurrentUser(), dashboardPanel);
        BudgetPanel budgetPanel = new BudgetPanel(app.getCurrentUser(), expensePanel, dashboardPanel);
        GoalFrame goalPanel = new GoalFrame(app.getCurrentUser());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(budgetPanel, "BUDGET");
        cardPanel.add(expensePanel, "EXPENSE");
        cardPanel.add(goalPanel, "GOALS");
        cardPanel.add(dashboardPanel, "DASHBOARD");
        
        add(nav, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
    }

    public void showScreen(String pageName) {
        cardLayout.show(cardPanel, pageName);
    }
}
