package sim3;

public class Constants{

    /** ////////////////////////////////////////////
     * DISPLAY PREFERENCES
     * //////////////////////////////////////////// */ 
    static Boolean printPowers = false;
    

    /** ////////////////////////////////////////////
     * REAL PHYSICAL CONSTANTS (meters, kilograms, seconds) that come from GraphicInput
     * //////////////////////////////////////////// */ 
    static Constant GEAR_RATIO = new Constant("GEAR_RATIO", 8);
    static Constant MOTORS_PER_SIDE = new Constant("MOTORS_PER_SIDE", 2);

    static Constant ROBOT_MASS = new Constant("ROBOT_MASS", 45.3592);
    static Constant ROBOT_WIDTH = new Constant("ROBOT_WIDTH", 0.6096);
    static Constant DIST_BETWEEN_WHEELS = new Constant("DIST_BETWEEN_WHEELS", 0.508);
    static Constant WHEEL_RADIUS = new Constant("WHEEL_RADIUS", 0.0762);
    static Constant STATIC_FRIC_COEFF = new Constant("STATIC_FRIC_COEFF", 1.1); //between wheels and ground
    static Constant KINE_FRIC_COEFF = new Constant("KINE_FRIC_COEFF", 0.7); //should be < static

    //Overall makes motors slower
    static Constant GEAR_STATIC_FRIC = new Constant("GEAR_STATIC_FRIC", 2); //actual torque against gearbox when not moving, not the coefficient 
    static Constant GEAR_KINE_FRIC = new Constant("GEAR_KINE_FRIC", 0.5); //actual torque against gearbox when not moving, not the coefficient 
    static Constant GEAR_FRIC_THRESHOLD = new Constant("GEAR_FRIC_THRESHOLD", 0.01); //lowest motor speed in rad/sec considered as 'moving' to kine fric

    // Wheel scrub torque slows turning, coeff is a combo of fric coeff, drop center, robot length.
    static Constant WHEEL_SCRUB_STATIC_COEFF = new Constant("WHEEL_SCRUB_STATIC_COEFF", 0.02); 
    static Constant WHEEL_SCRUB_KINE_COEFF = new Constant("WHEEL_SCRUB_KINE_COEFF", 0.01); 
    static Constant WHEEL_SCRUB_FRIC_THRESHOLD = new Constant("WHEEL_SCRUB_FRIC_THRESHOLD", 0.01); //lowest robot rad/sec considered as 'moving' to kine fric

    static Constant GRAV_ACCEL = new Constant("GRAV_ACCEL", 9.81);

    // static Constant CIM_STALL_TORQUE = new Constant("CIM_STALL_TORQUE", 2.413);
    static double CIM_STALL_TORQUE = 2.413;
    static double CIM_TORQUE_SLOPE = -0.0004527;
    static double MINICIM_STALL_TORQUE = 1.409;
    static double MINICIM_TORQUE_SLOPE = -0.0002413;
    static double REDLINE_STALL_TORQUE = 0.7080;
    static double REDLINE_TORQUE_SLOPE = -3.779e-05;

    static Constant CONTROLLER_INDEX = new Constant("Controller_INDEX", 0); //which joystick?

    static Constant DISPLAY_SCALE = new Constant("DISPLAY_SCALE", 100); //in pixels per meter

    static Constant[] constants = {GEAR_RATIO, 
                                   MOTORS_PER_SIDE, 
                                   ROBOT_MASS, 
                                   ROBOT_WIDTH, 
                                   DIST_BETWEEN_WHEELS,
                                   WHEEL_RADIUS,
                                   STATIC_FRIC_COEFF,
                                   KINE_FRIC_COEFF,
                                   GEAR_STATIC_FRIC,
                                   GEAR_KINE_FRIC,
                                   GEAR_FRIC_THRESHOLD,
                                   WHEEL_SCRUB_STATIC_COEFF,
                                   WHEEL_SCRUB_KINE_COEFF,
                                   WHEEL_SCRUB_FRIC_THRESHOLD,
                                   GRAV_ACCEL,
                                   CONTROLLER_INDEX,
                                   DISPLAY_SCALE,
                                  };

    /** ////////////////////////////////
     * CALCULATED FROM REAL CONSTANTS
     * //////////////////////////////// */     
    static double HALF_DIST_BETWEEN_WHEELS = DIST_BETWEEN_WHEELS.getDouble() / 2.0;
    static double STATIC_FRIC = ROBOT_MASS.getDouble() * GRAV_ACCEL.getDouble() * STATIC_FRIC_COEFF.getDouble();
    static double KINE_FRIC = ROBOT_MASS.getDouble() * GRAV_ACCEL.getDouble() * KINE_FRIC_COEFF.getDouble();

    static double WHEEL_SCRUB_STATIC = ROBOT_MASS.getDouble() * GRAV_ACCEL.getDouble() * WHEEL_SCRUB_STATIC_COEFF.getDouble(); //is a torque
    static double WHEEL_SCRUB_KINE = ROBOT_MASS.getDouble() * GRAV_ACCEL.getDouble() * WHEEL_SCRUB_KINE_COEFF.getDouble(); //is a torque

    static double ROBOT_ROT_INERTIA = (1.0/6.0) * ROBOT_MASS.getDouble() * ROBOT_WIDTH.getDouble() * ROBOT_WIDTH.getDouble();
    // static double ROBOT_ROT_INERTIA = 2;
    //https://en.wikipedia.org/wiki/List_of_moments_of_inertia



    public static class Constant{
        private String name;
        private Object value;
        Constant(String name_input, Object value_input){
            name = name_input;
            value = value_input;
        }
        
        public String getName() {
            return name;
        }
        
        public double getDouble() {
            return Double.valueOf(getString());
        }

        public int getInt() {
            return Integer.valueOf(getString());
        }

        public String getString() {
            return String.valueOf(value);
        }

        public Object getObject(){
            return value;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public void setValue(double value) {
            this.value = value;
        }
    }

}