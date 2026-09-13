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
import com.pat.controller.NavigationController;
import com.pat.controller.UserController;
import com.pat.view.components.TitlePanel;
import com.pat.utils.FontLoader;

public class SignUpPanel extends JPanel {
    public SignUpPanel(NavigationController navController, UserController userController) {
        setLayout(new BorderLayout());

        Dimension inputDim = new Dimension(400, 60);
        Dimension buttonDim = new Dimension(300, 30);
        Dimension labelDim = new Dimension(200, 50);

        setBackground(Color.decode("#9AC6E5"));
        TitlePanel leftPane = new TitlePanel();
        JPanel rightPane = new JPanel();
        rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
        rightPane.setBackground(Color.decode("#9AC6E5"));

        JLabel loginLabel = new JLabel("SIGN UP");
        loginLabel.setFont(FontLoader.TITLE_FONT);
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        JButton signUpButton = new JButton("Sign Up!");
        JButton returnButton = new JButton("Return to Main");
        JButton loginButton = new JButton("Login Instead");
        JLabel invalidUsername = new JLabel("");
        JLabel invalidPassword = new JLabel("");

        usernameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Username");
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Password");
        confirmPasswordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Confirm Password");

        loginLabel.setPreferredSize(labelDim);
        loginLabel.setMaximumSize(labelDim);

        usernameField.setPreferredSize(inputDim);
        usernameField.setMaximumSize(inputDim);

        passwordField.setPreferredSize(inputDim);
        passwordField.setMaximumSize(inputDim);

        confirmPasswordField.setPreferredSize(inputDim);
        confirmPasswordField.setMaximumSize(inputDim);

        signUpButton.setPreferredSize(buttonDim);
        signUpButton.setMaximumSize(buttonDim);

        returnButton.setPreferredSize(buttonDim);
        returnButton.setMaximumSize(buttonDim);

        loginButton.setPreferredSize(buttonDim);
        loginButton.setMaximumSize(buttonDim);

        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        invalidUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        invalidPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField.putClientProperty(FlatClientProperties.STYLE, "arc: 16");
        
        usernameField.putClientProperty(FlatClientProperties.STYLE, "font: 24 'Border Wall'");

        rightPane.add(Box.createVerticalGlue());
        rightPane.add(loginLabel);
        rightPane.add(Box.createVerticalStrut(15));
        rightPane.add(usernameField);
        rightPane.add(invalidUsername);
        rightPane.add(Box.createVerticalStrut(15));
        rightPane.add(passwordField);
        rightPane.add(Box.createVerticalStrut(15));
        rightPane.add(confirmPasswordField);
        rightPane.add(invalidPassword);
        rightPane.add(Box.createVerticalStrut(30));
        rightPane.add(signUpButton);
        rightPane.add(Box.createVerticalStrut(10));
        rightPane.add(returnButton);
        rightPane.add(Box.createVerticalStrut(10));
        rightPane.add(loginButton);
        rightPane.add(Box.createVerticalStrut(10));
        rightPane.add(Box.createVerticalGlue());

        add(leftPane, BorderLayout.WEST);

        signUpButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            char[] password = passwordField.getPassword();
            char[] confirmPassword = confirmPasswordField.getPassword();

            String passString = String.valueOf(password).trim();
            String confirmString = String.valueOf(confirmPassword).trim();

            int userId = userController.checkUserExists(username);
            if(userId == -1 && !username.isEmpty()){
                if(!passString.isEmpty() && passString.equals(confirmString)){
                    usernameField.setText("");
                    passwordField.setText("");
                    confirmPasswordField.setText("");

                    userController.signUpUser(username, passString);
                    navController.showScreen("LOGIN");
                }else{
                    invalidPassword.setText("◉ PASSWORDS DO NOT MATCH!");
                    invalidPassword.setForeground(Color.RED);
                    invalidUsername.setText("");
                }
            }else{
                invalidUsername.setText("◉ Invalid Username!");
                invalidUsername.setForeground(Color.RED);
                invalidPassword.setText("");
            }
        });
        returnButton.addActionListener(e -> navController.showScreen("INITIAL"));
        loginButton.addActionListener(e -> navController.showScreen("LOGIN"));

        rightPane.add(Box.createVerticalGlue());
        add(rightPane, BorderLayout.CENTER);

    }
}
