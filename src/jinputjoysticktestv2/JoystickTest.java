package jinputjoysticktestv2;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

/**
 *
 * Joystick Test with JInput
 *
 *
 * @author TheUzo007 
 *         http://theuzo007.wordpress.com
 *
 * Created 22 Oct 2013
 *
 */
public class JoystickTest {
    
    public static void main(String args[]) {
        //JInputJoystickTest jinputJoystickTest = new JInputJoystickTest();
        // Writes (into console) informations of all controllers that are found.
        //jinputJoystickTest.getAllControllersInfo();
        // In loop writes (into console) all joystick components and its current values.
        //jinputJoystickTest.pollControllerAndItsComponents(Controller.Type.STICK);
        //jinputJoystickTest.pollControllerAndItsComponents(Controller.Type.GAMEPAD);
        
        new JoystickTest();
    }
    
    
    final JFrameWindow window;
    private ArrayList<Controller> foundControllers;

    public JoystickTest() {
        window = new JFrameWindow();
        
        foundControllers = new ArrayList<>();
        searchForControllers();
        
        // If at least one controller was found we start showing controller data on window.
        if(!foundControllers.isEmpty())
            startShowingControllerData();
        else
            window.addControllerName("No controller found!");
    }

    /**
     * Search (and save) for controllers of type Controller.Type.STICK,
     * Controller.Type.GAMEPAD, Controller.Type.WHEEL and Controller.Type.FINGERSTICK.
     */
    private void searchForControllers() {
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
                
                // Add new controller to the list on the window.
                window.addControllerName(controller.getName() + " - " + controller.getType().toString() + " type");
            }
        }
    }
    
    /**
     * Starts showing controller data on the window.
     */
    public double sensCurve(double power) {
		return power * Math.abs(power);
    }
	
    
    
    
    private void startShowingControllerData(){
    	
        double xAxisScaled = 0;
        double yAxisScaled = 0;
    	
        
        
        
        	
    
        
    
    
        while(true)
        {
            // Currently selected controller.
            int selectedControllerIndex = window.getSelectedControllerName();
            Controller controller = foundControllers.get(selectedControllerIndex);

            // Pull controller for current data, and break while loop if controller is disconnected.
            if( !controller.poll() ){
                System.out.println("showControllerDisconnected");
                break;
            }
            

            
            
            
            // Go trough all components of the controller.
            Component[] components = controller.getComponents();
            for(int i=0; i < components.length; i++)
            {
                Component component = components[i];
                Identifier componentIdentifier = component.getIdentifier();
                
                // Buttons
                //if(component.getName().contains("Button")){ // If the language is not english, this won't work.
                if(componentIdentifier.getName().matches("^[0-9]*$")){ // If the component identifier name contains only numbers, then this is a button.
                    // Is button pressed?
                    boolean isItPressed = true;
                    if(component.getPollData() == 0.0f)
                        isItPressed = false;
                    
                    // Button index
                    String buttonIndex;
                    buttonIndex = component.getIdentifier().toString();
                    
                    // Create and add new button to panel.
                    JToggleButton aToggleButton = new JToggleButton(buttonIndex, isItPressed);
                    aToggleButton.setPreferredSize(new Dimension(48, 25));
                    aToggleButton.setEnabled(false);
                    
                    // We know that this component was button so we can skip to next component.
                    continue;
                }

                // Axes
                if(component.isAnalog()){
                    float axisValue = component.getPollData();
                    int axisValueInPercentage = getAxisValueInPercentage(axisValue);
                    // X axis
                    if(componentIdentifier == Component.Identifier.Axis.X){
             
                    	 xAxisScaled = Math.round(sensCurve(axisValueInPercentage - 50) * 0.00016 * 10000d) / 10000d;                        
                         
                    	 
                    	 
                        
                        System.out.println( "X = " + xAxisScaled);
                        
                        
                        
                        
                        
                        
                        continue; 
                    }
                    // Y axis
                    if(componentIdentifier == Component.Identifier.Axis.Y){
                    	
                    	
                    	
                    	
                    	
                   	 yAxisScaled = Math.round(sensCurve((axisValueInPercentage - 50)*0.1) * -0.016 * 5000d) / 5000d;                        
                        
                        
                        System.out.println( "Y = " + yAxisScaled
                        		);

                        
                        
                        
                        
                        continue;
                    }
                    
                   
                  
                }
            }
            
            // Now that we go trough all controller components,
            // we add butons panel to window,
           
            
            // We have to give processor some rest.
            try {
                Thread.sleep(500
     );
            } catch (InterruptedException ex) {
                Logger.getLogger(JoystickTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
    /**
     * Given value of axis in percentage.
     * Percentages increases from left/top to right/bottom.
     * If idle (in center) returns 50, if joystick axis is pushed to the left/top 
     * edge returns 0 and if it's pushed to the right/bottom returns 100.
     * 
     * @return value of axis in percentage.
     */
    public int getAxisValueInPercentage(float axisValue)
    {
        return (int)(((2 - (1 - axisValue)) * 100) / 2);
    }
}
