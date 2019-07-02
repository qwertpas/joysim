package sim3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicSim extends JPanel implements MouseListener {
	private static final long serialVersionUID = -87884863222799400L;

	static JFrame frame;

	static File robotFile;
	// static File turnCenter;
	static Image robotImage;
	// static Image turnCenterImage;

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
		double windowWidth = frame.getContentPane().getSize().getWidth();
		double windowHeight = frame.getContentPane().getSize().getHeight();
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int x = (int) Util.posModulo(Robot.physics.x * Constants.DISPLAY_SCALE.getDouble(), windowWidth); // in pixels
		int y = (int) Util.posModulo(Robot.physics.y * Constants.DISPLAY_SCALE.getDouble(), windowHeight);

		g.drawString("left encoder (in) "+ Util.metersToInches(Robot.physics.distL), 500, 700);
		g.drawString("right encoder (in) "+ Util.metersToInches(Robot.physics.distR), 500, 725);
		g.drawString("usercode power "+ Util.roundHundreths(UserCode.power), 500, 750);
		// g.drawString("right velo "+ Util.roundHundreths(Robot.physics.veloR), 500, 775);
		g.drawString("linVelo " + Util.roundHundreths(Util.metersToFeet(Robot.physics.linVelo)), 500, 800);
		g.drawString("veloL " + Util.roundHundreths(Util.metersToFeet(Robot.physics.veloL)), 500, 825);
		g.drawString("elapsed time " + Util.roundHundreths(Robot.elaspedTime), 500, 850);

		//drawing the grid
		g.setColor(Color.GRAY.brighter());
		for(int i = 0; i < screenWidth; i += Constants.DISPLAY_SCALE.getDouble() / Util.metersToFeet(1)){
			g.drawLine(i, 0, i, screenHeight);
		}
		for(int i = 0; i < screenHeight; i += Constants.DISPLAY_SCALE.getDouble() / Util.metersToFeet(1)){
			g.drawLine(0, i, screenWidth, i);
		}

		// g.setColor(Color.BLACK);

		// points.add(new Point((int) (UserCode.time * 10) + 400, (int) -(UserCode.motion.velo * 10) + 300));
		// for(Point point : points){
		// 	g.drawOval((int) point.getX(), (int) point.getY(), 1, 1);
		// }
		// g.drawOval((int) (UserCode.time * 5) + 200, (int) (UserCode.motion.velo * 2) + 300, 2, 2);
		// g.drawOval((int) (500), (int) (20) + 500, 5, 5);
		

		int robotCenterX = x + robotDisplayWidth/2;
		int robotCenterY = y + robotDisplayWidth/2;

		g2d.rotate(Robot.physics.heading, robotCenterX, robotCenterY);
		
		g2d.scale(robotScale, robotScale); // purpose is to scale robot image.
										   // also scales the pixels on the screen, so have to divide coordinates

		g.drawImage(robotImage, (int) (x / robotScale), (int) (y / robotScale), this);

		g.setColor(Color.GREEN);
		g.drawString("o", (int) (robotCenterX / robotScale), (int) (robotCenterY / robotScale));
    }
    
	public static void init(){
		screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		try {
			robotFile = new File("./robot.png");
			// turnCenterFile = new File("./turnCenter.png");

			robotImage = ImageIO.read(robotFile);
			// turnCenterImage = ImageIO.read(turnCenterFile);


			setDisplayScales(robotFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame = new JFrame("Joystick Sim");
		sim = new GraphicSim();
		frame.add(sim);
		frame.setSize((int) screenWidth, (int) screenHeight);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static void setDisplayScales(File file) throws IOException {
		BufferedImage bufferedImage = ImageIO.read(file);
		robotImgHeight = bufferedImage.getHeight();
		robotDisplayWidth = (int) (Constants.DISPLAY_SCALE.getDouble() * Constants.ROBOT_WIDTH.getDouble()); //width of robot in pixels
		robotScale = (double) robotDisplayWidth / robotImgHeight; //scaling robot image to fit display width.
	}

	public static void rescale(){
		try {
			setDisplayScales(robotFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
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