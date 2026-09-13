package com.pat.view.screens;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.IntStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import com.pat.controller.BudgetController;
import com.pat.model.BudgetModel;
import com.pat.model.User;
import com.pat.model.Week;
import com.pat.utils.FontLoader;

public class BudgetPanel extends JPanel {
    private JPanel budgetCategoryPanel;
    private final JPanel budgetPane;
    private User currentUser;
    private double totalBudget;
    private BudgetController budgetController;
    private final String[] cols = {"Category", "Amount"};
    private DefaultTableModel model;
    private JTable table;
    private ArrayList<BudgetModel> bm;
    private JLabel totalBudgetAmount;
    private JComboBox<Integer> yearSelector;
    private JComboBox<String> monthSelector;
    private JComboBox<Week> weekSelector;
    private Week week;
    private DefaultPieDataset<String> dataset;
    private JFreeChart pieChart;
    private ChartPanel cp;
    private JButton deleteButton;
    private ExpenseFrame ef;
    private DashboardFrame df;

    public BudgetPanel(User currentUser, ExpenseFrame ef, DashboardFrame df) {
        this.currentUser = currentUser;
        budgetController = new BudgetController(this.currentUser);
        this.ef = ef;
        this.df = df;
        setBackground(Color.decode("#9AC6E5"));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // Major Components of the BudgetPanel
        JLabel mainLabel = new JLabel("Budget Tracker");
        JPanel datePane = new JPanel();
        
        //Main Components of BudgetPane (Right-Component)
        JPanel mainPane = new JPanel(new GridBagLayout());
        JPanel innerPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        budgetPane = new JPanel();
        
        // Components within budgetPane (Right-Component)
        budgetCategoryPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.setFont(FontLoader.TEXT_FONT);
        saveButton.setToolTipText("Input values for both Category and Amount then press Save");

        deleteButton = new JButton("Delete");
        deleteButton.setFont(FontLoader.TEXT_FONT);
        deleteButton.setToolTipText("Select entire row and press Delete");

        //DatePane Components
        JLabel yearLabel = new JLabel("Year: ");
        JLabel monthLabel = new JLabel("Month: ");
        JLabel weekLabel = new JLabel("Week: ");

        ArrayList<Integer> years = new ArrayList<>();
        monthSelector = new JComboBox<>(
            Arrays.stream(Month.values())
            .map(month -> month.getDisplayName(TextStyle.FULL, Locale.getDefault()))
            .toArray(String[]::new)
        );

        IntStream.rangeClosed(2000,2050).forEach(y -> years.add(y));


        Month month = LocalDate.now().getMonth();
        Year year = Year.now();

        yearSelector =  new JComboBox<>(years.toArray(Integer[] :: new));
        yearSelector.setSelectedItem(year.getValue());
        monthSelector.setSelectedItem(month.getDisplayName(TextStyle.FULL, Locale.getDefault()));

        updateWeeks(month, year);

        week = (Week) weekSelector.getSelectedItem();

        bm = budgetController.displayBudget(week.getStart(), week.getEnd());


        model = new DefaultTableModel(cols, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return true;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex){
                return switch (columnIndex) {
                    case 0 -> String.class;
                    case 1 -> Double.class;
                    default -> Object.class;
                };
            }

            @Override
            public void setValueAt(Object aValue, int row, int col){
                
                switch(col){
                    case 0 -> {
                        String oldValue = String.valueOf(getValueAt(row,col));
                        String newValue = String.valueOf(aValue);

                        if(!oldValue.equals(newValue) && !newValue.trim().isEmpty()){
                            Double amount =((Number) getValueAt(row, 1)).doubleValue();
                            budgetController.updateBudget(oldValue, newValue, amount, amount);
                        }
                        super.setValueAt(aValue, row, col);

                    }
                    case 1 -> {
                        try{
                            double oldValue = ((Number) getValueAt(row, 1)).doubleValue();
                            double value = ((Number) aValue).doubleValue();
                    
                            if(value > 0){
                                if(oldValue != value){
                                    String category = String.valueOf(getValueAt(row, 0));
                                    budgetController.updateBudget(category, category, oldValue, value);
                                }
                                bm = budgetController.displayBudget(week.getStart(), week.getEnd());
                                totalBudget = budgetController.setTotalBudget(bm);
                                super.setValueAt(aValue, row, col);
                            }

                    }catch(NumberFormatException e){
                        }
                    }
                    default -> super.setValueAt(aValue, row, col);
                }
            }
        };

        table = new JTable(model);
        table.setToolTipText("Enter Values on Category and Amount cell to add new row");
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);



        JScrollPane scrollPane = new JScrollPane(table);
        
        // Customization of Major Components
        mainLabel.setFont(FontLoader.SIDE_TITLE_FONT);
        mainLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainLabel.setAlignmentY(Component.CENTER_ALIGNMENT);


        mainLabel.setPreferredSize(new Dimension(200,50));
        mainLabel.setBackground(Color.decode("#9AC6E5"));
        innerPane.setBackground(Color.decode("#9AC6E5"));
        datePane.setAlignmentX(Component.CENTER_ALIGNMENT);
        datePane.setSize(520, 50);
        datePane.setPreferredSize(new Dimension(520, 50));
        datePane.setBackground(Color.WHITE);
        
        mainPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPane.setSize(530,550);
        mainPane.setPreferredSize(new Dimension(530,550));
        mainPane.setBackground(Color.decode("#9AC6E5"));


        //Customization of BudgetPane (Right-Component)
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        budgetCategoryPanel.setLayout(new BoxLayout(budgetCategoryPanel, BoxLayout.PAGE_AXIS));
        budgetPane.setBackground(Color.decode("#9AC6E5"));

        budgetPane.setPreferredSize(new Dimension(225,400));
        
        scrollPane.setPreferredSize(new Dimension(225, 320));
        saveButton.setPreferredSize(new Dimension(100, 30));
        deleteButton.setPreferredSize(new Dimension(100, 30));


        //Customizing datePane Components
        datePane.setLayout(new BoxLayout(datePane, BoxLayout.X_AXIS));

        yearLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        yearLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        yearSelector.setAlignmentY(Component.CENTER_ALIGNMENT);
        yearSelector.setAlignmentX(Component.CENTER_ALIGNMENT);
        monthLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        monthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        monthSelector.setAlignmentY(Component.CENTER_ALIGNMENT);
        monthSelector.setAlignmentX(Component.CENTER_ALIGNMENT);
        weekLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        weekLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        weekSelector.setAlignmentY(Component.CENTER_ALIGNMENT);
        weekSelector.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Adding Components to DatePane
        
        datePane.add(yearLabel);
        datePane.add(yearSelector);
        datePane.add(monthLabel);
        datePane.add(monthSelector);
        datePane.add(weekLabel);
        datePane.add(weekSelector);

        // Adding Componentst to BudgetPane        
        budgetPane.add(scrollPane);
        budgetPane.add(saveButton);
        budgetPane.add(deleteButton);

        //Main Components of statsPane (Left-Component)
        JPanel statsPane = new JPanel();
        
        JPanel chartPane = new JPanel();
        JPanel totalBudgetPane = new JPanel();

        // Subcomponents of Allocation Panel
        JLabel totalBudgetLabel = new JLabel("Total Budget: ");
        totalBudgetLabel.setFont(FontLoader.TEXT_FONT);

        totalBudgetAmount = new JLabel(String.valueOf(totalBudget));
        totalBudgetAmount.setFont(FontLoader.TEXT_FONT);


        //Customization of statsPane (Left-Component)
        statsPane.setLayout(new BoxLayout(statsPane, BoxLayout.PAGE_AXIS));

        statsPane.setPreferredSize(new Dimension(285,400));
        chartPane.setPreferredSize(new Dimension(285, 320));

        statsPane.setBackground(Color.decode("#9AC6E5"));
        chartPane.setBackground(Color.decode("#9AC6E5"));
        totalBudgetPane.setBackground(Color.decode("#9AC6E5"));

        chartPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        totalBudgetPane.setAlignmentX(Component.CENTER_ALIGNMENT);
      
        //Adding Panels to statsPane (Left-Component)
        totalBudgetPane.add(totalBudgetLabel);
        totalBudgetPane.add(totalBudgetAmount);

        dataset = new DefaultPieDataset<>();

        setValues(bm, model);
        //JFreeChart Panels and Dataset:
        cp = new ChartPanel(pieChart);
        cp.setPreferredSize(new Dimension(285, 320));
        chartPane.add(cp);

        statsPane.add(chartPane);
        statsPane.add(totalBudgetPane);

        //Adding to Main BudgetPanel 
        add(mainLabel);
        add(datePane);

        innerPane.add(statsPane);
        innerPane.add(Box.createHorizontalStrut(5));
        innerPane.add(budgetPane);
        
        mainPane.add(innerPane);

        add(mainPane);

        datePane.setPreferredSize(new Dimension(550,45));
        datePane.setMaximumSize(new Dimension(550, 45));

        yearSelector.addActionListener(e->{
            selectorFunction();
        });

        monthSelector.addActionListener(e->{
            selectorFunction();
        });

        weekSelector.addActionListener(e->{
            Week weekSelected = (Week) weekSelector.getSelectedItem();
            refreshTable(weekSelected);
        });

        deleteButton.addActionListener(e -> {
            int selectedRowView = table.getSelectedRow();
            if(selectedRowView != -1){
            int option = JOptionPane.showConfirmDialog(null, "Delete Row?","Delete!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(option == JOptionPane.OK_OPTION){
                if(table.isEditing()){
                    table.getCellEditor().stopCellEditing();
                }
                int selectedRowModel = table.convertRowIndexToModel(selectedRowView);

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                Object target = model.getValueAt(selectedRowModel, 0);
                budgetController.deleteBudget(String.valueOf(target));

                model.setRowCount(0);
                refreshTable(week);
                totalBudgetAmount.setText(String.valueOf(totalBudget));
                }
            }
        });



        saveButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(null, "Allow Changes?","Save Edits!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(option == JOptionPane.OK_OPTION){
                if(table.isEditing()){
                    table.getCellEditor().stopCellEditing();
                }

                DefaultTableModel m = (DefaultTableModel) table.getModel();
                for(int i=0; i < m.getRowCount(); i++){
                    if(!(String.valueOf(m.getValueAt(i, 0))).trim().isEmpty()){
                        String category = String.valueOf(m.getValueAt(i,0));
                        double amount = ((Number) m.getValueAt(i,1)).doubleValue();
                        if(budgetController.checkCategoryExists(category) == false && amount > 0){
                            budgetController.saveBudgetToDatabase(category, amount);
                        }
                    }
                }
                    model.setRowCount(0);
                    refreshTable(week);
                    totalBudgetAmount.setText(String.valueOf(totalBudget));

            }
        });
    }

    private void selectorFunction(){
            try{
                Integer selectedValue = (Integer) yearSelector.getSelectedItem();
                Year selectedYear = Year.of(selectedValue);
                Month selectedMonth = Month.valueOf(String.valueOf(monthSelector.getSelectedItem()).toUpperCase());

                updateWeeks(selectedMonth, selectedYear);

                Week weekSelected = (Week) weekSelector.getSelectedItem();
                
                refreshTable(weekSelected);
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
    }


    private void setValues(ArrayList<BudgetModel> bm, DefaultTableModel model){
        if(bm == null  || bm.isEmpty()){
            totalBudget = 0.0;
            totalBudgetAmount.setText(String.valueOf(totalBudget));
            dataset.clear();
        }else{
           for(BudgetModel budgetModel : bm){
                double amount = budgetModel.getAmount();
                String cat = budgetModel.getCategory();
                model.addRow(new Object[]{cat, amount});
                totalBudget = budgetController.setTotalBudget(bm);
                totalBudgetAmount.setText(String.valueOf(totalBudget));
                dataset.setValue(budgetModel.getCategory(), budgetModel.getAmount());
            }

        }
        model.addRow(new Object[]{"",0});
        pieChart = ChartFactory.createPieChart("Weekly Budget Allocation",dataset,true,true,false);
        ((PiePlot<?>) pieChart.getPlot()).setLabelGenerator(null);
    }

    private void refreshTable(Week selectedWeek){
        if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
        }   

        model = (DefaultTableModel) table.getModel();
        model.getDataVector().removeAllElements();
        bm = budgetController.displayBudget(selectedWeek.getStart(), selectedWeek.getEnd());
        if(dataset != null){
            dataset.clear();
        }
        if(bm == null || bm.isEmpty()){
            totalBudget = 0.0;

        }else{
            for(BudgetModel budgetModel : bm){
                model.addRow(new Object[]{budgetModel.getCategory(),  budgetModel.getAmount()});
                totalBudget = budgetController.setTotalBudget(bm);
                dataset.setValue(budgetModel.getCategory(), budgetModel.getAmount());          
            }
        }
        model.addRow(new Object[]{"",0});

        pieChart = ChartFactory.createPieChart("Weekly Budget Allocation",dataset,true,true,false);
        ((PiePlot<?>) pieChart.getPlot()).setLabelGenerator(null);
        cp.revalidate();
        cp.repaint();
        model.fireTableDataChanged();

        DefaultTableModel expenseModel = this.ef.getTableModel();
        this.ef.setTableData(expenseModel, selectedWeek);
        this.df.setTotalValues(selectedWeek.getStart(), selectedWeek.getEnd());
    }


    private void updateWeeks(Month selectedMonth, Year selectedYear){
        ArrayList<Week> weeks = budgetController.setWeeks(selectedYear.getValue(), selectedMonth);
        Week[] weekArray = weeks.toArray(new Week[0]);

        if(weekSelector == null){
            weekSelector = new JComboBox<>(weekArray);
        }else{
            weekSelector.setModel(new DefaultComboBoxModel<>(weekArray));
        }
        for(int i = 0; i < weekSelector.getItemCount() ;i++){
            Week w = weekSelector.getItemAt(i);
            if(w.contains(LocalDate.now())){
                weekSelector.setSelectedIndex(i);
                break;
            }
        }
    }
}