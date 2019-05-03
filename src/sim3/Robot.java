package sim3;




public class Robot {


    public static Motor leftMotor = new Motor(20, Motor.Model.CIM, 1);
    public static Motor rightMotor = new Motor(20, Motor.Model.CIM, 1);
    public static Physics physics = new Physics();

    public static void main(String[] args) {
        Controls.searchForControllers();
        physics.init();
        GraphicSim.init();
        
        new UserCodeThread();

        while (true) {

            Controls.updateControls();
            physics.update();
            System.out.println(Controls.rawX + " " + Controls.rawY);
            GraphicSim.sim.repaint();

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

}