package com.pat.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Optional;

import com.pat.model.BudgetModel;
import com.pat.model.User;
import com.pat.model.Week;
import com.pat.repository.BudgetRepository;
import com.pat.view.screens.BudgetPanel;

public class BudgetController {
    private User currentUser;
    private BudgetRepository br;
    private BudgetModel bm;
    private ArrayList<BudgetModel> budgetModel;

    public BudgetController(User user) {
        this.currentUser = user;
        this.br = new BudgetRepository();
    }

    public void saveBudgetToDatabase(String category, Double amount) {
        br.addBudget(category, amount, this.currentUser.getUserId());
    }

    public ArrayList<BudgetModel> displayBudget(LocalDate dateStart, LocalDate dateEnd) {
        ResultSet rs = br.selectAllBudgetWithinRange(this.currentUser.getUserId(), dateStart, dateEnd);
        budgetModel = new ArrayList<>();

        try {
            if (rs.next()) {
                do {
                    bm = new BudgetModel();
                    bm.setId(rs.getInt(1));
                    bm.setAmount(rs.getDouble(2));
                    bm.setCategory(rs.getString(3));
                    bm.setDate(rs.getString(4));
                    budgetModel.add(bm);
                } while (rs.next());

            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return budgetModel;
    }

    public double setTotalBudget(ArrayList<BudgetModel> bm) {
        double totalBudget = 0.0;
        if (bm == null) {
            totalBudget = 0.0;
        } else {
            for (BudgetModel budgetModel : bm) {
                double amount = budgetModel.getAmount();
                totalBudget += amount;
            }
        }
        return totalBudget;
    }

    public void updateBudget(String oldValueCat, String category, double oldValueAmount, double amount) {
        Optional<Integer> id = budgetModel.stream()
                .filter(p -> p.getCategory().equals(oldValueCat)) 
                .map(BudgetModel::getId)
                .findFirst();
        br.updateBudget(amount, category, this.currentUser.getUserId(), id.orElse(0));
    }

    public Boolean checkCategoryExists(String category) {
        Boolean flag = br.checkCategoryExists(this.currentUser.getUserId(), category);
        return flag;
    }

    public void deleteBudget(String category) {
        br.deleteBudget(category, this.currentUser.getUserId());
    }

    public ArrayList<Week> setWeeks(int year, Month month) {
        ArrayList<Week> weeks = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        LocalDate firstMonday = firstDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        LocalDate weekStart = firstMonday;

        while (!weekStart.isAfter(lastDay)) {
            LocalDate weekEnd = weekStart.plusDays(6);

            Week week = new Week(weekStart, weekEnd);
            weeks.add(week);
            weekStart = weekStart.plusWeeks(1);
        }

        return weeks;
    }
}
