package com.pat.view.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import com.pat.utils.FontLoader;



public class TitlePanel extends JPanel {
    public TitlePanel() {
        JLabel mainLabel = new JLabel("<html><div style='text-align: center; font-family: Call of Ops Duty II; font-size: 32px' >Peso<br>Tracker</div><html>", SwingConstants.CENTER);
        mainLabel.setFont(FontLoader.SIDE_TITLE_FONT);
        mainLabel.setForeground(Color.decode("#FFFFFF"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.decode("#003459"));
        add(Box.createVerticalGlue());

        mainLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(mainLabel);

        add(Box.createVerticalGlue());
        setSize(240, 600);
        setPreferredSize(new Dimension(240, 600));
    }
}
