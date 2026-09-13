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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;
import com.pat.App;
import com.pat.controller.NavigationController;
import com.pat.controller.UserController;
import com.pat.model.User;
import com.pat.utils.FontLoader;
import com.pat.view.components.TitlePanel;

public class LoginPanel extends JPanel {
    public LoginPanel(App app, NavigationController navController, UserController userController) {
        setLayout(new BorderLayout());


        TitlePanel leftPane = new TitlePanel();
        JPanel rightPane = new JPanel();
        rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
        rightPane.setBackground(Color.decode("#9AC6E5"));

        Dimension inputDim = new Dimension(400, 60);
        Dimension invalidDim = new Dimension(400, 20);
        Dimension buttonDim = new Dimension(300, 30);

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(FontLoader.TITLE_FONT);
        JTextField usernameField = new JTextField();
        JLabel invalidUsername = new JLabel("");
        JLabel invalidPassword = new JLabel("");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login Account");
        JButton returnButton = new JButton("Return to Main");
        JButton signUpButton = new JButton("Sign Up Instead!");

        usernameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Username");
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Password");

        usernameField.putClientProperty(FlatClientProperties.STYLE, "font: 24 'Border Wall'");

        usernameField.setPreferredSize(inputDim);
        usernameField.setMaximumSize(inputDim);

        passwordField.setPreferredSize(inputDim);
        passwordField.setMaximumSize(inputDim);

        invalidUsername.setPreferredSize(invalidDim);
        invalidUsername.setMaximumSize(invalidDim);

        invalidPassword.setPreferredSize(invalidDim);
        invalidPassword.setMaximumSize(invalidDim);

        loginButton.setPreferredSize(buttonDim);
        loginButton.setMaximumSize(buttonDim);

        signUpButton.setPreferredSize(buttonDim);
        signUpButton.setMaximumSize(buttonDim);

        returnButton.setPreferredSize(buttonDim);
        returnButton.setMaximumSize(buttonDim);

        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        invalidPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        invalidUsername.setAlignmentX(Component.CENTER_ALIGNMENT);



        rightPane.add(Box.createVerticalGlue());
        rightPane.add(loginLabel);
        rightPane.add(Box.createVerticalStrut(15));
        rightPane.add(usernameField);
        rightPane.add(invalidUsername);
        //rightPane.add(Box.createVerticalStrut(15));
        rightPane.add(passwordField);
        //rightPane.add(Box.createVerticalStrut(30));
        rightPane.add(invalidPassword);
        rightPane.add(loginButton);
        rightPane.add(Box.createVerticalStrut(15));
        rightPane.add(returnButton);
        rightPane.add(Box.createVerticalStrut(15));
        rightPane.add(signUpButton);

        rightPane.add(Box.createVerticalGlue());

        add(leftPane, BorderLayout.WEST);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            char[] password = passwordField.getPassword();
            String passString = String.valueOf(password);  
            User user = userController.loginUser(username, passString);
            if(user.getUserId() > -1){
                usernameField.setText("");
                passwordField.setText("");
        
                app.setCurrentUser(user);
                app.showMain();
            }
            if(user.getUserId() == -1){
                invalidUsername.setText("◉ INVALID USERNAME");
                invalidUsername.setForeground(Color.RED);
                invalidPassword.setText("");
            }
            if(user.getUserId() == -2){
                invalidPassword.setText("◉ INVALID PASSWORD");
                invalidPassword.setForeground(Color.RED);
                invalidUsername.setText("");
            }
        });
        returnButton.addActionListener(e -> navController.showScreen("INITIAL"));
        signUpButton.addActionListener(e -> navController.showScreen("SIGNUP"));

        rightPane.add(Box.createVerticalGlue());
        add(rightPane, BorderLayout.CENTER);
    }
}
