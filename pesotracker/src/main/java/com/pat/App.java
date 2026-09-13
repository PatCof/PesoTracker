package com.pat;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;
import com.pat.controller.NavigationController;
import com.pat.controller.UserController;
import com.pat.database.DatabaseConnection;
import com.pat.database.DatabaseInitializer;
import com.pat.model.User;
import com.pat.utils.FontLoader;
import com.pat.view.screens.InitialPanel;
import com.pat.view.screens.LandingPanel;
import com.pat.view.screens.LoginPanel;
import com.pat.view.screens.SignUpPanel;

public class App extends JFrame {
    private final JPanel cardPanel;
    private final CardLayout cl;
    private final NavigationController navController;
    private final UserController userController;
    private User currentUser;

    public App() {
        currentUser = new User();
        navController = new NavigationController(this);
        userController = new UserController(currentUser);
        InitialPanel initialPanel = new InitialPanel(navController);
        SignUpPanel signUp = new SignUpPanel(navController, userController);
        LoginPanel login = new LoginPanel(this,navController, userController);

        cl = new CardLayout();
        cardPanel = new JPanel(cl);
        cardPanel.add(initialPanel, "INITIAL");
        cardPanel.add(signUp, "SIGNUP");
        cardPanel.add(login, "LOGIN");

        add(cardPanel);

        setResizable(false);
        setSize(800, 600);
        setPreferredSize(new Dimension(800, 600));
        setTitle("Peso Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void switchScreen(String pageName) {
        cl.show(cardPanel, pageName);
    }

    public void logoutUser(){
        this.currentUser = null;
    }

    public void showMain(){
        LandingPanel budget = new LandingPanel(this, navController, userController);
        cardPanel.add(budget, "MAIN");
        cl.show(cardPanel, "MAIN");
    }


    public static void main(String[] args) {
        try{
            FlatLightLaf.setup();
            FontLoader.loadFonts();
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Button.font", FontLoader.MAIN_FONT);
            UIManager.put("PasswordField.font", FontLoader.MAIN_FONT);
            UIManager.put("Button.arc", 16);
            UIManager.put("Button.color", "#00171F");
            UIManager.put("PasswordField.showRevealButton", true);
            UIManager.put("TextComponent.arc", 16);
            DatabaseConnection.connect();
            DatabaseInitializer.initializer();
        }catch(UnsupportedLookAndFeelException e){
            System.err.println("Failed to initialize Flatlaf");
        }

        SwingUtilities.invokeLater(() -> {
            new App();
        });
    }
}
