package sim2;

public class Robot {

    public static Motor leftMotor = new Motor(1, Motor.Model.CIM, 1);
    public static Motor rightMotor = new Motor(1, Motor.Model.CIM, 1);
    public static Physics physics = new Physics();

    public static void main(String[] args) {

        // physics.init();
        // while (true) {
        //     physics.update();
        //     System.out.println(physics);

        //     try {
        //         Thread.sleep(20);
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }
        leftMotor.setVoltage(12);
        System.out.println(leftMotor.calcGearedTorque(5000));

    }



}