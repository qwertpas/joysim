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

	static Image image;
	static double screenHeight;
	static double screenWidth;
	static GraphicSim sim;

	static double robotImgHeight;
	static double robotDisplayWidth;
	static double robotScale;


  	GraphicSim() {
		addMouseListener(this);
	}

    @Override
	public void paint(Graphics g) { //gets called iteratively by JFrame
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);



		double x = (Robot.physics.x * Constants.DISPLAY_SCALE) % screenWidth;
		double y = (Robot.physics.y * Constants.DISPLAY_SCALE) % screenHeight;

		g.drawString("left torque "+ Robot.leftMotor.torque, 100, 700);
		g.drawString("right torque "+ Robot.rightMotor.torque, 100, 750);
		g.drawString(x+" , " + y, 100, 800);
		

		g2d.rotate(Robot.physics.heading, x, y);
		g2d.scale(robotScale, robotScale);
		g.drawImage(image, (int) x, (int) y, this);
    }
    
	public static void init(){
		screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		File file = new File("./robot.png");
		try {
			image = ImageIO.read(file);
			setDisplayScales(file);
			

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
		robotDisplayWidth = Constants.DISPLAY_SCALE * Constants.ROBOT_WIDTH; //width of robot in pixels
		robotScale = robotDisplayWidth / robotImgHeight; //scaling robot image to fit display width.
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