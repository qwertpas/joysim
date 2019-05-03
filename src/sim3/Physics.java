package sim3;

public class Physics{

    double x = 0;
    double y = 0;
    double heading = 0;
    double distL;
    double distR;

    double linVelo = 0;
    double angVelo = 0;

    double linAccel = 0.1;
    double angAccel = 0;

    long lastTime;

    double veloL = 0;
    double veloR = 0;

    double torqueL;
    double torqueR;
    double torqueNet;
    double forceNet;
    Boolean slipping = false;


    double calcWheelForce(double torque){
        double force = torque / Constants.WHEEL_RADIUS;
        if(force > Constants.STATIC_FRIC * 0.5){
            force = Constants.KINE_FRIC;
            slipping = true;
        } else slipping = false;
        return force;
    }

    public void init(){
        lastTime = System.nanoTime();
        Robot.leftMotor.setVoltage(0);
        Robot.rightMotor.setVoltage(0);
    }

    public void update(){
        double dt = (System.nanoTime() - lastTime) / 1e+9; //change in time (seconds)

        Robot.leftMotor.setVoltage(1);
        Robot.rightMotor.setVoltage(2);

        torqueL = Robot.leftMotor.calcGearedTorque(veloL / Constants.WHEEL_RADIUS);
        torqueR = Robot.rightMotor.calcGearedTorque(veloR / Constants.WHEEL_RADIUS);

        double forceL = calcWheelForce(torqueL);
        double forceR = calcWheelForce(torqueR);

        torqueNet = (forceR - forceL) * Constants.HALF_DIST_BETWEEN_WHEELS; //torque around center of robot
        forceNet = forceL + forceR;

        angAccel = torqueNet / Constants.ROBOT_ROT_INERTIA;
        linAccel = forceNet / Constants.ROBOT_MASS;

        angVelo = angVelo + angAccel * dt;
        linVelo = linVelo + linAccel * dt;
        veloL = linVelo - Constants.HALF_DIST_BETWEEN_WHEELS * angVelo;
        veloR = linVelo + Constants.HALF_DIST_BETWEEN_WHEELS * angVelo;

        heading = heading + angVelo * dt;
        distL = distL + veloL * dt;
        distR = distR + veloR * dt;

        x = x + linVelo * dt * Math.cos(heading);
        y = y + linVelo * dt * Math.sin(heading);
        lastTime = System.nanoTime();
    }

    public String toString(){
        return x +" "+ y +" "+ heading +" "+ linVelo +" "+ angVelo +" "+ linAccel +" "+ angAccel;
    }

    


}