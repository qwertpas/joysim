package sim3;

public class UserCode{


    public static void initialize(){
        System.out.println("hello, I just initialized!");
    }

    public static void execute(){

        double x = Controls.rawX * 0.3;
        double y = Controls.rawY * 1.0;

        // System.out.println(Robot.leftMotor.getDistance());
        // System.out.println(Robot.rightMotor.getDistance());

        double leftPower = y - x;
        double rightPower = y + x;
        
        Robot.setDrivePowers(leftPower, rightPower);
        
    }




}