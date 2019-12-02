package sim3;

import java.awt.Color;
import sim3.GraphicDebug.Serie;


public class UserCode{

    static double lPower;
    static double rPower;

    public static void initialize(){ //this function is run once when the robot starts
        GraphicDebug.turnOnAll(); //displaying the graphs
    }

    public static void execute(){ //this function is run 50 times a second (every 0.02 second)

        //set powers to the simulated robot drivetrain
        lPower = 0.5;
        rPower = 0.5;
        
        Robot.setDrivePowers(lPower, rPower); //power ranges from -1 to 1

        /**
         * Try this if you want to control the robot using a joystick or mouse coordinates:
         * 
         *      lPower = Controls.rawY - Controls.rawX
         *      rPower = Controls.rawY + Controls.rawX
         * 
         * The robot may move unexpectedly at first. Mess around with scaling or negatives
         * until the controls feel intuitive to you.
         */        


        graph(); //updating the graphs
    }





    // Motion graphs
    static Serie currentPositionSerie = new Serie(Color.BLUE, 3);
    static GraphicDebug positionWindow = new GraphicDebug("Position", new Serie[]{currentPositionSerie});

    static Serie currentVelocitySerie = new Serie(Color.BLUE, 3);
    static GraphicDebug velocityWindow = new GraphicDebug("Velocity", new Serie[]{currentVelocitySerie});
    
    private static void graph(){
        currentPositionSerie.addPoint(Robot.elaspedTime, Robot.leftEncoderDist());
        currentVelocitySerie.addPoint(Robot.elaspedTime, Util.metersToInches(Robot.physics.linVelo));

        GraphicDebug.paintAll();
    }




}