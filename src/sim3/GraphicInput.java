package sim3;

import javax.swing.*;

import sim3.Constants.Constant;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 *  * A Java Swing program that shows how much water you should drink a day.
 *  * @author www.codejava.net  
 */
public class GraphicInput extends JFrame implements ActionListener {

    JPanel panel = new JPanel();
    JScrollPane scrollPane = new JScrollPane(panel);
    JButton buttonSave = new JButton("Save");

    
    public GraphicInput() {
        super("Input");
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setSize(250, 790);
    }

    private void initComponents() {
        add(scrollPane);

        for(Constant constant : Constants.constants){
            panel.add(constant.label);
            constant.field.setMaximumSize(new Dimension(200, constant.field.getPreferredScrollableViewportSize().height));
            panel.add(constant.field);
        }

        panel.add(buttonSave);
        buttonSave.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent event) {
        for(Constant constant : Constants.constants){
            Object obj = constant.field.getText();
            constant.setValue(obj);
        }
        System.out.println("Saved");
    }

    

    public static void main(String[] args) { //for testing solely the GraphicInput class
        new GraphicInput().setVisible(true);
    }
}