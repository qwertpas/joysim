package sim3;

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

	static Image robotImage;
	static Image turnCenterImage;

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
		screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int x = (int) posModulo(Robot.physics.x * Constants.DISPLAY_SCALE.getDouble(), screenWidth);
		int y = (int) posModulo(Robot.physics.y * Constants.DISPLAY_SCALE.getDouble(), screenHeight);

		g.drawString("left RPM "+ Robot.leftMotor.getDistance(), 500, 700);
		g.drawString("right RPM "+ Robot.rightMotor.getDistance(), 500, 750);
		g.drawString("linVelo fps" + Util.metersToFeet(Robot.physics.linVelo), 500, 800);
		
		g2d.scale(robotScale, robotScale);

		int robotCenterX = x + robotDisplayWidth/2;
		int robotCenterY = y + robotDisplayWidth/2;

		g2d.rotate(Robot.physics.heading, robotCenterX, robotCenterY);

		// g.drawImage(turnCenterImage, x, y, this);


		g.drawImage(robotImage, x, y, this);
    }
    
	public static void init(){
		screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		try {
			File robotFile = new File("./robot.png");
			File turnCenterFile = new File("./turnCenter.png");

			robotImage = ImageIO.read(robotFile);
			turnCenterImage = ImageIO.read(turnCenterFile);
			setDisplayScales(robotFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame("Joystick Sim");
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