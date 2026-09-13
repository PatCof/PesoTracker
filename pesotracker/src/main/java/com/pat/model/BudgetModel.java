package com.pat.model;
    
public class BudgetModel {
    private int id;
    private int user_id;
    private double amount;
    private String category;
    private String date;


    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setUserId(int user_id){
        this.user_id = user_id;
    }

    public int getUserId(){
        return user_id;
    }

    public void setAmount(double amount){
        this.amount = amount;
    }

    public double getAmount(){
        return amount;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getCategory(){
        return category;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

}
