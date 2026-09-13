package com.pat.view.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;
import com.pat.controller.NavigationController;
import com.pat.utils.FontLoader;

public class InitialPanel extends JPanel {

    public InitialPanel(NavigationController navController) {
        setLayout(new BorderLayout());
        JPanel leftPane = new JPanel();
        
        Dimension dim = new Dimension(300,60);

        JPanel rightPane = new JPanel();
        JLabel mainLabel = new JLabel("<html><div style='text-align: center;'>Peso<br>Tracker</div><html>", SwingConstants.CENTER);
        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");
        JButton exitButton = new JButton("Exit");

        mainLabel.setForeground(Color.decode("#00171F"));
        mainLabel.setFont(FontLoader.TITLE_FONT);


        leftPane.setBackground(Color.decode("#003459"));
        leftPane.setSize(240, 600);
        leftPane.setPreferredSize(new Dimension(240, 600));

        loginButton.setPreferredSize(dim);
        signUpButton.setPreferredSize(dim);
        exitButton.setSize(200, 60);
        exitButton.setPreferredSize(new Dimension(200, 60));

        loginButton.setMaximumSize(dim);
        signUpButton.setMaximumSize(dim);
        exitButton.setMaximumSize(new Dimension(200, 60));

        loginButton.setBackground(Color.decode("#3479A9"));
        signUpButton.setBackground(Color.decode("#B3DEFC"));
        exitButton.setBackground(Color.decode("#B3DEFC"));


        rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
        rightPane.add(Box.createVerticalGlue());

        mainLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainLabel.setAlignmentY(Component.CENTER_ALIGNMENT);


        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        rightPane.setBackground(Color.decode("#9AC6E5"));

        loginButton.putClientProperty(FlatClientProperties.BUTTON_TYPE,FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        signUpButton.putClientProperty(FlatClientProperties.BUTTON_TYPE,FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        exitButton.putClientProperty(FlatClientProperties.BUTTON_TYPE,FlatClientProperties.BUTTON_TYPE_BORDERLESS);

        rightPane.add(mainLabel);
        rightPane.add(Box.createVerticalStrut(30));
        rightPane.add(loginButton);
        rightPane.add(Box.createVerticalStrut(30));
        rightPane.add(signUpButton);
        rightPane.add(Box.createVerticalStrut(30));
        rightPane.add(exitButton);

        add(leftPane, BorderLayout.WEST);
        rightPane.add(Box.createVerticalGlue());
        add(rightPane, BorderLayout.CENTER);

        loginButton.addActionListener(e -> navController.showScreen("LOGIN"));
        signUpButton.addActionListener(e -> navController.showScreen("SIGNUP"));
        exitButton.addActionListener(e -> System.exit(0));
    }
}