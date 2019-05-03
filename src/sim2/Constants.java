package sim2;

public class Constants{

    // REAL CONSTANTS (meters, kilograms, seconds)
    static final double DIST_BETWEEN_WHEELS = 1;
    static final double WHEEL_RADIUS = 0.0762;
    static final double STATIC_FRIC_COEFF = 1.1;
    static final double KINE_FRIC_COEFF = 0.7;

    static final double ROBOT_MASS = 45.3592;

    static final double CIM_STALL_TORQUE = 2.413;
    static final double CIM_TORQUE_SLOPE = -0.0004527;
    static final double MINICIM_STALL_TORQUE = 1.409;
    static final double MINICIM_TORQUE_SLOPE = -0.0002413;
    static final double REDLINE_STALL_TORQUE = 0.7080;
    static final double REDLINE_TORQUE_SLOPE = -3.779e-05;




    // CALCULATED FROM REAL CONSTANTS
    static final double HALF_DIST_BETWEEN_WHEELS = DIST_BETWEEN_WHEELS / 2.0;
    static final double STATIC_FRIC = ROBOT_MASS * STATIC_FRIC_COEFF;
    static final double KINE_FRIC = ROBOT_MASS * KINE_FRIC_COEFF;


}