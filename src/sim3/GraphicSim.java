package sim3;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

    GraphicSim() {
		addMouseListener(this);
	}

    @Override
	public void paint(Graphics g) { //gets called iteratively by JFrame
		double scale = 72 / Constants.ROBOT_WIDTH; //pixels per meter

		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.rotate(Robot.physics.heading, Robot.physics.x*scale+36, Robot.physics.y*scale+36);
		g.drawImage(image, (int) Math.round(Robot.physics.x * scale), (int) Math.round(Robot.physics.y*scale), this);
    }
    
    public static void init(){
		screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        File file = new File("./robot.png");
		try {
			image = ImageIO.read(file);
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