package sim3;

public class Gearbox{

    public Motor[] motors = new Motor[2];

    private double angVelo;

    public Gearbox(Motor[] motors){
        this.motors = motors;
    }

    public Gearbox(int numMotors){
        this.motors = new Motor[numMotors];
        for(int i = 0; i < numMotors; i++){
            motors[i] = new Motor();
        }
    }

    public double getOutputTorque(){
        double totalTorque = 0;
        for(Motor motor : motors){
            totalTorque += motor.getTorque();
        }
        return Util.applyFrictions(
            totalTorque * Constants.GEAR_RATIO.getDouble(),
            angVelo, 
            Constants.GEAR_STATIC_FRIC.getDouble(), 
            Constants.GEAR_KINE_FRIC.getDouble(), 
            Constants.ANG_FRIC_THRESHOLD.getDouble());
    }

    public void update(double angVelo){
        this.angVelo = angVelo;
        double motorAngVelo = angVelo * Constants.GEAR_RATIO.getDouble();
        for(int i = 0; i < motors.length; i++){
            motors[i].update(motorAngVelo);
        }
    }

    public void setPower(double power){

        power = Util.limit(power, 1);

        double voltage = power * 12;

        for(int i = 0; i < motors.length; i++){
            motors[i].setVoltage(voltage);
        }
    }

    public double getAvgEncoderPosition(){
        double encoderPositionSum = 0;
        for(Motor motor : motors){
            encoderPositionSum += motor.getEncoderPosition();
        }
        return encoderPositionSum;
    }

    public double getAvgEncoderVelocity(){
        double encoderVelocitySum = 0;
        for(Motor motor : motors){
            encoderVelocitySum += motor.getEncoderVelocity();
        }
        return encoderVelocitySum;
    }

}