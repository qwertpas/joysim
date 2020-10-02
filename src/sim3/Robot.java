package sim3;

import sim3.Util.Vector2D;
import sim3.Util.Vector2D.Type;

public class Robot{

    Vector2D globalPos = new Vector2D();
    double heading = 0;

    public double linVelo = 0;
    double angVelo = 0;

    double linAccel = 0;
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

    public Gearbox leftGearbox = new Gearbox(2);
    public Gearbox rightGearbox = new Gearbox(2);

    public void init(){
        lastTime = System.nanoTime();
        leftGearbox.setPower(0);
        rightGearbox.setPower(0);
    }

    public void update(){
        dt = (System.nanoTime() - lastTime) / 1e+9; //change in time (seconds) used for integrating
        lastTime = System.nanoTime();

        leftGearbox.update(veloL / Constants.WHEEL_RADIUS.getDouble());
        rightGearbox.update(veloR / Constants.WHEEL_RADIUS.getDouble());

        torqueL = leftGearbox.getOutputTorque();
        torqueR = rightGearbox.getOutputTorque();

        double forceL = calcWheelForce(torqueL) + Constants.TURN_ERROR.getDouble();
        double forceR = calcWheelForce(torqueR) - Constants.TURN_ERROR.getDouble();

        torqueNet = calcTorqueNet(forceL, forceR); //newton*meters
        forceNet = forceL + forceR; //newtons

        angAccel = torqueNet / Constants.ROBOT_ROT_INERTIA; //rad per sec per sec
        linAccel = forceNet / Constants.ROBOT_MASS.getDouble(); //meters per sec per sec

        angVelo = angVelo + angAccel * dt;
        linVelo = linVelo + linAccel * dt;
        veloL = linVelo - Constants.HALF_DIST_BETWEEN_WHEELS * angVelo;
        veloR = linVelo + Constants.HALF_DIST_BETWEEN_WHEELS * angVelo;

        heading = heading + angVelo * dt;
        distL = distL + veloL * dt; 
        distR = distR + veloR * dt;

        globalPos =  globalPos.add(new Vector2D(linVelo * dt, heading, Type.POLAR));
    }


    private double calcWheelForce(double torque){
        double force = torque / Constants.WHEEL_RADIUS.getDouble();
        if(force > Constants.STATIC_FRIC){
            force = Constants.KINE_FRIC;
            slipping = true;
        } else slipping = false;
        return force;
    }

    private double calcTorqueNet(double forceL, double forceR){
        double torqueMotors = (forceR - forceL) * Constants.HALF_DIST_BETWEEN_WHEELS; //torque around center of robot

        torqueNet = Util.applyFrictions(
            torqueMotors, 
            angVelo, 
            Constants.SCRUB_STATIC_FRIC.getDouble(), 
            Constants.SCRUB_KINE_FRIC.getDouble(), 
            Constants.ANG_FRIC_THRESHOLD.getDouble());

        return torqueNet;
    }



}