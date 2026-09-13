package com.pat.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.pat.model.GoalModel;
import com.pat.model.User;
import com.pat.repository.GoalRepository;

public class GoalController {
    private final User user;
    private final GoalRepository gr;

    public GoalController(User currentUser){
        this.user = currentUser;
        this.gr = new GoalRepository();
    }

    public void  saveGoalToDatabase(double savings, double end, String category){
        gr.setGoal(savings, end, category, this.user.getUserId());
    }

    public void deleteGoalInDatabase(int id){
        gr.deleteGoal(id, this.user.getUserId());
    }

    public ArrayList<GoalModel> getGoalFromDatabase(){
        ArrayList<GoalModel> gm = new ArrayList<>();
        ResultSet rs = gr.getGoal(this.user.getUserId());
        if(rs != null){
            try{
                while(rs.next()){

                    GoalModel goalModel = new GoalModel();
                    goalModel.setCategory(rs.getString("category"));
                    goalModel.setEnd(rs.getDouble("end"));
                    goalModel.setSavings(rs.getDouble("savings"));
                    goalModel.setId(rs.getInt("id"));
                    gm.add(goalModel);
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }        
        return gm;
    }

    public void setGoal(int savings, int end, String category){
        gr.setGoal(savings, end, category, this.user.getUserId());
    }

    public void updateGoalToDatabase(int id, String category, double savings, double end){
       gr.updateGoalInDB(this.user.getUserId(), id, category, savings, end); 
    }



}
