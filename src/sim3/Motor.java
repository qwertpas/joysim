package sim3;

public class Motor{

    enum Model{
        CIM, miniCIM, Redline
    }

    //only changes on contruction
    private double gearing;
    private Motor.Model motorModel;
    private int numMotors;
    private double stallTorque;
    private double torqueSlope;

    //may change throughout operation
    public Boolean isStalled;
    public double voltage;
    public double angVelocity; //ungeared
    public double torque; //ungeared
    
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

    public double calcUngearedTorque(double ungearedAngVelocity){
        angVelocity = ungearedAngVelocity;
        torque = torqueSlope * Math.copySign(1, voltage) * ungearedAngVelocity + stallTorque; //base torque in direction of voltage
        if(Math.abs(torque) >= stallTorque){
            torque = numMotors * (voltage/12.0) * Math.copySign(stallTorque, torque); //maximum |torque| is stallTorque, apply scaling
            isStalled = true;
        } else {
            torque = numMotors * (voltage/12.0) * torque;
            isStalled = false;
        }

        torque = applyFrictions(torque, ungearedAngVelocity);
        return torque;
    }

    public double calcGearedTorque(double gearedAngVelocity){
        double ungearedAngVelocity = gearedAngVelocity * gearing;
        double ungearedTorque = calcUngearedTorque(ungearedAngVelocity);
        double gearedTorque = ungearedTorque * gearing;
        return gearedTorque;
    }

    private double applyFrictions(double torque, double angVelocity){
        if(Math.abs(angVelocity) < 0.01 && Math.abs(torque) < Constants.GEAR_STATIC_FRIC){
            torque = 0;
        } else {
            double direction = Math.copySign(1, angVelocity); //either +1 or -1
            torque = torque - Constants.GEAR_KINE_FRIC*direction;
        }
        return torque;
    }



    public String toString(){
        return "voltage: " + voltage + 
               ", isStalled: " + isStalled + 
               ", torque: " + torque + 
               ", angVelo: " + angVelocity;
    }
    






}