package com.pat.model;

public class ExpenseModel {
    private int id;
    private int user_id;
    private double amount;
    private double budgetedAmount;
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
    
    public void setBudgetedAmount(double budgetedAmount){
        this.budgetedAmount = budgetedAmount;
    }

    public double getBudgetedAmount(){
        return budgetedAmount;
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
