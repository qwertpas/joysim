package sim2;

public class Motor{

    enum Model{
        CIM, miniCIM, Redline
    }


    private double gearing;
    private Motor.Model motorModel;
    private int numMotors;
    private double stallTorque;
    private double torqueSlope;
    
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

    private double voltage;
    public void setVoltage(double voltage_input){
        voltage = voltage_input;
    }

    public double calcUngearedTorque(double ungearedVelocity){
        double torque;
        if(voltage > 0){ //sketch but works probably
            torque = numMotors * (voltage/12.0) * (torqueSlope * ungearedVelocity + stallTorque);
            System.out.println(torque);
            if(torque > stallTorque) torque = stallTorque;
        }else{
            torque = numMotors * (voltage/12.0) * (torqueSlope * -ungearedVelocity + stallTorque);
            System.out.println(torque);
            if(torque < -stallTorque) torque = -stallTorque;
        }
        return torque;
    }

    public double calcGearedTorque(double gearedVelocity){
        double ungearedVelocity = gearedVelocity * gearing;
        double ungearedTorque = calcUngearedTorque(ungearedVelocity);
        double gearedTorque = ungearedTorque * gearing;
        return gearedTorque;
    }
    






}