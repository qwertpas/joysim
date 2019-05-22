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
    JPanel panel;

    JLabel labelQuestion;
    JLabel labelWeight;
    JTextField fieldWeight;
    JButton buttonSave;
    
    public GraphicInput() {
        super("Input");
        panel = new JPanel();
        setPreferredSize(new Dimension(200, 2000));
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);
        setSize(240, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        labelQuestion = new JLabel("How much water should I drink?");
        labelWeight = new JLabel("My weight (kg):");

        JLabel[] labels = {
            new JLabel("GEAR_RATIO"),
            new JLabel("MOTORS_PER_SIDE"),
            new JLabel("ROBOT_MASS")
        };
        ArrayList<JTextField> fields = new ArrayList<JTextField>();


        for(JLabel label : labels){
            add(label);
            

            
            new JTextField(5);
            fieldWeight.setMaximumSize(new Dimension(Integer.MAX_VALUE, fieldWeight.getPreferredScrollableViewportSize().height));
            add(fieldWeight);
        }
        
        

        buttonSave = new JButton("Tell Me");

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        add(labelQuestion);
        add(labelWeight);
        add(fieldWeight);
        add(new JLabel("something after the box"));
        add(buttonSave);
        
        buttonSave.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent event) {
        // Constants.ROBOT_MASS.SE = Double.parseDouble(fieldWeight.getText());
    }

    

    public static void main(String[] args) {
        // new GraphicInput().setVisible(true);
        for(Constant constant : Constants.constants){
            try{
                System.out.println(constant.getName() + " : " + constant.getObject());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}