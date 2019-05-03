package sim2;

public class Physics{

    double x = 0;
    double y = 0;
    double heading = 0;
    double v = 0;
    double angVelo = 0;
    double a = 0.1;
    double angAccel = 0;
    long lastTime;

    double veloL = 0;
    double veloR = 0;

    double torqueL;
    double torqueR;
    double torqueNet;
    double forceNet;
    double turnCenter;
    Boolean LSlipping = false;
    Boolean RSlipping = false;

    double calcTurnCenter(double vL, double vR){
        return turnCenter = Constants.HALF_DIST_BETWEEN_WHEELS * (vL + vR) / (vL - vR);
    }

    double calcWheelForce(double torque){
        double force = torque / Constants.WHEEL_RADIUS;
        if(force > Constants.STATIC_FRIC * 0.5) force = Constants.KINE_FRIC;
        return force;
    }

    public void init(){
        lastTime = System.nanoTime();
    }

    public void update(){
        double dt = (System.nanoTime() - lastTime) / 1e+9;

        torqueL = Robot.leftMotor.calcGearedTorque(veloL / Constants.WHEEL_RADIUS);
        torqueR = Robot.rightMotor.calcGearedTorque(veloR / Constants.WHEEL_RADIUS);

        double forceL = calcWheelForce(torqueL);
        double forceR = calcWheelForce(torqueR);

        // torqueNet = Constants.HALF_DIST_BETWEEN_WHEELS * ;
        forceNet = forceL + forceR;

        a = forceNet / Constants.ROBOT_MASS;
        v = v + a*dt;
        x = x + v*dt*Math.cos(heading);
        y = y + v*dt*Math.sin(heading);
        lastTime = System.nanoTime();
    }

    public String toString(){
        return x +" "+ y +" "+ heading +" "+ v +" "+ angVelo +" "+ a +" "+ angAccel;
    }

    


}