package sim3.userclasses;

import java.awt.Color;

import sim3.Controls;
import sim3.GraphicDebug;
import sim3.Main;
import sim3.Util;
import sim3.GraphicDebug.Serie;


public class UserCode{

    public static double lPower;
    public static double rPower;

    public static void initialize(){ //this function is run once when the robot starts
        GraphicDebug.turnOnAll(); //displaying the graphs
    }

    public static void execute(){ //this function is run 50 times a second (every 0.02 second)


        double[] powers = Util.cheesyDrive(-Controls.rawY, Controls.rawX*0.4, true, false);

        lPower = powers[0];
        rPower = powers[1];

        setDrivePowers(lPower, rPower); //power ranges from -1 to 1

        graph(); //updating the graphs
    }

    private static void setDrivePowers(double lPower, double rPower){
        Main.robot.leftGearbox.setPower(lPower);
        Main.robot.rightGearbox.setPower(rPower);
    }


    // Motion graphs
    static Serie currentLPositionSerie = new Serie(Color.RED, 3);
    static Serie currentRPositionSerie = new Serie(Color.GREEN, 3);
    static GraphicDebug positionWindow = new GraphicDebug("Position", new Serie[]{currentLPositionSerie, currentRPositionSerie}, 100);

    static Serie currentLVelocitySerie = new Serie(Color.RED, 3);
    static Serie currentRVelocitySerie = new Serie(Color.GREEN, 3);
    static GraphicDebug velocityWindow = new GraphicDebug("Velocity", new Serie[]{currentLVelocitySerie, currentRVelocitySerie}, 100);
    
    private static void graph(){
        currentLPositionSerie.addPoint(Main.elaspedTime, Main.robot.leftGearbox.getAvgEncoderPosition());
        currentRPositionSerie.addPoint(Main.elaspedTime, Main.robot.rightGearbox.getAvgEncoderPosition());
        currentLVelocitySerie.addPoint(Main.elaspedTime, Main.robot.leftGearbox.getAvgEncoderVelocity());
        currentRVelocitySerie.addPoint(Main.elaspedTime, Main.robot.rightGearbox.getAvgEncoderVelocity());

        GraphicDebug.paintAll();
    }




}