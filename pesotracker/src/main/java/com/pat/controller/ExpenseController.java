package com.pat.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import com.pat.model.BudgetModel;
import com.pat.model.ExpenseModel;
import com.pat.model.User;
import com.pat.model.Week;
import com.pat.repository.ExpenseRepository;
import com.pat.view.screens.ExpenseFrame;

public class ExpenseController {
    private User user;
    private double totalBudget;
    private ExpenseRepository er;

    public ExpenseController(User currentUser){
        this.user = currentUser;
        this.totalBudget = 0.0;
        this.er = new ExpenseRepository();
    }
    public void saveExpenseToDatabase(double amount, String category){
        er.addExpense(amount, this.user.getUserId(), category);
    }

    public void updateExpenseToDatabase(double amount, int valueId ,String category){
        er.updateExpense(amount, this.user.getUserId(), category, valueId);
    }

    public ArrayList<BudgetModel> getTotalBudget(LocalDate start, LocalDate end){
        ArrayList<BudgetModel> bm = new ArrayList<>();
        ResultSet rs = er.getTotalBudget(this.user.getUserId(), start, end);
        
        if(rs != null){
            try{
                while(rs.next()){
                    BudgetModel budgetModel = new BudgetModel();
                    budgetModel.setAmount(rs.getDouble(1));
                    bm.add(budgetModel);
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        
        return bm;
    }

    public double setTotalExpense(ArrayList<ExpenseModel> em){
        double totalExpense = 0.0;
        for(ExpenseModel expense : em){
            totalExpense += expense.getAmount();
        }
        return totalExpense;
    }


    public ArrayList<ExpenseModel> getExpenseValues(LocalDate start, LocalDate end){
        ArrayList<ExpenseModel> em = new ArrayList<>();

        ResultSet rs = er.getExpenseValue(this.user.getUserId(), start, end);
        if(rs != null){    
            try{
                while(rs.next()){
                ExpenseModel exModel =  new ExpenseModel();
                exModel.setAmount(rs.getDouble("expenseAmount"));
                exModel.setBudgetedAmount(rs.getDouble("budgetAmount"));
                exModel.setCategory(rs.getString("budgetCategory"));
                exModel.setDate(rs.getString("expenseDate"));
                exModel.setId(rs.getInt("expenseId"));
                exModel.setUserId(this.user.getUserId());

                em.add(exModel);
                }
            }catch(SQLException e){
                System.err.println(e.getMessage());
            }
        }
        return em;
    } 


    public ArrayList<Week> setWeeks(int year, Month month){
        ArrayList<Week> weeks = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        LocalDate firstMonday = firstDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        LocalDate weekStart = firstMonday;

        while(!weekStart.isAfter(lastDay)){
            LocalDate weekEnd = weekStart.plusDays(6);

            Week week = new Week(weekStart, weekEnd);
            weeks.add(week);
            weekStart = weekStart.plusWeeks(1);

        }
        
        return weeks;
    }


}
