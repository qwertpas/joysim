package sim3;

import javax.swing.*;

import sim3.Constants.Constant;

import java.awt.*;
import java.awt.event.*;

/**
 *  * A Java Swing program that shows how much water you should drink a day.
 *  * @author www.codejava.net  
 */
public class GraphicInput extends JFrame implements ActionListener {

    JPanel panel = new JPanel();
    JScrollPane scrollPane = new JScrollPane(panel);
    JButton buttonSave = new JButton("Save");
    JButton buttonPause = new JButton("Pause");

    
    public GraphicInput() {
        super("Input");
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setSize(250, 820);
    }

    private void initComponents() {
        add(scrollPane);

        for(Constant constant : Constants.constants){
            panel.add(constant.label);
            constant.field.setMaximumSize(new Dimension(200, constant.field.getPreferredScrollableViewportSize().height));
            panel.add(constant.field);
        }

        // buttonSave.setActionCommand("SAVE");
        // buttonPause.setActionCommand("PAUSE");
        panel.add(buttonSave);
        panel.add(buttonPause);
        buttonSave.addActionListener(this);
        buttonPause.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent event) {
        System.out.println("Button pressed...");
        if(event.getSource() == buttonSave){
            for(Constant constant : Constants.constants){
                Object obj = constant.field.getText();
                constant.setValue(obj);
            }
            Constants.calcConstants();
            System.out.println("Saved");
        }
        if(event.getSource() == buttonPause){
            if(Robot.paused){
                Robot.paused = false;
                Physics.dt = 0;
                Physics.lastTime = System.nanoTime();

                buttonPause.setText("Pause");
                System.out.println("Resumed");
            }else{
                Robot.paused = true;
                buttonPause.setText("Resume");
                System.out.println("Paused");
            }
        }
    }

    

    public static void main(String[] args) { //for testing solely the GraphicInput class
        new GraphicInput().setVisible(true);
    }
}