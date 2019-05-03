package sim3;




public class Robot {


    public static Motor leftMotor = new Motor(20, Motor.Model.CIM, 2);
    public static Motor rightMotor = new Motor(20, Motor.Model.CIM, 2);
    public static Physics physics = new Physics();

    public static void main(String[] args) {

        physics.init();
        while (true) {
            physics.update();
            // System.out.println(physics);

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // leftMotor.setVoltage(-12);
        // System.out.println(leftMotor.calcGearedTorque(-10000));
        // System.out.println(leftMotor.toString());

    }



}