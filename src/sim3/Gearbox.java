package sim3;

public class Gearbox{

    public Motor[] motors = new Motor[2];

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
        return totalTorque * Constants.GEAR_RATIO.getDouble();
    }

    public void update(double angVelo){
        double motorAngVelo = angVelo * Constants.GEAR_RATIO.getDouble();
        for(int i = 0; i < motors.length; i++){
            motors[i].update(motorAngVelo);
        }
    }

    public void setPower(double power){
        double voltage = power * 12;
        for(int i = 0; i < motors.length; i++){
            motors[i].setVoltage(voltage);
        }
    }

}