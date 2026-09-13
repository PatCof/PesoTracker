package com.pat.view.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.pat.App;
import com.pat.controller.LandingNavigationController;
import com.pat.controller.NavigationController;
import com.pat.view.screens.LandingPanel;

public class NavPanel extends JPanel {
    private final NavigationController navController; 
    public NavPanel(App app, NavigationController navController, LandingNavigationController landingNavController, LandingPanel lf) {
        this.navController = navController;
        JLabel mainLabel = new JLabel("<html><body style='text-align: center; font-family: Call of Ops Duty II; font-size: 32px; color: #FFFFFF '>Peso <br>Tracker</br></body></html>", SwingConstants.CENTER);
        JLabel userLabel = new JLabel("<html><body style='text-align: center; font-family: Call of Ops Duty II; font-size: 16px; color: #FFFFFF'>Welcome <br>" + app.getCurrentUser().getUsername() + "</br></body></html>", SwingConstants.CENTER);
        JButton budgetButton = new JButton("Budget Tracker");
        JButton goalsButton = new JButton("Goals");
        JButton dashboardButton = new JButton("Dashboard");
        JButton transactionsButton = new JButton("Expense Tracker");
        JButton logoutButton = new JButton("Log Out");



        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.decode("#003459"));
        add(Box.createVerticalGlue());

        mainLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(mainLabel);
        add(Box.createVerticalStrut(25));

        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(userLabel);
        add(Box.createVerticalStrut(10));

        budgetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(budgetButton);
        add(Box.createVerticalStrut(10));

        transactionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(transactionsButton);
        add(Box.createVerticalStrut(10));

        goalsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(goalsButton);
        add(Box.createVerticalStrut(10));

        dashboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(dashboardButton);
        add(Box.createVerticalStrut(40));

        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(logoutButton);

        add(Box.createVerticalGlue());


        logoutButton.addActionListener(e -> {
            var option = JOptionPane.showConfirmDialog(null, "Log out?", "Select an option!" ,JOptionPane.YES_NO_OPTION);
            if(option == 0){
                app.logoutUser();
                navController.showScreen("INITIAL");
    }});
            
        budgetButton.addActionListener(e -> landingNavController.showScreen("BUDGET"));
        goalsButton.addActionListener(e -> landingNavController.showScreen("GOALS"));
        dashboardButton.addActionListener(e -> landingNavController.showScreen("DASHBOARD"));
        transactionsButton.addActionListener(e -> landingNavController.showScreen("EXPENSE"));

        logoutButton.addActionListener(e -> {
        });
        setSize(240, 600);
        setPreferredSize(new Dimension(240, 600));
    }
}
