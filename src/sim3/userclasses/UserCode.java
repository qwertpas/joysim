package sim3.userclasses;

import java.awt.Color;

import sim3.Controls;
import sim3.GraphicDebug;
import sim3.Main;
import sim3.Robot;
import sim3.Util;
import sim3.GraphicDebug.Serie;


public class UserCode{

    public static double lPower;
    public static double rPower;

    public static void initialize(){ //this function is run once when the robot starts
        GraphicDebug.turnOnAll(); //displaying the graphs
    }

    public static void execute(){ //this function is run 50 times a second (every 0.02 second)

        lPower = -Controls.rawY - Controls.rawX;
        rPower = -Controls.rawY + Controls.rawX;

        setDrivePowers(lPower, rPower); //power ranges from -1 to 1

        graph(); //updating the graphs
    }

    private static void setDrivePowers(double lPower, double rPower){
        Main.robot.leftGearbox.setPower(lPower);
        Main.robot.rightGearbox.setPower(rPower);
    }


    // Motion graphs
    static Serie currentPositionSerie = new Serie(Color.BLUE, 3);
    static GraphicDebug positionWindow = new GraphicDebug("Position", new Serie[]{currentPositionSerie});

    static Serie currentVelocitySerie = new Serie(Color.BLUE, 3);
    static GraphicDebug velocityWindow = new GraphicDebug("Velocity", new Serie[]{currentVelocitySerie});
    
    private static void graph(){
        currentPositionSerie.addPoint(Main.elaspedTime, Main.robot.leftEncoderPosition());
        currentVelocitySerie.addPoint(Main.elaspedTime, Main.robot.leftEncoderVelocity());

        GraphicDebug.paintAll();
    }




}