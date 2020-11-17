package sim3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import sim3.userclasses.UserCode;

public class GraphicSim extends JPanel implements MouseListener {
	private static final long serialVersionUID = -87884863222799400L;

	static JFrame frame;

	AffineTransform defaultTransform = new AffineTransform(); //to reset the g2d position and rotation

	static BufferedImage robotImage, slipImage, odoImage, robot_estImage;

    double windowWidth = frame.getContentPane().getSize().getWidth();
	double windowHeight = frame.getContentPane().getSize().getHeight();
    // static int robotCenterXPixel, robotCenterYPixel;

	static int screenHeight;
	static int screenWidth;
	static GraphicSim sim;

	static int robotImgHeight;
	static int robotDisplayWidth;
    static double robotScale;
    
	static ArrayList<Point> points = new ArrayList<Point>();

  	GraphicSim() {
		addMouseListener(this);
	}

    @Override
    public void paint(Graphics g) { //gets called iteratively by JFrame
        windowWidth = frame.getContentPane().getSize().getWidth();
		windowHeight = frame.getContentPane().getSize().getHeight();
        super.paint(g);
        
		// Graphics2D g2d = (Graphics2D) g;
        // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawString("posest " + UserCode.odometry.estPose.getVector2D().toString(), 500, 700);
		g.drawString("actual " + Main.robot.globalPos.toString(), 500, 725);
		g.drawString("linear velocity (ft/sec) " + Util.roundHundreths(Util.metersToFeet(Main.robot.linVelo)), 500, 750);
		g.drawString("left power "+ Util.roundHundreths(UserCode.lPower), 500, 775);
		g.drawString("right power "+ Util.roundHundreths(UserCode.rPower), 500, 800);
		g.drawString("elapsed time " + Util.roundHundreths(Main.elaspedTime), 500, 825);


        //drawing lines
        g.setColor(Color.GRAY.brighter());
        for(int i = 0; i < screenWidth; i += Constants.DISPLAY_SCALE.getDouble() / Util.metersToFeet(1)){
            g.drawLine(i, 0, i, screenHeight);
        }
        for(int i = 0; i < screenHeight; i += Constants.DISPLAY_SCALE.getDouble() / Util.metersToFeet(1)){
            g.drawLine(0, i, screenWidth, i);
        }

        drawFromRobotCenter(g, robotImage, 0, 0, 0, 1, this);
        drawFromRobotCenter(g, odoImage, 0, Constants.SIDE_ODO_Y.getDouble(), 0, 0.2, this);
        drawFromRobotCenter(g, odoImage, 0, -Constants.SIDE_ODO_Y.getDouble(), 0, 0.2, this);
        drawFromRobotCenter(g, odoImage, Constants.CENTER_ODO_X.getDouble(), 0, 0.5*Math.PI, 0.2, this);

        drawGlobal(g, robot_estImage, UserCode.odometry.estPose.x, UserCode.odometry.estPose.y, UserCode.odometry.estPose.heading, robotScale, this);


        if(Main.robot.slippingL){
            drawFromRobotCenter(g, slipImage, 0, Constants.HALF_DIST_BETWEEN_WHEELS, 0, 0.5, this);
        }
        if(Main.robot.slippingR){
            drawFromRobotCenter(g, slipImage, 0, -Constants.HALF_DIST_BETWEEN_WHEELS, 0, 0.5, this);
        }

    }

    public int[] convertMeterToPixel(double xMeters, double yMeters){
        xMeters += 2;
        yMeters += 2;
        int pixelX = (int) Util.posModulo(xMeters * Constants.DISPLAY_SCALE.getDouble(), windowWidth); // robot position in pixels
        int pixelY = (int) Util.posModulo(yMeters * Constants.DISPLAY_SCALE.getDouble(), windowHeight);
        return new int[] {pixelX, pixelY};
    }

    public void drawGlobal(Graphics g, BufferedImage image, double xMeters, double yMeters, double heading, double scale, ImageObserver imageObserver){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform initialTransform = g2d.getTransform();

        int[] pixelPos = convertMeterToPixel(xMeters, yMeters);

        g.translate(pixelPos[0] + image.getWidth()/2, pixelPos[1] + image.getWidth()/2);
        g2d.scale(scale, scale);
        g2d.rotate(heading);

        g.drawImage(image, -image.getWidth()/2, -image.getHeight()/2, imageObserver);

		g2d.setTransform(initialTransform);
    }

    public void drawFromRobotCenter(Graphics g, BufferedImage image, double offsetXMeters, double offsetYMeters, double offsetRotation, double scale, ImageObserver imageObserver){
        
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform initialTransform = g2d.getTransform();

        int[] robotPixelPos = convertMeterToPixel(Main.robot.globalPos.x, Main.robot.globalPos.y);

        g.translate(robotPixelPos[0] + robotImage.getWidth()/2, robotPixelPos[1] + robotImage.getWidth()/2);
        g2d.scale(robotScale, robotScale);
        g2d.rotate(Main.robot.heading);

        g.translate(
            (int) (offsetXMeters * Constants.DISPLAY_SCALE.getDouble() / robotScale), 
            (int) (offsetYMeters * Constants.DISPLAY_SCALE.getDouble() / robotScale));
        g2d.scale(scale, scale);
        g2d.rotate(offsetRotation);
        g.drawImage(image, -image.getWidth()/2, -image.getHeight()/2, imageObserver);

		g2d.setTransform(initialTransform);
    }
    
	public static void init(){
		screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        
		try {
			robotImage = ImageIO.read(new File("./robot.png"));
			slipImage = ImageIO.read(new File("./slip.png"));
            odoImage = ImageIO.read(new File("./odo.png"));
            robot_estImage = ImageIO.read(new File("./robot_est.png"));

		} catch (IOException e) {
			e.printStackTrace();
        }
        updateScales();

		frame = new JFrame("Robot Sim");
		sim = new GraphicSim();
		frame.add(sim);
		frame.setSize((int) screenWidth-200, (int) screenHeight);
		frame.setLocation(200, 0);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

    public static void updateScales(){
        robotImgHeight = robotImage.getWidth();
		robotDisplayWidth = (int) (Constants.DISPLAY_SCALE.getDouble() * Constants.ROBOT_WIDTH.getDouble()); //width of robot in pixels
		robotScale = (double) robotDisplayWidth / robotImgHeight; //scaling robot image to fit display width
    }
	

    public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
        System.out.println("mouseClicked");
	}


}