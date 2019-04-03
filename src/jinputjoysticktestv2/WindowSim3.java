package jinputjoysticktestv2;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


@SuppressWarnings("serial")
public class WindowSim3 extends JPanel implements MouseListener{

	WindowSim3(){
		addMouseListener(this);
	}

	
	//turnfunc
	
	public static double[] joymap(double rawX, double rawY) {
		double x = 0.25 * sensCurve(rawX, 2);
		double y = sensCurve(rawY, 3);
		return new double[] {y+x, y-x};
	}
	
	private static final double kThrottleDeadband = 0.02;
    private static final double kWheelDeadband = 0.01;

    // These factor determine how fast the wheel traverses the "non linear" sine curve.
    private static final double kHighWheelNonLinearity = 0.65;
    private static final double kLowWheelNonLinearity = 0.5;

    private static final double kHighNegInertiaScalar = 4.0;

    private static final double kLowNegInertiaThreshold = 0.65;
    private static final double kLowNegInertiaTurnScalar = 3.5;
    private static final double kLowNegInertiaCloseScalar = 4.0;
    private static final double kLowNegInertiaFarScalar = 5.0;

    private static final double kHighSensitivity = 0.65;
    private static final double kLowSensitiity = 0.65;

    private static final double kQuickStopDeadband = 0.5;
    private static final double kQuickStopWeight = 0.1;
    private static final double kQuickStopScalar = 5.0;

    private double mOldWheel = 0.0;
    private double mQuickStopAccumlator = 0.0;
	private double mNegInertiaAccumlator = 0.0;
	
	private Boolean isQuickTurn = false;
	
	public double[] cheesyDrive(double throttle, double wheel, boolean isQuickTurn,
            boolean isHighGear) {

wheel = handleDeadband(wheel, kWheelDeadband);
throttle = handleDeadband(throttle, kThrottleDeadband);

double negInertia = wheel - mOldWheel;
mOldWheel = wheel;

double wheelNonLinearity;
if (isHighGear) {
wheelNonLinearity = kHighWheelNonLinearity;
final double denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity);
// Apply a sin function that's scaled to make it feel better.
wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
} else {
wheelNonLinearity = kLowWheelNonLinearity;
final double denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity);
// Apply a sin function that's scaled to make it feel better.
wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
}

double leftPwm, rightPwm, overPower;
double sensitivity;

double angularPower;
double linearPower;

// Negative inertia!
double negInertiaScalar;
if (isHighGear) {
negInertiaScalar = kHighNegInertiaScalar;
sensitivity = kHighSensitivity;
} else {
if (wheel * negInertia > 0) {
// If we are moving away from 0.0, aka, trying to get more wheel.
negInertiaScalar = kLowNegInertiaTurnScalar;
} else {
// Otherwise, we are attempting to go back to 0.0.
if (Math.abs(wheel) > kLowNegInertiaThreshold) {
negInertiaScalar = kLowNegInertiaFarScalar;
} else {
negInertiaScalar = kLowNegInertiaCloseScalar;
}
}
sensitivity = kLowSensitiity;
}
double negInertiaPower = negInertia * negInertiaScalar;
mNegInertiaAccumlator += negInertiaPower;

wheel = wheel + mNegInertiaAccumlator;
if (mNegInertiaAccumlator > 1) {
mNegInertiaAccumlator -= 1;
} else if (mNegInertiaAccumlator < -1) {
mNegInertiaAccumlator += 1;
} else {
mNegInertiaAccumlator = 0;
}
linearPower = throttle;

// Quickturn!
if (isQuickTurn) {
if (Math.abs(linearPower) < kQuickStopDeadband) {
double alpha = kQuickStopWeight;
mQuickStopAccumlator = (1 - alpha) * mQuickStopAccumlator
 + alpha * limit(wheel, 1.0) * kQuickStopScalar;
}
overPower = 1.0;
angularPower = wheel;
} else {
overPower = 0.0;
angularPower = Math.abs(throttle) * wheel * sensitivity - mQuickStopAccumlator;
if (mQuickStopAccumlator > 1) {
mQuickStopAccumlator -= 1;
} else if (mQuickStopAccumlator < -1) {
mQuickStopAccumlator += 1;
} else {
mQuickStopAccumlator = 0.0;
}
}

rightPwm = leftPwm = linearPower;
leftPwm += angularPower;
rightPwm -= angularPower;

if (leftPwm > 1.0) {
rightPwm -= overPower * (leftPwm - 1.0);
leftPwm = 1.0;
} else if (rightPwm > 1.0) {
leftPwm -= overPower * (rightPwm - 1.0);
rightPwm = 1.0;
} else if (leftPwm < -1.0) {
rightPwm += overPower * (-1.0 - leftPwm);
leftPwm = -1.0;
} else if (rightPwm < -1.0) {
leftPwm += overPower * (-1.0 - rightPwm);
rightPwm = -1.0;
}
return new double[] {leftPwm, rightPwm};
}
	
	public double handleDeadband(double val, double deadband) {
        return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
    }
	
	public double limit(double val, double limit) {
		if(val > limit) {
			return limit;
		}
		else if(val < -limit) {
			return -limit;
		}else {
			return val;
		}
	}
	
	
	
	
	//things to run windowsim
	
	static Image image;
	
	static double rawX;
	static double rawY;
	
	static double scaledX;
	static double scaledY;
	
	final double margin = 20;
	
	double theta = 0;
	double omega = 0;
	double alpha = 0;

	double x = 720;
	double y = 450;
	double v = 1;
	double a = 0;
	
	double linEfficiency = 0.95;
	double rotEfficiency = 0.5;
	
    private static ArrayList<Controller> foundControllers;
	
	

	public static double round(double number){
		return ((Math.round(number * 10000.0))/10000.0);
	}
	
	public static double sensCurve(double joystickVal, double exponent){
        return Math.copySign( Math.pow( Math.abs( joystickVal), exponent), joystickVal);
    }
	
	
	
	private void moveBall() {
		
//		double[] power = joymap(-scaledX, -scaledY);
		double[] power = cheesyDrive(-scaledY, -scaledX * 0.5, isQuickTurn, false);


		
		alpha = power[1] - power[0];
		omega = (omega + alpha) * rotEfficiency;
		theta = theta + omega;
		
		
		a = (power[0] + power[1])/2.0;
		v = (v + a) * linEfficiency;
		
		x = x + (v * -Math.sin(theta));
		y = y + (v * Math.cos(theta));
		
		
		if (x < margin) {
			x = margin;
			v = 0;
			System.out.println("hit left wall");
		}
		if (x > getWidth() - margin - 30) {
			x = getWidth() - margin - 30;
			v = 0;
			System.out.println("hit right wall");
		}
		if (y < margin) {
			y = margin;
			v = 0;
			System.out.println("hit top wall");
		}
		if (y > getHeight() - margin - 30) {
			y = getHeight() - margin - 30;
			v = 0;
			System.out.println("hit bottom wall");
		}
		
		System.out.println("a: " + a);
		System.out.println("v: " + v);
		System.out.println("x: " + x);
		System.out.println("y: " + y);
		System.out.println("alpha: " + alpha);
		System.out.println("theta: " + theta);
		System.out.println("rawX: " + rawX);
		System.out.println("rawY: " + rawY);

		

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.rotate(theta, x+32, y+32);
		g.drawImage(image, (int)Math.round(x), (int)Math.round(y), this);

	}

	public static void main(String[] args) throws InterruptedException {

		File file = new File("./robot.png");
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame("Joystick Sim");
		WindowSim3 game = new WindowSim3();
		frame.add(game);
		frame.setSize(1439, 899);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		foundControllers = new ArrayList<>();
        searchForControllers();
        
        // If at least one controller was found we start showing controller data on window.
        if(!foundControllers.isEmpty()) {
	        while(true) {
	            startShowingControllerData();

				game.moveBall();
				game.repaint();
				Thread.sleep(20);
	        }
		}else {
            System.out.println("No controller found, using mouse coords");
            
	        while(true) {
	        	getMouseData();
	        	
				game.moveBall();
				game.repaint();
				Thread.sleep(20);;
	        }
        }
		
		
		
		
		
	}
	
	private static void getMouseData() {
		rawX = (MouseInfo.getPointerInfo().getLocation().getX() - 720.0) / 720.0;
		rawY = (MouseInfo.getPointerInfo().getLocation().getY() - 450.0) / -450.0;
		scaledX = rawX * 0.1;
		scaledY = rawY * 0.5;
	}
	
	private static void startShowingControllerData(){
		
		
			
		
		// Currently selected controller.
        int selectedControllerIndex = 0;
        Controller controller = foundControllers.get(selectedControllerIndex);

        // Pull controller for current data, and break while loop if controller is disconnected.
        if( !controller.poll() ){
            System.out.println("ControllerDisconnected, using mouse coords");
            getMouseData();
            
        }
        

        
        // Go trough all components of the controller.
        Component[] components = controller.getComponents();
        for(int i=0; i < components.length; i++)
        {
            Component component = components[i];
            Identifier componentIdentifier = component.getIdentifier();
           

            // Axes
            if(component.isAnalog()){
                float axisValue = component.getPollData();
                // X axis
                
        		System.out.println("using joystick");

                if(componentIdentifier == Component.Identifier.Axis.X){
                	rawX = axisValue;
                	scaledX = rawX * 0.2;
                	
                  	System.out.println("X = "  + axisValue); 
                    continue; 
                }
                // Y axis
                if(componentIdentifier == Component.Identifier.Axis.Y){
                	rawY = -axisValue;
            		scaledY = rawY * 0.75;

                	System.out.println("Y = " + axisValue);
                    continue;
                }    
            }
        }
		}
	
	
	private static void searchForControllers() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for(int i = 0; i < controllers.length; i++){
            Controller controller = controllers[i];
            
            if (
                    controller.getType() == Controller.Type.STICK || 
                    controller.getType() == Controller.Type.GAMEPAD || 
                    controller.getType() == Controller.Type.WHEEL ||
                    controller.getType() == Controller.Type.FINGERSTICK
               )
            {
                // Add new controller to the list of all controllers.
                foundControllers.add(controller);
                
            }
        }
	}
	



	
    public void mousePressed(MouseEvent e) {
		isQuickTurn = true;
	 }
 
	 public void mouseReleased(MouseEvent e) {
		isQuickTurn = false;
	 }
 
	 public void mouseEntered(MouseEvent e) {
	 }
 
	 public void mouseExited(MouseEvent e) {
	 }
 
	 public void mouseClicked(MouseEvent e) {
		System.out.println("CLKICKED");

	 }
	
}