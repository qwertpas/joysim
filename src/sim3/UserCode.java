package sim3;

import java.awt.Color;
import java.util.Arrays;

import sim3.GraphicDebug.Serie;
import sim3.Util.MotionProfile;
import sim3.Util.MotionProfile.MotionProfilePoint;

public class UserCode{

    // private static PID pid = new PID(0.007, 0, 0, 0);
    static volatile MotionProfile motionProfile;
    static volatile MotionProfilePoint motion;
    static volatile double power;

    public static void initialize(){ //don't delete this function; it is called by Robot.java
        motionProfile = new MotionProfile(Util.metersToInches(3), //max velocity
                                          Util.metersToInches(2), //max acceleration
                                          Util.metersToInches(-2.5), //min acceleration
                                          360 ); //target distance

        
                    
        System.out.println("isTrapezoid profile: " + motionProfile.isTrapezoid);
        System.out.println("time: " + Arrays.toString(motionProfile.times));
        System.out.println("initted usercode");
        motion = motionProfile.getPoint(0); //initial motion point
        GraphicDebug.turnOnAll();
    }

    public static void execute(){ //don't delete this function; it is called by Robot.java
        motion = motionProfile.getPoint(Robot.elaspedTime);

        double fric_feed = 0.1;
        double x_error = motion.dist - Robot.leftEncoderDist();
        double v_error = motion.velo - Util.metersToInches(Robot.physics.linVelo);

        // double v_error = motion.dist - Robot.physics.linVelo;
        // double power = (motion.dist * 0.00) + 
        //                (motion.velo * 0.2) + 
        //                (motion.accel * 0.0);

        if(!motionProfile.done){
            power = (Math.copySign(fric_feed, Robot.physics.linVelo)) + 
            (0.1 * x_error) +
            (0.1 * v_error);  
        } else {
            power = 0;
        }
              
        Robot.setDrivePowers(power, power);
        // Robot.setDrivePowers(-Controls.rawY + 0.2*Controls.rawX, -Controls.rawY - 0.2*Controls.rawX);

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