package sim3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicSim extends JPanel implements MouseListener {

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

		int x = (int) posModulo(Robot.physics.x * Constants.DISPLAY_SCALE.getDouble(), windowWidth); // in pixels
		int y = (int) posModulo(Robot.physics.y * Constants.DISPLAY_SCALE.getDouble(), windowHeight);

		g.drawString("left encoder (in)"+ Robot.leftMotor.getDistance(), 500, 700);
		g.drawString("right encoder (in) "+ Robot.rightMotor.getDistance(), 500, 750);
		g.drawString("feet per sec" + Util.roundHundreths(Util.metersToFeet(Robot.physics.linVelo)), 500, 800);

		//drawing the grid
		g.setColor(Color.GRAY.brighter());
		for(int i = 0; i < screenWidth; i += Constants.DISPLAY_SCALE.getDouble() / Util.metersToFeet(1)){
			g.drawLine(i, 0, i, screenHeight);
		}
		for(int i = 0; i < screenHeight; i += Constants.DISPLAY_SCALE.getDouble() / Util.metersToFeet(1)){
			g.drawLine(0, i, screenWidth, i);
		}
		

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

	private static double posModulo(double input, double modulo){
		while(input >= modulo) input -= modulo;
		while(input < 0) input += modulo;
		return input;
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
		System.out.println("CLKICKED");
	}


}