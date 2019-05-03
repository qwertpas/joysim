package sim3;

public class UserCode{


    public static void initialize(){
        System.out.println("hello, I just initialized!");
    }

    public static void execute(){

        double leftPower = Controls.rawY - Controls.rawX;
        double rightPower = Controls.rawY + Controls.rawX;
        
        Robot.leftMotor.setVoltage(leftPower);
        Robot.rightMotor.setVoltage(rightPower);
        
    }




}