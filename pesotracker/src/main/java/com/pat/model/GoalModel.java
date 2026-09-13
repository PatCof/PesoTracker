package com.pat.model;

public class GoalModel {
    private int id;
    private double savings;
    private double end;
    private String category;


    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }
    public double getSavings(){
        return this.savings;
    }
    public void setSavings(double savings){
        this.savings = savings;
    }
    public double getEnd(){
        return this.end;
    }
    public void setEnd(double end){
        this.end = end;
    }

    public String getCategory(){
        return this.category;
    }
    public void setCategory(String category){
        this.category = category;
    }



}
