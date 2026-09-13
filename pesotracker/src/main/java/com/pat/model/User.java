package com.pat.model;

public class User {
    private String username;
    private int userId;

    public int getUserId(){
        return this.userId;
    }

   public void setUserId(Integer id){
        this.userId = id;
   }

   public String getUsername(){
    return this.username;
   }

   public void setUsername(String username){
        this.username = username;
   }


}
