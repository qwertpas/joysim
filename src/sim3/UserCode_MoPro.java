package sim3;

import java.awt.Color;
import java.util.Arrays;

import sim3.GraphicDebug.Serie;
import sim3.Util.MotionProfile;
import sim3.Util.MotionProfile.MotionProfilePoint;

public class UserCode_MoPro{

    // private static PID pid = new PID(0.007, 0, 0, 0);
    static volatile MotionProfile motionProfile;
    static volatile MotionProfilePoint motion;
    static volatile double power;

    static long time;

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
        time = System.nanoTime();
    }

    public static void execute(){ //don't delete this function; it is called by Robot.java
        motion = motionProfile.getPoint(Robot.elaspedTime);

        double fric_feed = 0.1;
        double x_error = motion.dist - Robot.leftEncoderDist();
        double v_error = motion.velo - Util.metersToInches(Robot.physics.linVelo);

        if(!motionProfile.done){
            power = (Math.copySign(fric_feed, Robot.physics.linVelo)) + 
            (0.1 * x_error) +
            (0.1 * v_error);  
        } else {
            power = 0;
        }
        Robot.setDrivePowers(power, power);
        // if(System.nanoTime() - time < 5e9){
        //     Robot.setDrivePowers(0.5, 0.5);
        //     System.out.println("run");

        // }else{
        //     Robot.setDrivePowers(0, 0);
        //     System.out.println("stop");
        // }
        // Boolean  isQuickTurn = Controls.buttons.get(1);
        // double[] powers = Util.cheesyDrive(-Controls.rawY, -0.3 *Controls.rawX, isQuickTurn, false);
        // Robot.setDrivePowers(powers[0], powers[1]);

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