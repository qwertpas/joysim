package sim3;

import java.awt.Color;

import sim3.GraphicDebug.Serie;
import sim3.Util.MotionProfile;
import sim3.Util.MotionProfile.MotionProfilePoint;

public class UserCode{

    static volatile MotionProfile motionProfile;
    static volatile MotionProfilePoint motion;
    static volatile double power;

    public static void initialize(){ //don't delete this function; it is called by Robot.java
        GraphicDebug.turnOnAll();
    }

    public static void execute(){ //don't delete this function; it is called by Robot.java
        
        Robot.setDrivePowers(power, power);
        graph();
    }

    // Graphs
    static Serie currentPositionSerie = new Serie(Color.BLUE, 3);
    static Serie targetPositionSerie = new Serie(Color.RED, 3);
    static GraphicDebug positionWindow = new GraphicDebug("Position", new Serie[]{currentPositionSerie, targetPositionSerie});

    static Serie currentVelocitySerie = new Serie(Color.BLUE, 3);
    static Serie targetVelocitySerie = new Serie(Color.RED, 3);
    static GraphicDebug velocityWindow = new GraphicDebug("Velocity", new Serie[]{currentVelocitySerie, targetVelocitySerie});
    
    private static void graph(){
        currentPositionSerie.addPoint(Robot.elaspedTime, Robot.leftEncoderDist());
        targetPositionSerie.addPoint(Robot.elaspedTime, motion.dist);

        currentVelocitySerie.addPoint(Robot.elaspedTime, Util.metersToInches(Robot.physics.linVelo));
        targetVelocitySerie.addPoint(Robot.elaspedTime, motion.velo);
        
        GraphicDebug.paintAll();
    }




}