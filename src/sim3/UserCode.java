package sim3;

import sim3.Util.MotionProfile;
import sim3.Util.PID;
import sim3.Util.MotionProfile.MotionProfilePoint;

public class UserCode{

    // private static PID pid = new PID(0.007, 0, 0, 0);
    static MotionProfile motionProfile;
    static MotionProfilePoint motion;
    static double startTime;
    static double time;

    public static void initialize(){
        motionProfile = new MotionProfile(3, 1, 1, 15); //in meters for now
        System.out.println("estimated time: " + motionProfile.times[3]);
        startTime = System.nanoTime() * 1e-9;
    }

    public static void execute(){
        time = (System.nanoTime() * 1e-9) - startTime;
        motion = motionProfile.getPoint(time);
        double power = (motion.dist * 0.00) + 
                       (motion.velo * 0.2) + 
                       (motion.accel * 0.0);
        
        Robot.setDrivePowers(power, power);
        System.out.println("Diff dist: " + (Util.inchesToMeters(Robot.leftEncoderDist()) - motion.dist));

        // double averageDist = (Robot.leftEncoderDist() + Robot.rightEncoderDist()) / 2.0;
        // pid.loop(averageDist, 100);
        // Robot.setDrivePowers(pid.getPower(), pid.getPower());
        // System.out.println(pid.getPower());
        
        
        
    }




}