package com.pat.view.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.pat.controller.GoalController;
import com.pat.model.GoalModel;
import com.pat.model.User;
import com.pat.utils.FontLoader;
import com.pat.view.components.GoalPanel;

public class GoalFrame extends JPanel {
    private JTable table;
    private GoalController gc;
    private User currentUser;
    private JPanel goalsPanel;
    private ArrayList<GoalModel> gm;
    public GoalFrame(User currentUser) {
        this.currentUser = currentUser;
        this.gc = new GoalController(this.currentUser);
        setBackground(Color.decode("#9AC6E5"));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel goalsLabel = new JLabel("Saving Goals");

        goalsLabel.setFont(FontLoader.SIDE_TITLE_FONT);

        JPanel tablePane = new JPanel();
        goalsPanel = new JPanel();
        JPanel buttonPane = new JPanel();
        JPanel mainPane = new JPanel();

        goalsPanel.setLayout(new BoxLayout(goalsPanel, BoxLayout.Y_AXIS));

        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.X_AXIS));
        tablePane.setLayout(new BoxLayout(tablePane, BoxLayout.PAGE_AXIS));

        JButton saveButton = new JButton("Save");
        JButton deleteButton = new JButton("Delete");

        String[] columns = {"Id", "Goal", "Target", "Savings","Remaining"};
        DefaultTableModel model = new DefaultTableModel(columns, 0){
            @Override 
            public boolean isCellEditable(int row, int col){
                return col != 4;
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
            public void setValueAt(Object val, int row, int col){
                int id = ((Number) getValueAt(row, 0)).intValue();
                switch(col){
                    case 1 ->{
                        String oldVal = String.valueOf(getValueAt(row, col));
                        String newVal = String.valueOf(val);
                        double end  = ((Number) getValueAt(row, 2)).doubleValue();
                        double savings  = ((Number) getValueAt(row, 3)).doubleValue();
                       
                        super.setValueAt(val, row, col);
                        if (!oldVal.equals(newVal) && !newVal.trim().isEmpty()) {
                            SwingUtilities.invokeLater(()->{

                                if (id != 0) {
                                    gc.updateGoalToDatabase(id, newVal, savings, end);
                                    setTableData(this);
                                } else {

                                }
                            });  

                        }
                    }
                    case 2 ->{
                            double oldVal  = ((Number) getValueAt(row, col)).doubleValue();
                            double newVal = ((Number) val).doubleValue();
                            double savings  = ((Number) getValueAt(row, 3)).doubleValue();
                            
                            super.setValueAt(val, row, col);
                            if (oldVal != newVal && newVal >= savings) {
                                String category = String.valueOf(getValueAt(row, 1));
                                
                                SwingUtilities.invokeLater(()->{
                                    if(id != 0){
                                        gc.updateGoalToDatabase(id, category, savings, newVal);     
                                        setTableData(this);
                                    }
                                });    
                            }
                        }
                    case 3->{
                            double oldVal  = ((Number) getValueAt(row, col)).doubleValue();
                            double newVal = ((Number) val).doubleValue();
                            double end  = ((Number) getValueAt(row, 2)).doubleValue();
                            super.setValueAt(val, row, col);
                            
                            if (oldVal != newVal && end >= newVal) {
                                String category = String.valueOf(getValueAt(row, 1));
                                SwingUtilities.invokeLater(()->{
                                    if(id != 0){
                                        gc.updateGoalToDatabase(id, category, newVal, end);     
                                        setTableData(this);
                                    }   
                            });
                        }
                    }
            

                    default -> {
                        super.setValueAt(val, row, col);
                    }
                }
            }
        };

        table = new JTable(model);
        TableColumn idColumn = table.getColumnModel().getColumn(0);
        table.getColumnModel().removeColumn(idColumn);

        setTableData(model);

        JScrollPane goalScrollPane = new JScrollPane(goalsPanel);
        JScrollPane sp = new JScrollPane(table);

        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setPreferredSize(new Dimension(225, 375));
        sp.setSize(225, 375);

        tablePane.setSize(225, 400);
        tablePane.setPreferredSize(new Dimension(225, 400));

        goalScrollPane.setPreferredSize(new Dimension(225,400));

        goalScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        goalScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        buttonPane.add(saveButton);
        buttonPane.add(deleteButton);

        tablePane.add(sp);
        tablePane.add(buttonPane);
        mainPane.add(tablePane);

        goalsPanel.setBackground(Color.decode("#9AC6E5"));

        mainPane.add(goalScrollPane);


        saveButton.addActionListener(e ->{
            int option = JOptionPane.showConfirmDialog(null, "Allow Changes?","Save Edits!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(option == JOptionPane.OK_OPTION){
                if(table.isEditing()){
                    table.getCellEditor().stopCellEditing();
                }
                
                DefaultTableModel m = (DefaultTableModel) table.getModel();
                int selectedRowIndex = table.getSelectedRow();
                if (selectedRowIndex != -1) {
                    int selectedRowModel = table.convertRowIndexToModel(selectedRowIndex);

                    String category = String.valueOf(m.getValueAt(selectedRowModel, 1));
                    int end = ((Number) m.getValueAt(selectedRowModel, 2)).intValue();
                    int savings = ((Number) m.getValueAt(selectedRowModel, 3)).intValue();
                    if(!category.trim().isEmpty() && end >= savings && end > 0 && savings >= 0){
                        gc.setGoal(savings, end, category);
                }
            }
            model.setRowCount(0);
            setTableData(model);
            }
        });

        deleteButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(null, "Delete Row?","Delete!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(option == JOptionPane.OK_OPTION){
                if(table.isEditing()){
                    table.getCellEditor().stopCellEditing();
                }
                
                DefaultTableModel m = (DefaultTableModel) table.getModel();
                int selectedRowIndex = table.getSelectedRow();
                if (selectedRowIndex != -1) {
                    int selectedRowModel = table.convertRowIndexToModel(selectedRowIndex);
                    int id = ((Number) m.getValueAt(selectedRowModel, 0)).intValue() ;
                    gc.deleteGoalInDatabase(id);
            }
            model.setRowCount(0);
            gm.clear();
            setTableData(model);
            }
        });

        add(goalsLabel);
        add(mainPane);
    }

    private void setTableData(DefaultTableModel model){
        goalsPanel.removeAll();
        model.setRowCount(0);
        gm = gc.getGoalFromDatabase();
        if(!gm.isEmpty()){
            for(GoalModel goals : gm){
                int id = goals.getId();
                double savings = goals.getSavings();
                double end = goals.getEnd();
                String category = goals.getCategory();

                model.addRow(new Object[]{id, category, end, savings, end-savings});
                goalsPanel.add(new GoalPanel(category, end, savings));
                goalsPanel.add(Box.createVerticalStrut(20));

            }
        }
        model.addRow(new Object[]{0, "", 0.0, 0.0, 0.0});
        model.fireTableDataChanged();
        revalidate();
        repaint();
    }
}
