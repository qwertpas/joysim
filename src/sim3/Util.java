
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

    public static double roundHundreths(double input){
        return Double.parseDouble(new DecimalFormat("#.##").format(input));
    }




}