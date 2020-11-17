package sim3;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class Constants{

    enum Type{
        BOOLEAN, INT, DOUBLE, STRING;
    }

    JPanel panel = new JPanel();

    /** ////////////////////////////////////////////
     * DEBUG PREFERENCES
     * //////////////////////////////////////////// */ 
    public static Boolean printJoystick = false;    

    /** ////////////////////////////////////////////
     * REAL PHYSICAL CONSTANTS (meters, kilograms, seconds) that come from GraphicInput
     * //////////////////////////////////////////// */ 
    public static Constant GEAR_RATIO = new Constant("GEAR_RATIO", 1.181818, Type.DOUBLE);
    public static Constant STALL_TORQUE = new Constant("STALL_TORQUE", 0.537, Type.DOUBLE); //76 oz-in for yellowjacket 3.7, converted to newton*meters
    public static Constant FREE_SPEED = new Constant("FREE_SPEED", 170, Type.DOUBLE); //1620 rpm for yellowjacket 3.7, converted to rad/sec
    public static Constant TICKS_PER_REV = new Constant("TICKS_PER_REV", 103.6, Type.DOUBLE); //1 rev of output shaft of yellowjacket 3.7 -> 103.6 encoder ticks

    public static Constant ROBOT_MASS = new Constant("ROBOT_MASS", 5, Type.DOUBLE);
    public static Constant ROBOT_WIDTH = new Constant("ROBOT_WIDTH", 0.4572, Type.DOUBLE);
    public static Constant DIST_BETWEEN_WHEELS = new Constant("DIST_BETWEEN_WHEELS", 0.4064, Type.DOUBLE);
    public static Constant WHEEL_RADIUS = new Constant("WHEEL_RADIUS", 0.0381, Type.DOUBLE); //5 inch diameter
    public static Constant STATIC_FRIC_COEFF = new Constant("STATIC_FRIC_COEFF", 1.1, Type.DOUBLE); //between wheels and ground
    public static Constant KINE_FRIC_COEFF = new Constant("KINE_FRIC_COEFF", 0.7, Type.DOUBLE); //should be < static

    //Overall makes motors slower
    public static Constant GEAR_STATIC_FRIC = new Constant("GEAR_STATIC_FRIC", 0.5, Type.DOUBLE); //actual torque against gearbox when not moving, not the coefficient 
    public static Constant GEAR_KINE_FRIC = new Constant("GEAR_KINE_FRIC", 0.4, Type.DOUBLE); //actual torque against gearbox when not moving, not the coefficient 

    // Wheel scrub torque slows turning, is a combo of fric coeff, drop center, robot length.
    public static Constant SCRUB_STATIC_FRIC = new Constant("SCRUB_STATIC_FRIC", 0.6, Type.DOUBLE);
    public static Constant SCRUB_KINE_FRIC = new Constant("SCRUB_KINE_FRIC", 0.5, Type.DOUBLE); 

    public static Constant ANG_FRIC_THRESHOLD = new Constant("ANG_FRIC_THRESHOLD", 0.01, Type.DOUBLE); //lowest rad/sec considered as 'moving' to apply kine fric
    
    public static Constant GRAV_ACCEL = new Constant("GRAV_ACCEL", 9.81, Type.DOUBLE);

    public static Constant CONTROLLER_INDEX = new Constant("Controller_INDEX", 0, Type.INT); //which joystick?
    public static Constant DISPLAY_SCALE = new Constant("DISPLAY_SCALE", 200, Type.DOUBLE); //in pixels per meter

    public static Constant TURN_ERROR = new Constant("TURN_ERROR", 0.0, Type.DOUBLE); //difference in powers between the two sides (build problem)

    public static Constant SIDE_ODO_Y = new Constant("SIDE_ODO_Y", 0.201295, Type.DOUBLE);
    public static Constant CENTER_ODO_X = new Constant("CENTER_ODO_X", -0.18878, Type.DOUBLE);


    //constants that are editable by GraphicInput
    public static Constant[] constants = {GEAR_RATIO, 
                                   ROBOT_MASS, 
                                   STATIC_FRIC_COEFF,
                                   KINE_FRIC_COEFF,
                                   GEAR_STATIC_FRIC,
                                   GEAR_KINE_FRIC,
                                   SCRUB_STATIC_FRIC,
                                   SCRUB_KINE_FRIC,
                                   ANG_FRIC_THRESHOLD,
                                   CONTROLLER_INDEX,
                                   DISPLAY_SCALE,
                                   TURN_ERROR
                                  };

    /** ////////////////////////////////
     * CALCULATED FROM REAL CONSTANTS
     * //////////////////////////////// */     
    
    
    
    public static double HALF_DIST_BETWEEN_WHEELS = DIST_BETWEEN_WHEELS.getDouble() / 2.0;

    public static double STATIC_FRIC = ROBOT_MASS.getDouble() * GRAV_ACCEL.getDouble() * STATIC_FRIC_COEFF.getDouble();
    public static double KINE_FRIC = ROBOT_MASS.getDouble() * GRAV_ACCEL.getDouble() * KINE_FRIC_COEFF.getDouble();

    public static double ROBOT_ROT_INERTIA = (1.0/6.0) * ROBOT_MASS.getDouble() * ROBOT_WIDTH.getDouble() * ROBOT_WIDTH.getDouble();
    // public static double ROBOT_ROT_INERTIA = 2;
    //https://en.wikipedia.org/wiki/List_of_moments_of_inertia

    public static void calcConstants(){
        HALF_DIST_BETWEEN_WHEELS = DIST_BETWEEN_WHEELS.getDouble() / 2.0;
        STATIC_FRIC = ROBOT_MASS.getDouble() * GRAV_ACCEL.getDouble() * STATIC_FRIC_COEFF.getDouble();
        KINE_FRIC = ROBOT_MASS.getDouble() * GRAV_ACCEL.getDouble() * KINE_FRIC_COEFF.getDouble();

        ROBOT_ROT_INERTIA = (1.0/6.0) * ROBOT_MASS.getDouble() * ROBOT_WIDTH.getDouble() * ROBOT_WIDTH.getDouble();
    }

    public static Boolean checkTypes(){
        Boolean good = true;
        for(Constant constant : constants){
            try{
                if(constant.type.equals(Type.DOUBLE)) constant.getDouble();
                if(constant.type.equals(Type.INT)) constant.getInt();
                if(constant.type.equals(Type.STRING)) constant.getString();
            }catch(IllegalArgumentException e){
                JOptionPane.showMessageDialog(GraphicInput.panel, constant.name + " must be of type " + constant.type.name());
                good = false;
            }
        }
        return good;
    }

    public static void setAllToDefault(){
        for(Constant constant : constants){
            constant.setValue(constant.defaultValue);
        }
    }


    public static class Constant{
        private String name;
        private Object value;
        private String defaultValue;
        
        Type type;
        
        JLabel label;
        JTextField field;


        Constant(String name_input, Object value_input, Type type_input){
            name = name_input;
            value = value_input;
            defaultValue = String.valueOf(value_input);
            type = type_input;
            
            label = new JLabel(name);
            field = new JTextField(String.valueOf(value));
        }
        
        public String getName(){
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

        public String getDefaultString() {
            return String.valueOf(defaultValue);
        }

        public Object getObject(){
            return value;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public void setValue(Object value_input) {
            this.value = value_input;
        }

    }

}