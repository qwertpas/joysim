package sim3;

import java.awt.Color;
import java.awt.Point;

import sim3.Util.MotionProfile;
import sim3.Util.PID;
import sim3.Util.MotionProfile.MotionProfilePoint;

public class UserCode{

    // private static PID pid = new PID(0.007, 0, 0, 0);
    static volatile MotionProfile motionProfile;
    static volatile MotionProfilePoint motion;
    static volatile double power;

    public static void initialize(){ //don't delete this function; it is called by Robot.java
        motionProfile = new MotionProfile(Util.metersToInches(3), //max velocity
                                          Util.metersToInches(1), //max acceleration
                                          Util.metersToInches(-1), //min acceleration
                                          Util.metersToInches(15) ); //target distance

        
        // motionProfile = new MotionProfile(3*10, 1*10, -1*10, 15*10);
                    
        System.out.println("isTrapezoid profile: " + motionProfile.isTrapezoid);
        System.out.println("estimated time: " + motionProfile.times[motionProfile.times.length-1]);
        System.out.println("initted usercode");
        motion = motionProfile.getPoint(0); //initial motion point
        debugInit();
    }

    public static void execute(){ //don't delete this function; it is called by Robot.java
        motion = motionProfile.getPoint(Robot.elaspedTime);
        debugLoop();

        double fric_feed = 0.2;

        double x_error = motion.dist - Robot.leftEncoderDist();

        // double v_error = motion.dist - Robot.physics.linVelo;
        // double power = (motion.dist * 0.00) + 
        //                (motion.velo * 0.2) + 
        //                (motion.accel * 0.0);

        if(!motionProfile.done){
            power = fric_feed + 
                    (0.05 * x_error);
        } else {
            power = 0.0;
        }
        
        
    
        Robot.setDrivePowers(power, power);

        
        // System.out.println("Diff dist: " + (Util.inchesToMeters(Robot.leftEncoderDist()) - motion.dist));

        // double averageDist = (Robot.leftEncoderDist() + Robot.rightEncoderDist()) / 2.0;
        // pid.loop(averageDist, 100);
        // Robot.setDrivePowers(pid.getPower(), pid.getPower());
        // System.out.println(pid.getPower());
        
        
        
    }

    //user's util functions
    private static void debugInit(){
        //current position
        GraphicDebug.position.addSerie(Color.RED);
        GraphicDebug.position.series.get(0).on = true;
        //target velocity
        GraphicDebug.position.addSerie(Color.BLACK);
        GraphicDebug.position.series.get(1).on = true;

        //current velocity
        GraphicDebug.velocity.addSerie(Color.RED);
        GraphicDebug.velocity.series.get(0).on = true;
        //target velocity
        GraphicDebug.velocity.addSerie(Color.BLACK);
        GraphicDebug.velocity.series.get(1).on = true;
    }

    private static void debugLoop(){
        GraphicDebug.velocity.series.get(0).addPoint( 10 * Robot.elaspedTime, Util.metersToInches(Robot.physics.linVelo));
        GraphicDebug.velocity.series.get(1).addPoint( 10 * Robot.elaspedTime, motion.velo);

        GraphicDebug.position.series.get(0).addPoint( 10 * Robot.elaspedTime, 0.3 * Robot.leftEncoderDist());
        GraphicDebug.position.series.get(1).addPoint( 10 * Robot.elaspedTime, 0.3 * motion.dist);
    }




}