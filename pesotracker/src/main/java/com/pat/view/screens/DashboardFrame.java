package com.pat.view.screens;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.IntStream;

import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.math3.linear.DefaultRealMatrixChangingVisitor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.pat.controller.BudgetController;
import com.pat.controller.ExpenseController;
import com.pat.model.BudgetModel;
import com.pat.model.ExpenseModel;
import com.pat.model.User;
import com.pat.model.Week;
import com.pat.utils.FontLoader;

public class DashboardFrame extends JPanel {
    private JComboBox<Integer> yearSelector;
    private JComboBox<String> monthSelector;
    private JComboBox<Week> weekSelector;
    private Week week;
    private double totalBudget = 0.0;
    private double totalExpense = 0.0;
    private ExpenseController ec;
    private BudgetController bc;
    private ArrayList<BudgetModel> budgetModel;
    private ArrayList<ExpenseModel> expenseModel;
    private  JLabel totalBudgetLabel;
    private JLabel totalExpenseLabel;
    private DefaultPieDataset<String> dataset = new DefaultPieDataset<String>();
    private DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
    private DefaultPieDataset<String> ringDataset = new DefaultPieDataset<String>();
    private JFreeChart expenseChart;
    private JFreeChart barChart;
    private JFreeChart ringChart;
    private ChartPanel expensePanel;
    private ChartPanel barPanel;
    private ChartPanel ringPanel;

    public DashboardFrame(User currentUser) {
        ec = new ExpenseController(currentUser);
        bc = new BudgetController(currentUser);

        setBackground(Color.decode("#9AC6E5"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel dashboardTitle = new JLabel("Dashboard Statistics");
        
        JPanel selectorPanel = new JPanel();
        JPanel totalPanel = new JPanel();
        JPanel chartsPanel = new JPanel();

        selectorPanel.setBackground(Color.decode("#FFFFFF"));
        totalPanel.setBackground(Color.decode("#9AC6E5"));
        chartsPanel.setBackground(Color.decode("#9AC6E5"));

        JScrollPane scrollPane = new JScrollPane(chartsPanel);

        JLabel yearLabel = new JLabel("Year");
        JLabel monthLabel = new JLabel("Month");
        JLabel weekLabel = new JLabel("Week");

        totalBudgetLabel = new JLabel("Total Budget: " + totalBudget); 
        totalExpenseLabel = new JLabel("Total Expense: " + totalExpense);

        totalBudgetLabel.setFont(FontLoader.TEXT_FONT);
        totalExpenseLabel.setFont(FontLoader.TEXT_FONT);

        selectorPanel.setLayout(new BoxLayout(selectorPanel,BoxLayout.X_AXIS));
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.X_AXIS));
        chartsPanel.setLayout(new BoxLayout(chartsPanel, BoxLayout.Y_AXIS));

        ArrayList<Integer> years = new ArrayList<>();

        IntStream.rangeClosed(2000, 2050).forEach(y -> years.add(y));

        yearSelector = new JComboBox<>(years.toArray(Integer[]::new));
        monthSelector = new JComboBox<>(
            Arrays.stream(Month.values())
            .map(month -> month.getDisplayName(TextStyle.FULL, Locale.getDefault()))
            .toArray(String[] :: new)
        );

        Month month = LocalDate.now().getMonth();
        Year year = Year.now();

        yearSelector.setSelectedItem(year.getValue());
        monthSelector.setSelectedItem(month.getDisplayName(TextStyle.FULL, Locale.getDefault()));

        weekSelector = new JComboBox<>();
        updateWeeks(month, year);

        week = (Week) weekSelector.getSelectedItem();

        setTotalValues(week.getStart(), week.getEnd());
        
        scrollPane.setPreferredSize(new Dimension(480, 350));
        scrollPane.setSize(480, 350);

        selectorPanel.setPreferredSize(new Dimension(550,45));
        selectorPanel.setMaximumSize(new Dimension(550, 45));
        totalPanel.setPreferredSize(new Dimension(550, 60));
        totalPanel.setMaximumSize(new Dimension(550, 60));

        dashboardTitle.setFont(FontLoader.SIDE_TITLE_FONT);
        dashboardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        selectorPanel.add(yearLabel);
        selectorPanel.add(yearSelector);
        selectorPanel.add(monthLabel);
        selectorPanel.add(monthSelector);
        selectorPanel.add(weekLabel);
        selectorPanel.add(weekSelector);

        totalPanel.add(Box.createHorizontalGlue());
        totalPanel.add(totalBudgetLabel);
        totalPanel.add(Box.createHorizontalGlue());
        totalPanel.add(totalExpenseLabel);
        totalPanel.add(Box.createHorizontalGlue());

        expensePanel = new ChartPanel(expenseChart);
        barPanel = new ChartPanel(barChart);
        ringPanel = new ChartPanel(ringChart);

        chartsPanel.add(expensePanel);
        chartsPanel.add(barPanel);
        chartsPanel.add(ringPanel);



        yearSelector.addActionListener(e->{
            selectorFunction();
        });

        monthSelector.addActionListener(e->{
            selectorFunction();
        });

        weekSelector.addActionListener(e->{
            Week weekSelected = (Week) weekSelector.getSelectedItem();
            setTotalValues(weekSelected.getStart(), weekSelected.getEnd());
        });

        expensePanel.setPreferredSize(new Dimension(500, 200));
        barPanel.setPreferredSize(new Dimension(500, 400));
        ringPanel.setPreferredSize(new Dimension(500, 400));


        add(dashboardTitle);
        add(selectorPanel);
        add(totalPanel);
        add(scrollPane);
    }

    private void setChartValues(ArrayList<ExpenseModel> em){
        dataset.clear();
        barDataset.clear();
        ringDataset.clear();
        double totalBudgetData = 0.0;
        double totalExpenseData = 0.0;
        
        for(ExpenseModel exModel : em){
            dataset.setValue(exModel.getCategory(), exModel.getAmount());
            barDataset.setValue(exModel.getAmount(),"Expense" , exModel.getCategory());
            barDataset.setValue(exModel.getBudgetedAmount(), "Budget", exModel.getCategory());
            totalBudgetData += exModel.getBudgetedAmount();
            totalExpenseData += exModel.getAmount();
        }

        double totalRemaining = totalBudgetData - totalExpenseData;
        ringDataset.setValue("Spent", totalExpenseData);
        ringDataset.setValue("Remaining", totalRemaining);

        expenseChart = ChartFactory.createPieChart("Weekly Expense by Category",dataset, true, true, false);
        ((PiePlot<?>) expenseChart.getPlot()).setLabelGenerator(null);
        barChart = ChartFactory.createBarChart("Weekly Budget Vs. Expense", "Category", "Value", barDataset, PlotOrientation.VERTICAL, true, true, false);
        
        ringChart = ChartFactory.createRingChart("Budget Usage", ringDataset, true, true, false);
        RingPlot plot = (RingPlot) ringChart.getPlot();
        plot.setSectionDepth(0.70);


        if(expensePanel != null ){
            expensePanel.revalidate();
            expensePanel.repaint();
        }
        if (barPanel != null) {
            barPanel.revalidate();
            barPanel.repaint();
        }
        if (ringPanel != null) {
            ringPanel.revalidate();
            ringPanel.repaint();
        }
    }


    public void setTotalValues(LocalDate dateStart, LocalDate dateEnd){
        budgetModel = bc.displayBudget(dateStart, dateEnd);
        totalBudget = bc.setTotalBudget(budgetModel);

        expenseModel = ec.getExpenseValues(dateStart, dateEnd);
        totalExpense = ec.setTotalExpense(expenseModel);

        totalBudgetLabel.setText("Total Budget: " + totalBudget);
        totalExpenseLabel.setText("Total Expense: " + totalExpense);

        setChartValues(expenseModel);
    }

    private void selectorFunction(){
            try{
                Integer selectedValue = (Integer) yearSelector.getSelectedItem();
                Year selectedYear = Year.of(selectedValue);
                Month selectedMonth = Month.valueOf(String.valueOf(monthSelector.getSelectedItem()).toUpperCase());

                updateWeeks(selectedMonth, selectedYear);

                Week weekSelected = (Week) weekSelector.getSelectedItem();
                setTotalValues(weekSelected.getStart(), weekSelected.getEnd());                
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
    }

    private void updateWeeks(Month mon, Year year){
        ArrayList<Week> weeks = ec.setWeeks(year.getValue(), mon);

        Week[] weekArray = weeks.toArray(Week[] :: new);

        if(weekSelector == null){
            weekSelector = new JComboBox<>(weekArray);
        }else{
            weekSelector.setModel(new DefaultComboBoxModel<>(weekArray));
        }
        for(int i = 0 ; i < weekSelector.getItemCount() ; i++){
            Week w = weekSelector.getItemAt(i);
            if(w.contains(LocalDate.now())){
                weekSelector.setSelectedIndex(i);
                break;
            }
        }
    }
}
