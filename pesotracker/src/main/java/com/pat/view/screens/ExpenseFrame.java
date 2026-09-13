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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.pat.controller.ExpenseController;
import com.pat.model.BudgetModel;
import com.pat.model.ExpenseModel;
import com.pat.model.User;
import com.pat.model.Week;
import com.pat.utils.FontLoader;

public final class ExpenseFrame extends JPanel {
    private double totalBudget = 0.0;
    private JLabel totalBudgetLabel;
    private JComboBox<Integer> yearSelector;
    private JComboBox<String> monthSelector;
    private JComboBox<Week> weekSelector;
    private ArrayList<ExpenseModel> expenseArray;
    private Week week;
    private ExpenseController ec;
    private JTable table;
    private ArrayList<BudgetModel> bm;
    private DashboardFrame df;

    public ExpenseFrame(User currentUser, DashboardFrame df) {
        this.df = df;
        ec = new ExpenseController(currentUser);

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel expenseLabel = new JLabel("Expense Tracker");
        
        expenseLabel.setFont(FontLoader.SIDE_TITLE_FONT);

        JPanel dateSelectorPanel = new JPanel();
        JPanel totalBudgetPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        JLabel yearLabel = new JLabel("Year");
        JLabel monthLabel = new JLabel("Month");
        JLabel weekLabel = new JLabel("Week");


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

        expenseArray = new ArrayList<>();
        bm = new ArrayList<>();

        JLabel totalLabel = new JLabel("Total Budget: ");
        totalBudgetLabel = new JLabel(String.valueOf(totalBudget));
        totalLabel.setFont(FontLoader.TEXT_FONT);
        totalBudgetLabel.setFont(FontLoader.TEXT_FONT);

        JButton saveButton = new JButton("Save");

        String[] columns = {"Id", "Category", "Expense Amount", "Budgeted Amount"};

        DefaultTableModel model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int col){
                return col == 2;
            }

            @Override 
            public Class<?> getColumnClass(int columnIndex){
                return switch(columnIndex){
                    case 0 -> Integer.class;
                    case 1 -> String.class;
                    case 2 -> Double.class;
                    case 3 -> Double.class;
                    default -> Object.class;
                };
            }

            @Override
            public void setValueAt(Object value, int row, int col){
                switch(col){
                case 2 ->{
                    try{
                        double oldValue = ((Number) getValueAt(row, 2)).doubleValue();
                        double newValue = ((Number) value).doubleValue();

                        if(newValue > 0){
                            if(oldValue != newValue){
                                String category = String.valueOf(getValueAt(row, 1));
                                ec.saveExpenseToDatabase(newValue, category);
                            }
                            Week selectedWeek = (Week) weekSelector.getSelectedItem();
                            setTableData((DefaultTableModel)table.getModel(), selectedWeek);
                            super.setValueAt(value, row, col);
                        }   
                    }catch(NumberFormatException e){

                    }
                }
                default -> super.setValueAt(value, row, col);
            }
        }

        }; 

        table = new JTable(model){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col){
                Component comp = super.prepareRenderer(renderer, row, col);
                
                if(isRowSelected(row)){
                    return comp;
                }

                int modelRow = convertRowIndexToModel(row);
                Object value = getModel().getValueAt(modelRow, 2);
                Object budgetValue = getModel().getValueAt(modelRow, 3);
                
                double expense = ((Number) value).doubleValue();
                double budget = ((Number) budgetValue).doubleValue();

                comp.setBackground(getBackground());
                comp.setForeground(getForeground());
                
                if (expense > budget) {
                    comp.setBackground(Color.decode("#FF7F7F"));
                    comp.setForeground(Color.BLACK);
                }else{
                    comp.setBackground(Color.decode("#F2F0EF"));
                    comp.setForeground(Color.BLACK);
                }
                return comp;
            }
        };
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setToolTipText("Add Budget in Budget Tracker and Edit Expense by double clicking and pressing Save button");

        TableColumn idColumn = table.getColumnModel().getColumn(0);
        table.getColumnModel().removeColumn(idColumn);

        Week selectedWeek = (Week) weekSelector.getSelectedItem();

        setTableData(model, selectedWeek);


        setBackground(Color.decode("#9AC6E5"));
        JScrollPane tablePanel = new JScrollPane(table);
        tablePanel.setPreferredSize(new Dimension(480, 350));
        tablePanel.setSize(480, 350);

        totalBudgetPanel.setPreferredSize(new Dimension(200,40));
        buttonPanel.setPreferredSize(new Dimension(200,60));
        buttonPanel.setBackground(Color.decode("#9AC6E5"));


        dateSelectorPanel.setLayout(new BoxLayout(dateSelectorPanel,BoxLayout.X_AXIS));
        totalBudgetPanel.setLayout(new BoxLayout(totalBudgetPanel,BoxLayout.X_AXIS));
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));

        expenseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateSelectorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        totalBudgetPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        expenseLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        dateSelectorPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        totalBudgetPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        yearLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        yearSelector.setAlignmentY(Component.CENTER_ALIGNMENT);
        monthLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        monthSelector.setAlignmentY(Component.CENTER_ALIGNMENT);
        weekLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        weekSelector.setAlignmentY(Component.CENTER_ALIGNMENT);

        dateSelectorPanel.setBackground(Color.decode("#FFFFFF"));
        dateSelectorPanel.setPreferredSize(new Dimension(550,45));
        dateSelectorPanel.setMaximumSize(new Dimension(550, 45));

        dateSelectorPanel.add(yearLabel);
        dateSelectorPanel.add(yearSelector);
        dateSelectorPanel.add(monthLabel);
        dateSelectorPanel.add(monthSelector);
        dateSelectorPanel.add(weekLabel);
        dateSelectorPanel.add(weekSelector);

        totalBudgetPanel.add(totalLabel);
        totalBudgetPanel.add(totalBudgetLabel);

        totalBudgetPanel.setBackground(Color.decode("#9AC6E5"));
        tablePanel.setBackground(Color.decode("#9AC6E5"));
        
        buttonPanel.add(saveButton);

        yearSelector.addActionListener(e-> selectorFunction(model));
        monthSelector.addActionListener(e-> selectorFunction(model));

        weekSelector.addActionListener(e->{
            Week newWeek = (Week) weekSelector.getSelectedItem();
            setTableData(model, newWeek);  
        });


        saveButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(null, "Allow Changes?","Save Edits!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(option == JOptionPane.OK_OPTION){
                int selectedRowIndex = table.getSelectedRow();
                if(selectedRowIndex != -1){
                    if(table.isEditing()){
                         table.getCellEditor().stopCellEditing();
                     }


                    int modelRowIndex = table.convertRowIndexToModel(selectedRowIndex);
                    Object id = table.getModel().getValueAt(modelRowIndex, 0);
                    Object cat = table.getModel().getValueAt(modelRowIndex, 1);
                    Object am = table.getModel().getValueAt(modelRowIndex, 2);
                    int idValue = ((Number) id).intValue();
                    String category = String.valueOf(cat);
                    double amount = ((Number) am).doubleValue();
                    ec.updateExpenseToDatabase(amount, idValue ,category);
                }
                model.setRowCount(0);
                setTableData(model, selectedWeek);
            }
        });

        add(expenseLabel);
        add(dateSelectorPanel);
        add(totalBudgetPanel);
        add(tablePanel);
        add(buttonPanel);
    }

    public DefaultTableModel getTableModel(){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        return model;
    }

    public void setTableData(DefaultTableModel model, Week selectedWeek){
        model.setRowCount(0);
        expenseArray = ec.getExpenseValues(selectedWeek.getStart(), selectedWeek.getEnd());
        if(!expenseArray.isEmpty()){
            totalBudget = 0.0;
            for(ExpenseModel ea : expenseArray){
                int id = ea.getId();
                double amount = ea.getAmount();
                double budget = ea.getBudgetedAmount();
                String cat = ea.getCategory();
                model.addRow(new Object[]{id, cat, amount, budget});
                totalBudget += budget;
            } 
        }else{
                totalBudget = 0.0;
        }
        model.addRow(new Object[]{0,"", 0.0,0.0});
        totalBudgetLabel.setText(String.valueOf(totalBudget));
        model.fireTableDataChanged();
        this.df.setTotalValues(selectedWeek.getStart(), selectedWeek.getEnd());
    }



    private void selectorFunction(DefaultTableModel model){
        try{
            Integer selectedValue = (Integer) yearSelector.getSelectedItem();
            Year selectedYear = Year.of(selectedValue);
            Month selectedMonth = Month.valueOf(String.valueOf(monthSelector.getSelectedItem()).toUpperCase());

            updateWeeks(selectedMonth, selectedYear);
            
            Week getSelectedWeek = (Week) weekSelector.getSelectedItem();
            setTableData(model, getSelectedWeek);

        }catch(Exception e){
            System.out.println(e.getMessage());
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
