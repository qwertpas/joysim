package sim3;

import sim3.userclasses.UserCode;

public class Main {

    public static Boolean paused = true;

    public static double startTime;
    public static double pausedTime;
    public static double elaspedTime;

    public static Motor leftMotor = new Motor();
    public static Motor rightMotor = new Motor();
    public static Robot robot;
    public static GraphicDebug debug;


    public static void main(String[] args) {

        robot = new Robot();

        robot.init();
        GraphicSim.init();
        Controls.init();
        

        new GraphicInput().setVisible(true);

        new UserCodeThread();

        startTime = System.nanoTime() * 1e-9;
        while (true) {

            while(!paused){
                elaspedTime = (System.nanoTime() * 1e-9) - pausedTime - startTime;
                robot.update();
                if(Constants.printJoystick) System.out.println(Controls.rawX + " " + Controls.rawY);
                GraphicSim.sim.repaint();
            }

            System.out.println(Constants.ROBOT_ROT_INERTIA);


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

    
    

}