package com.pat.controller;

import org.mindrot.jbcrypt.BCrypt;

import com.pat.model.User;
import com.pat.repository.UserRepository;

public class UserController {
    private User user;
    
    public UserController(User user){
        this.user = user;
    }

    public int checkUserExists(String username){
        UserRepository userRepo = new UserRepository();
        int userId = userRepo.checkUser(username);
        return userId;
    }

    public User loginUser(String username, String password){
        UserRepository userRepo = new UserRepository();
        String hashedPassword = hashPassword(password);
        int userId = userRepo.selectUser(username, hashedPassword);
        if (userId > -1){
            this.user.setUsername(username);
        }
        this.user.setUserId(userId);
        return this.user;
    }


    public void signUpUser(String username, String password){
        UserRepository userRepo = new UserRepository();
        String hashedPassword = hashPassword(password);
        
        userRepo.addUser(username, hashedPassword);
    }

    public String hashPassword(String password){
        int logRounds = 12;
        String salt = BCrypt.gensalt(logRounds);
        return BCrypt.hashpw(password, salt);
    }
}
