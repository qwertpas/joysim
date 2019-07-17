package sim3;

import java.awt.MouseInfo;
import java.util.ArrayList;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Component.Identifier;

public class Controls {

    public static double rawX, rawY = 0;
    public static Boolean usingMouse = false;

    static ArrayList<Controller> foundControllers;

    public static void searchForControllers() {
        foundControllers = new ArrayList<>();
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

		for (int i = 0; i < controllers.length; i++) {
			Controller controller = controllers[i];
			if (controller.getType() == Controller.Type.STICK || controller.getType() == Controller.Type.GAMEPAD
					|| controller.getType() == Controller.Type.WHEEL
					|| controller.getType() == Controller.Type.FINGERSTICK) {
				// Add new controller to the list of all controllers.
                foundControllers.add(controller);
			}
        }

        if(foundControllers.size() == 0){
            System.out.println("No Controller found, using mouse coords");
            usingMouse = true;
        }else{
            System.out.println("Found controllers : " + foundControllers);
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        // for testing joystick
        searchForControllers();
        while(true){
            updateControls();
            Thread.sleep(100);
        }
    }

    static void updateControls() {
		if (usingMouse) {
			getMouseData();
		} else {
            getControllerData(foundControllers.get(Constants.CONTROLLER_INDEX.getInt()));
        }
    }
    
    private static void getMouseData() {
		rawX = (MouseInfo.getPointerInfo().getLocation().getX() - (GraphicSim.screenWidth / 2)) / (GraphicSim.screenWidth / 2);
		rawY = (MouseInfo.getPointerInfo().getLocation().getY() - (GraphicSim.screenHeight / 2)) / -(GraphicSim.screenHeight / 2);
    }
    
    private static void getControllerData(Controller controller){
        if(!controller.poll()){ //polls controller, if it returns false then polling has failed
            System.out.println("Controller disconnected, using mouse");
            usingMouse = true;
            return;
        }
        Component[] components = controller.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            Identifier componentIdentifier = component.getIdentifier();
            float value = component.getPollData();

            if(componentIdentifier == Component.Identifier.Axis.X){
                rawX = value;
            }else if(componentIdentifier == Component.Identifier.Axis.Y){
                rawY = value;
            }
                
            
        }
    }


}