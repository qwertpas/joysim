package sim3;




public class Robot {

    public static Boolean paused = true;

    public static double startTime;
    public static double pausedTime;
    public static double elaspedTime;

    public static Motor leftMotor = new Motor(Constants.GEAR_RATIO.getDouble(), Motor.Model.CIM, Constants.MOTORS_PER_SIDE.getInt());
    public static Motor rightMotor = new Motor(Constants.GEAR_RATIO.getDouble(), Motor.Model.CIM, Constants.MOTORS_PER_SIDE.getInt());
    public static Physics physics;
    public static GraphicDebug debug;


    public static void main(String[] args) {

        physics = new Physics();

        physics.init();
        GraphicSim.init();
        Controls.init();
        

        new GraphicInput().setVisible(true);

        new UserCodeThread();

        startTime = System.nanoTime() * 1e-9;
        while (true) {

            while(!paused){
                elaspedTime = (System.nanoTime() * 1e-9) - pausedTime - startTime;
                physics.update();
                if(Constants.printPowers) System.out.println(Controls.rawX + " " + Controls.rawY);
                GraphicSim.sim.repaint();
            }


            try {
                Thread.sleep(5);
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
                if(!paused){
                    UserCode.execute();
                    Controls.updateControls();
                }
                try{
                    Thread.sleep(20);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
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

    public static double leftEncoderDist(){
        return Util.roundHundreths(Util.metersToInches(physics.distL));
    }

    public static double rightEncoderDist(){
        return Util.roundHundreths(Util.metersToInches(physics.distR));
    }

}