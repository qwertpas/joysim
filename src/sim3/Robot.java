package sim3;




public class Robot {

    public static Boolean paused = false;

    public static Motor leftMotor = new Motor(Constants.GEAR_RATIO.getDouble(), Motor.Model.CIM, Constants.MOTORS_PER_SIDE.getInt());
    public static Motor rightMotor = new Motor(Constants.GEAR_RATIO.getDouble(), Motor.Model.CIM, Constants.MOTORS_PER_SIDE.getInt());
    public static Physics physics = new Physics();

    public static void main(String[] args) {
        Controls.searchForControllers();
        physics.init();
        GraphicSim.init();

        new GraphicInput().setVisible(true);
        new UserCodeThread();

        //TODO: add pause button
        while (true) {

            while(!paused){
                Controls.updateControls();
                physics.update();
                if(Constants.printPowers) System.out.println(Controls.rawX + " " + Controls.rawY);
                GraphicSim.sim.repaint();
            }


            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class UserCodeThread implements Runnable{
        private boolean exit;
        Thread t;
        UserCodeThread() {
            t = new Thread(this, "usercode");
            System.out.println("New Thread: " + t);
            exit = false;
            t.start();
        }

        public void run(){
            UserCode.initialize();
            while(!exit) {
                UserCode.execute();
                try {
                    Thread.sleep(20); //20 millisecond delay or 50 times a second
                } catch (InterruptedException e) {}
            } //END of UserCodeThread.run().while{}
        } //END of UserCodeThread.run()

        public void stop(){
            exit = true;
        }
    } //END of UserCodeThread


    public static void setDrivePowers(double leftPower, double rightPower){
        Robot.leftMotor.setVoltage(leftPower*12);
        Robot.rightMotor.setVoltage(rightPower*12);
    }

}