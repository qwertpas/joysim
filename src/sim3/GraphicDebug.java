package sim3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicDebug extends JPanel{
    private static final long serialVersionUID = -3303992246381800667L;

    // static functions and variables
    public static int numInstances = 0;

    public static GraphicDebug position;
    public static GraphicDebug velocity;

    public static void init(){
        position = new GraphicDebug("position");
        velocity = new GraphicDebug("velocity");
    }

    public static void paintAll(){
        position.repaint();
        velocity.repaint();
    }


    // Instance functions and variables
    JFrame frame;
    Dimension frameSize = new Dimension(300, 300);

    ArrayList<Serie> series = new ArrayList<Serie>();

    private GraphicDebug(String name){ // call this from init() above
        numInstances++;
        frame = new JFrame(name);
		frame.add(this);
        frame.setSize(frameSize);

        frame.setLocation((int) (GraphicSim.screenWidth - Util.posModulo((frameSize.getWidth() * numInstances), GraphicSim.screenWidth)), 0);

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void addSerie(Color color){
        series.add(new Serie(color));
    }

    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for(Serie serie : series){
            if(serie.on){
                g.setColor(serie.color);
                synchronized(serie.points){
                    for(Point point : serie.points){
                        g.drawOval((int) point.getX() + 10, (int) point.getY() - 30, 1, 1);
                    }
                }
                
            }
        }
    }


    public class Serie{ //series but singular :/
        Color color;
        volatile ArrayList<Point> points = new ArrayList<Point>();
        volatile Boolean on = false; //set to true once UserCode initializes

        public Serie(Color color_input){
            color = color_input;
        }

        public void addPoint(double x, double y){
            synchronized(points){ //synchronized so usercode thread can call this while painting and avoid concurrentModificationException
                points.add(new Point((int) x, frame.getContentPane().getHeight() - (int) y));
            }
        }
    }
}