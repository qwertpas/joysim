package sim3;

import sim3.Util.MotionProfile;
import sim3.Util.PID;

public class UserCode{

    private static PID pid = new PID(0.007, 0, 0, 0);

    public static void initialize(){
        System.out.println("hello, I just initialized!");
    }

    public static void execute(){

        double averageDist = (Robot.leftEncoderDist() + Robot.rightEncoderDist()) / 2.0;

        pid.loop(averageDist, 100);
        
        Robot.setDrivePowers(pid.getPower(), pid.getPower());

        System.out.println(pid.getPower());
        
        
        
    }




}