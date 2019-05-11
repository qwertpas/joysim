package sim3;

public class Constants{

    /** ////////////////////////////////////////////
     * DISPLAY PREFERENCES
     * //////////////////////////////////////////// */ 
    static final Boolean printPowers = false;
    

    /** ////////////////////////////////////////////
     * REAL PHYSICAL CONSTANTS (meters, kilograms, seconds) 
     * //////////////////////////////////////////// */ 
    static final double GEAR_RATIO = 8;
    static final int MOTORS_PER_SIDE = 2;

    static final double ROBOT_MASS = 45.3592;
    static final double ROBOT_WIDTH = 0.6096; //scaling field size and robot display
    static final double DIST_BETWEEN_WHEELS = 0.508; //affects robot turning characteristics
    static final double WHEEL_RADIUS = 0.0762; 
    static final double STATIC_FRIC_COEFF = 1.1; //between wheels and ground, bigger=less slipping
    static final double KINE_FRIC_COEFF = 0.7; //fric coeff while slipping. should be <static

    //Overall makes motors slower
    static final double GEAR_STATIC_FRIC = 2; //actual torque against gearbox when not moving, not the coefficient 
    static final double GEAR_KINE_FRIC = 0.5; //actual torque against gearbox when moving, not the coefficient 
    static final double GEAR_FRIC_THRESHOLD = 0.01; //lowest motor speed in rad/sec considered as 'moving' to kine fric

    // Wheel scrub torque slows turning, coeff is a combo of fric coeff, drop center, robot length. 
    static final double WHEEL_SCRUB_STATIC_COEFF = 0.02;
    static final double WHEEL_SCRUB_KINE_COEFF = 0.01; 
    static final double WHEEL_SCRUB_FRIC_THRESHOLD = 0.01; //lowest robot rad/sec considered as 'moving' to kine fric

    static final double GRAV_ACCEL = 9.81;

    static final double CIM_STALL_TORQUE = 2.413;
    static final double CIM_TORQUE_SLOPE = -0.0004527;
    static final double MINICIM_STALL_TORQUE = 1.409;
    static final double MINICIM_TORQUE_SLOPE = -0.0002413;
    static final double REDLINE_STALL_TORQUE = 0.7080;
    static final double REDLINE_TORQUE_SLOPE = -3.779e-05;

    static final int CONTROLLER_INDEX = 0; //which joystick?

    static final int DISPLAY_SCALE = 100; //in pixels per meter 


    /** ////////////////////////////////
     * CALCULATED FROM REAL CONSTANTS
     * //////////////////////////////// */     
    static final double HALF_DIST_BETWEEN_WHEELS = DIST_BETWEEN_WHEELS / 2.0;
    static final double STATIC_FRIC = ROBOT_MASS * GRAV_ACCEL * STATIC_FRIC_COEFF;
    static final double KINE_FRIC = ROBOT_MASS * GRAV_ACCEL * KINE_FRIC_COEFF;

    static final double WHEEL_SCRUB_STATIC = ROBOT_MASS * GRAV_ACCEL * WHEEL_SCRUB_STATIC_COEFF; //is a torque
    static final double WHEEL_SCRUB_KINE = ROBOT_MASS * GRAV_ACCEL * WHEEL_SCRUB_KINE_COEFF; //is a torque

    static final double ROBOT_ROT_INERTIA = (1.0/6.0) * ROBOT_MASS * ROBOT_WIDTH * ROBOT_WIDTH;
    // static final double ROBOT_ROT_INERTIA = 2;
    //https://en.wikipedia.org/wiki/List_of_moments_of_inertia




}