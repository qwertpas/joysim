package sim3;

public class Motor{

    enum Model{
        CIM, miniCIM, Redline
    }

    //only changes on contruction
    private double gearing; //ratio of output torque to input torque (higher==geared for torque)
    private Motor.Model motorModel;
    private int numMotors;
    private double stallTorque;
    private double torqueSlope;

    //may change throughout operation
    public Boolean isStalled;
    public double voltage;
    public double RPM; //ungeared angular velocity in rotations per minute
    public double torque; //ungeared in newton*meters
    public double distance; //integrating angular velocity then using wheel circumference, convert to inches
    
    public Motor(double gearing_input, Motor.Model motorModel_input, int numMotors_input){
        gearing = gearing_input;
        motorModel = motorModel_input;
        numMotors = numMotors_input;
        if(motorModel == Model.CIM){
            stallTorque = Constants.CIM_STALL_TORQUE;
            torqueSlope = Constants.CIM_TORQUE_SLOPE;
        }
        if(motorModel == Model.miniCIM){
            stallTorque = Constants.MINICIM_STALL_TORQUE;
            torqueSlope = Constants.MINICIM_TORQUE_SLOPE;
        }
        if(motorModel == Model.Redline){
            stallTorque = Constants.REDLINE_STALL_TORQUE;
            torqueSlope = Constants.REDLINE_TORQUE_SLOPE;
        }
    }

    public void setVoltage(double voltage_input){
        voltage = voltage_input;
    }

    public void integrateVelocity(){
        double radPerSec = Util.rpmToRadSec(RPM);
        double changeInAngle = radPerSec * Physics.dt; //change in the angle(radians) of the wheel in 1 update cycle
        distance += Util.metersToInches(changeInAngle * Constants.WHEEL_RADIUS.getDouble()); // arclength == angle * radius
    }

    public double calcUngearedTorque(double ungearedAngVelocityRad){ //input is radians per second
        RPM = Util.radSecToRPM(ungearedAngVelocityRad);
        integrateVelocity();

        torque = torqueSlope * Math.copySign(1, voltage) * RPM + stallTorque; //base torque in direction of voltage based on motor chart
        if(Math.abs(torque) >= stallTorque){
            torque = numMotors * (voltage/12.0) * Math.copySign(stallTorque, torque); //maximum |torque| is stallTorque, apply scaling
            isStalled = true;
        } else {
            torque = numMotors * (voltage/12.0) * torque;
            isStalled = false;
        }

        torque = Util.applyFrictions(torque, ungearedAngVelocityRad, 
                                     Constants.GEAR_STATIC_FRIC.getDouble(), 
                                     Constants.GEAR_KINE_FRIC.getDouble(), 
                                     Constants.GEAR_FRIC_THRESHOLD.getDouble());
        return torque;
    }

    public double calcGearedTorque(double gearedAngVelocity){
        double ungearedAngVelocity = gearedAngVelocity * gearing;
        double ungearedTorque = calcUngearedTorque(ungearedAngVelocity);
        double gearedTorque = ungearedTorque * gearing;
        return gearedTorque;
    }

    /**
     * @return distance in inches rounded to hundreths place
     */
    public double getDistance() {
        return Util.roundHundreths(distance);
    }

    /**
     * @return RPM rounded to hundreths place
     */
    public double getRPM() {
        return Util.roundHundreths(RPM);
    }



    public String toString(){
        return "voltage: " + voltage + 
               ", isStalled: " + isStalled + 
               ", torque: " + torque + 
               ", RPM: " + RPM;
    }
    






}