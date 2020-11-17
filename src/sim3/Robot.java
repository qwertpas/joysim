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
    boolean slippingL = false; 
    boolean slippingR = false;

    static double dt;
    static double lastTime;

    public Gearbox leftGearbox = new Gearbox(2);
    public Gearbox rightGearbox = new Gearbox(2);

    public double leftOdoEncoderPos, rightOdoEncoderPos, centerOdoEncoderPos = 0;

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

        double forceL = (torqueL / Constants.WHEEL_RADIUS.getDouble()) + Constants.TURN_ERROR.getDouble();
        double forceR = (torqueR / Constants.WHEEL_RADIUS.getDouble()) - Constants.TURN_ERROR.getDouble();

        if(forceL > Constants.STATIC_FRIC){
            forceL = Constants.KINE_FRIC;
            slippingL = true;
        } else slippingL = false;

        if(forceR > Constants.STATIC_FRIC){
            forceR = Constants.KINE_FRIC;
            slippingR = true;
        } else slippingR = false;

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

        simulateOdometry();
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

    private void simulateOdometry(){
        double leftOdoVelo = linVelo - Constants.SIDE_ODO_Y.getDouble() * angVelo;
        double rightOdoVelo = linVelo + Constants.SIDE_ODO_Y.getDouble() * angVelo;
        double centerOdoVelo = Constants.CENTER_ODO_X.getDouble() * angVelo;

        double odoEncoderTicksPerRad = 8192.0 / (2 * Math.PI); 
        double odoWheelRadius = 0.024;

        leftOdoEncoderPos += (leftOdoVelo / odoWheelRadius) * odoEncoderTicksPerRad * dt;
        rightOdoEncoderPos += (rightOdoVelo / odoWheelRadius) * odoEncoderTicksPerRad * dt;
        centerOdoEncoderPos += (centerOdoVelo / odoWheelRadius) * odoEncoderTicksPerRad * dt;
    }



}