package sim3.userclasses;

import sim3.Constants;
import sim3.Util.Vector2D;
import sim3.Util.Pose2D;
import sim3.Util.Vector2D.Type;



public class Odometry{

    public Pose2D estPose = new Pose2D();
    
    double r = Constants.SIDE_ODO_Y.getDouble();

    double lastLDist, lastRDist = 0;

    public void update(double newLDist, double newRDist) {
        double xl = newLDist - lastLDist;
        double xr = newRDist - lastRDist;

        lastLDist = newLDist;
        lastRDist = newRDist;

        if(Math.abs(xl - xr) < 0.00001){
            estPose = estPose.add(new Pose2D(new Vector2D(0.5 * (xr + xl), 0, Type.POLAR), 0));
            return;
        }

        double arcAngle = (xr - xl) / (2 * r);
        double distanceIncrement = (2 * r * (xr + xl) * Math.sin(arcAngle/2.0)) / (xr - xl);
        double angleIncrement = arcAngle / 2.0;

        estPose = estPose.add(new Pose2D(new Vector2D(distanceIncrement, angleIncrement, Type.POLAR), arcAngle));

    }

}