package sim3;

public class UserCode{


    public static void initialize(){
        System.out.println("hello, I just initialized!");
    }

    public static void execute(){

        double x = Controls.rawX * 0.2;
        double y = Controls.rawY * 1.0;

        double leftPower = y - x;
        double rightPower = y + x;
        
        Robot.leftMotor.setVoltage(leftPower*12);
        Robot.rightMotor.setVoltage(rightPower*12);
        
    }




}