package sim3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicDebug extends JPanel{
    static JFrame frame;
    static GraphicDebug debug;

    static int frameSize;

    static ArrayList<Point> targetVelocity = new ArrayList<Point>();
    static ArrayList<Point> realVelocity = new ArrayList<Point>();

    public static void init(){
		frame = new JFrame("Debug");
		debug = new GraphicDebug();
		frame.add(debug);
        frame.setSize(300, 300);
        frame.setLocation(GraphicSim.screenWidth-300, 0);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        

        targetVelocity.add(new Point((int)(10*Robot.elaspedTime + 10), frame.getContentPane().getHeight() - (int)(10*UserCode.motion.velo + 10)));
        g.setColor(Color.BLACK);
        for(Point point : targetVelocity){
            g.drawOval((int) point.getX(), (int) point.getY(), 1, 1);
        }

        realVelocity.add(new Point((int)(10*Robot.elaspedTime + 10), frame.getContentPane().getHeight() - (int)(10*Robot.physics.linVelo + 10)));
        g.setColor(Color.RED);
        for(Point point : realVelocity){
            g.drawOval((int) point.getX(), (int) point.getY(), 1, 1);
        }
        // realVelocity.add(new Point((int) Robot.elaspedTime, (int) Robot.physics.linVelo));
        // g.setColor(Color.GRAY);
        // for(Point point : realVelocity){
        //     g.drawOval((int)(10*point.getX() + 10), (int)(frame.getContentPane().getHeight() - (10*point.getY() + 10)), 1, 1);
        // }
    }
}