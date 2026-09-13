package com.pat.view.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class GoalPanel extends JPanel{
    public GoalPanel(String t, double end, double save){
        JLabel title = new JLabel(t);
        int endValue = (int) end;
        int saveValue = (int) save;
        JProgressBar pb = new JProgressBar(0,endValue);
        pb.setValue(saveValue);
        JLabel endGoal = new JLabel("End Goal: "+ end);
        JLabel savings = new JLabel("Savings: " + save);
        double remain = end - save;
        JLabel remaining = new JLabel("Remaining: " + remain);
        
        setPreferredSize(new Dimension(180, 150));
        setSize(180, 150);
        setMaximumSize(new Dimension(220, 150));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.decode("#FFFFFF"));
        setAlignmentX(Component.CENTER_ALIGNMENT);


        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        endGoal.setAlignmentX(Component.CENTER_ALIGNMENT);
        savings.setAlignmentX(Component.CENTER_ALIGNMENT);
        remaining.setAlignmentX(Component.CENTER_ALIGNMENT);

        pb.setPreferredSize(new Dimension(180, 15));
        pb.setStringPainted(true);
        pb.setMaximumSize(new Dimension(180, 15));
        
        add(Box.createHorizontalStrut(10));
        add(title);
        add(Box.createVerticalGlue());
        add(pb);
        
        add(Box.createHorizontalStrut(5));
        add(endGoal);
        add(savings);
        add(remaining);
        add(Box.createVerticalGlue());
    }
}
