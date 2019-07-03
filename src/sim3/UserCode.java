package sim3;

import java.awt.Color;
import java.util.Arrays;

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
        debugInit();
    }

    public static void execute(){ //don't delete this function; it is called by Robot.java
        motion = motionProfile.getPoint(Robot.elaspedTime);
        debugLoop();

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

        // Robot.setDrivePowers(1, 1);

        
        // System.out.println("Diff dist: " + (Util.inchesToMeters(Robot.leftEncoderDist()) - motion.dist));

        // double averageDist = (Robot.leftEncoderDist() + Robot.rightEncoderDist()) / 2.0;
        // pid.loop(averageDist, 100);
        // Robot.setDrivePowers(pid.getPower(), pid.getPower());
        // System.out.println(pid.getPower());
        
        
        
    }

    //user's util functions
    private static void debugInit(){

        //target position
        GraphicDebug.position.addSerie(Color.BLACK, 5);
        GraphicDebug.position.series.get(0).on = true;
        //current position
        GraphicDebug.position.addSerie(Color.RED, 4);
        GraphicDebug.position.series.get(1).on = true;
        
        //target velocity
        GraphicDebug.velocity.addSerie(Color.BLACK, 5);
        GraphicDebug.velocity.series.get(0).on = true;
        //current velocity
        GraphicDebug.velocity.addSerie(Color.RED, 4);
        GraphicDebug.velocity.series.get(1).on = true;
        
    }

    final static double time_scale = 80;
    final static double dist_scale = 1;
    final static double velo_scale = 3;

    private static void debugLoop(){
        GraphicDebug.position.series.get(0).addPoint( time_scale * Robot.elaspedTime, dist_scale * motion.dist);
        GraphicDebug.position.series.get(1).addPoint( time_scale * Robot.elaspedTime, dist_scale * Robot.leftEncoderDist());

        GraphicDebug.velocity.series.get(0).addPoint( time_scale * Robot.elaspedTime, velo_scale * motion.velo);
        GraphicDebug.velocity.series.get(1).addPoint( time_scale * Robot.elaspedTime, velo_scale * Util.metersToInches(Robot.physics.linVelo));
    }




}