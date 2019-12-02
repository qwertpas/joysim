package sim3;

public class Physics{

    double x = 5;
    double y = 6;
    double heading = 0;

    double linVelo = 0;
    double angVelo = 0;

    double linAccel = 0.1;
    double angAccel = 0;

    double veloL = 0;
    double veloR = 0;

    double distL = 0;
    double distR = 0;

    double torqueL;
    double torqueR;
    double torqueNet;
    double forceNet;
    Boolean slipping = false;

    static double dt;
    static double lastTime;

    

    public void init(){
        lastTime = System.nanoTime();
        Robot.leftMotor.setVoltage(0);
        Robot.rightMotor.setVoltage(0);
    }

    public void update(){
        dt = (System.nanoTime() - lastTime) / 1e+9; //change in time (seconds) used for integrating
        lastTime = System.nanoTime();


        torqueL = Robot.leftMotor.calcGearedTorque(veloL / Constants.WHEEL_RADIUS.getDouble());
        torqueR = Robot.rightMotor.calcGearedTorque(veloR / Constants.WHEEL_RADIUS.getDouble());

        double forceL = calcWheelForce(torqueL);
        double forceR = calcWheelForce(torqueR);

        torqueNet = calcTorqueNet(forceL, forceR); //newton*meters
        forceNet = forceL + forceR; //newtons

        angAccel = torqueNet / Constants.ROBOT_ROT_INERTIA; //rad per sec per sec
        linAccel = forceNet / Constants.ROBOT_MASS.getDouble(); //meters per sec per sec

        angVelo = angVelo + angAccel * dt;
        linVelo = linVelo + linAccel * dt;
        veloL = linVelo + Constants.HALF_DIST_BETWEEN_WHEELS * angVelo; //theoretical. untested if this model works
        veloR = linVelo - Constants.HALF_DIST_BETWEEN_WHEELS * angVelo;

        heading = heading + angVelo * dt; //integrating angVelo using physics equation
        distL = distL + veloL * dt; //acting as encoder since integrateVelocity() inside motor isn't working
        distR = distR + veloR * dt;

        x = x + linVelo * dt * Math.cos(heading); //for display purposes
        y = y + linVelo * dt * Math.sin(heading);
    }


    private double calcWheelForce(double torque){
        double force = torque / Constants.WHEEL_RADIUS.getDouble();
        if(force > Constants.STATIC_FRIC * 0.5){
            force = Constants.KINE_FRIC;
            slipping = true;
        } else slipping = false;
        return force;
    }

    private double calcTorqueNet(double forceL, double forceR){
        double torqueMotors = (forceL - forceR) * Constants.HALF_DIST_BETWEEN_WHEELS; //torque around center of robot
        //apply scrub
        torqueNet = Util.applyFrictions(torqueMotors, angVelo, Constants.WHEEL_SCRUB_STATIC, Constants.WHEEL_SCRUB_KINE, Constants.WHEEL_SCRUB_FRIC_THRESHOLD.getDouble());
        return torqueNet;
    }


    

    public String toString(){
        return x +" "+ y +" "+ heading +" "+ linVelo +" "+ angVelo +" "+ linAccel +" "+ angAccel;
    }

    


}