
package sim3;

import java.text.DecimalFormat;

public class Util {

    public static double applyFrictions(double force, double velocity, double STATIC_FRIC, double KINE_FRIC, double FRIC_THRESHOLD){
        if(Math.abs(velocity) < FRIC_THRESHOLD && Math.abs(force) < STATIC_FRIC){
            force = 0;
        } else {
            double direction = Math.copySign(1, velocity); //either +1 or -1
            force = force - KINE_FRIC * direction;
        }
        return force;
    }
 
    public static double rpmToRadSec(double rpm){ //Rotations per minute to Radians per second
        double rotationsPerSec =  rpm / 60.0; //each minute is 60 seconds
        return rotationsPerSec * 2 * Math.PI; // each rotation is 2PI radians
    }

    public static double radSecToRPM(double radSec){
        double rotationsPerSec = radSec / (2 * Math.PI);
        return rotationsPerSec * 60;
    }

    public static double metersToFeet(double meters){
        return meters * 3.281;
    }

    public static double metersToInches(double meters){
        return meters * 39.3701;
    }

    public static double inchesToMeters(double inches){
        return inches / 39.3701;
    }

    public static double inchesToFeet(double inches){
        return inches * 12.0;
    }

    public static double feetToInches(double feet){
        return feet / 12.0;
    }

    public static double roundHundreths(double input){
        return Double.parseDouble(new DecimalFormat("#.##").format(input));
    }

    public static double posModulo(double input, double modulo){ //modulo but it always returns a positive number, ideal for screen loopback
		while(input >= modulo) input -= modulo;
		while(input < 0) input += modulo;
		return input;
	}



    public static class MotionProfile{

        double vmax, amax, amin, target;
        Boolean isTrapezoid;
        double[] times;
        Boolean done = false;

        public MotionProfile(double vmax_input, double amax_input, double amin_input, double target_input){
            vmax = vmax_input;
            amax = amax_input;
            amin = Math.abs(amin_input);
            target = target_input;

            double time1 = vmax / amax;
            double time2 = (-vmax / (2.0*amin)) + (target / vmax) + (time1 / 2.0);
            if(time1 < time2){
                isTrapezoid = true;
                double time3 = (target / vmax) + (time1 / 2.0) + (vmax / (2.0*amin));
                times = new double[]{0.0, time1, time2, time3}; //added a zero in the first index so times[1] is time1
            } else {
                isTrapezoid = false;
                time1 = Math.sqrt((2*target) / (amax + ((amax)*(amax) / amin)));
                time2 = (amax*time1 / amin) + time1;
                times = new double[]{0.0, time1, time2};
            }
        }

        public MotionProfilePoint getPoint(double time){
            double accel = 0;
            double velo = 0;
            double dist = 0;

            if(isTrapezoid){
                if(time <= times[1]){
                    accel = amax;
                    velo = amax * time;
                    dist = 0.5 * amax * time * time;
                } else if(time <= times[2]){
                    accel = 0;
                    velo = vmax;
                    dist = (0.5 * amax * times[1] * times[1]) + 
                           (vmax * (time - times[1]));
                } else if(time <= times[3]){
                    accel = -amin;
                    velo = vmax - (amin * (time - times[2]));
                    dist = (0.5 * amax * times[1] * times[1]) + 
                           (vmax * (times[2] - times[1])) + 
                           (0.5 * (time - times[2]) * ((vmax) + (vmax - (amin * (time - times[2])))));
                } else if(time > times[3]){
                    done = true;
                    accel = 0;
                    velo = 0;
                    dist = target;
                }
            } else {
                if(time < times[1]){
                    accel = amax;
                    velo = amax * time;
                    dist = 0.5 * amax * time * time;
                } else if(time < times[2]){
                    accel = amin;
                    velo = (amax * times[1]) - (amax * (time - times[1]));
                    dist = (0.5 * amax * times[1] * times[1]) + 
                           (0.5 * (time - times[1]) * (amax*times[1] + amax*times[1] - (amin * (time - times[1]))));
                } else if(time > times[2]){
                    done = true;
                    accel = 0;
                    velo = 0;
                    dist = target;
                }
            }
            return new MotionProfilePoint(accel, velo, dist);
        }

        public class MotionProfilePoint{
            double accel, velo, dist;
            public MotionProfilePoint(double accel_input, double velo_input, double dist_input){
                accel = accel_input;
                velo = velo_input;
                dist = dist_input;
            }
        }


        public static void main(String[] args) {
            double scale = 10;

            MotionProfile testProfile = new MotionProfile(3 * scale, 1*scale, -1*scale, 15*scale);

            // MotionProfile testProfile = new MotionProfile(Util.metersToInches(3), //max velocity
            //                                               Util.metersToInches(1), //max acceleration
            //                                               Util.metersToInches(-1), //min acceleration
            //                                               Util.metersToInches(15) ); //target distance

                                                          
            System.out.println("is trapezoid: " + testProfile.isTrapezoid);
            System.out.println("time1: " + testProfile.times[1]);
            System.out.println("time2: " + testProfile.times[2]);
            if(testProfile.isTrapezoid){
                System.out.println("time3: " + testProfile.times[3]);
            }
            System.out.println("accel: " + testProfile.getPoint(0).accel);
            System.out.println("velo: " + testProfile.getPoint(0).velo);
            System.out.println("dist: " + testProfile.getPoint(0).dist);
            
            System.out.println("calc: " + (3*10)/(1*10));
        }
 
    }
    
    public static class PID {
        //constants
        double kP, kI, kD = 0;
        double tolerance;
    
        double currentValue, target, error, lastError;
        double P, I, D, power = 0;
    
        Boolean initialized = false;
        Boolean inTolerance = false;
        
        
        public PID(double kP, double kI, double kD, double tolerance){
            this.kP = kP;
            this.kI = kI;
            this.kD = kD;
            this.tolerance = tolerance;
        }
    
        public void loop(double currentValue, double target){
            this.currentValue = currentValue;
            this.target = target;
            if(!initialized){
                lastError = this.target;
                initialized = true;
            }
            error = this.target - this.currentValue;
    
    
            if (Math.abs(error) < tolerance){
                I = 0;
                inTolerance = true;
            }else{
                inTolerance = false;
            }
            
            P = kP * error;
            I = I + (kI*error);
            D = kD * (lastError - error);
    
            power = P + I + D;
        }
    
        public Boolean inTolerance(){
            return inTolerance;
        }
    
        public double getPower(){
            return power;
        }
    
        public double getError(){
            return error;
        }
    
        public void resetPID(){
            P=0;
            I=0;
            D=0;
        }
    
        public void debugPID(){
            // System.out.println("PID DEBUG ERROR: " + getError());
            // System.out.println("PID DEBUG TARGET: " + this.target);
            // System.out.println("PID DEBUG CURRENT VALUE: " + this.currentValue);
    
        }
    }


}