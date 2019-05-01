package sim2;

public class Physics{

    double x = 0;
    double y = 0;
    double heading = 0;
    double v = 0;
    double angVelo = 0;
    double a = 0.1;
    double angAccel = 0.0;
    long lastTime;

    double torqueL;
    double torqueR;
    double forceNet;
    double turnCenter;
    Boolean isSlipping = false;

    void calcTurnCenter(double vL, double vR){
        turnCenter = Constants.HALF_DIST_BETWEEN_WHEELS * (vL + vR) / (vL - vR);
    }

    void calcNetForce(double torqueL, double torqueR){
        double forceL = torqueL / Constants.WHEEL_RADIUS;
        double forceR = torqueR / Constants.WHEEL_RADIUS;
        forceNet = forceL + forceR;
        if(forceNet > Constants.STATIC_FRIC){
            isSlipping = true;
            forceNet = Constants.KINE_FRIC;
        }
        else isSlipping = false;
    }

    public void init(){
        lastTime = System.nanoTime();
    }

    public void update(){
        double dt = (System.nanoTime() - lastTime) / 1e+9;

        // torqueL = Robot.leftMotor.calcGearedTorque(gearedVelocity);
        // torqueL = Robot.leftMotor.calcGearedTorque(gearedVelocity);

        calcNetForce(torqueL, torqueR);
        a = forceNet / Constants.ROBOT_MASS;
        v = v + a*dt;
        x = x + v*dt*Math.cos(heading);
        y = y + v*dt*Math.sin(heading);
        lastTime = System.nanoTime();
    }

    public String toString(){
        return x +" "+ y +" "+ heading +" "+ v +" "+ angVelo +" "+ a +" "+ angAccel +" "+ isSlipping;
    }

    


}