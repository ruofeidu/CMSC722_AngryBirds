package ab.planner;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ab.utils.Lib;

/**
 * Trajectory Planner
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and Dr. Vikas Shivashankar 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class TrajectoryPlanner {
    private static double X_OFFSET = 0.5;
    private static double Y_OFFSET = 0.65;
    private static double BOUND = 0.1;
    private static double STRETCH = 0.4;
    private static int X_MAX = 640;
    
    //                                         10,    15,    20,    25,    30,    35,    40,    45,    50,    55,    60,    65,    70
    private static double _launchAngle [] =   {0.13,  0.215, 0.296, 0.381, 0.476, 0.567, 0.657, 0.741, 0.832, 0.924, 1.014, 1.106, 1.197};
    private static double _changeAngle [] =   {0.052, 0.057, 0.063, 0.066, 0.056, 0.054, 0.050, 0.053, 0.042, 0.038, 0.034, 0.029, 0.025};
    private static double _launchVelocity[] = {2.9,   2.88,  2.866, 2.838, 2.810, 2.800, 2.790, 2.773, 2.763, 2.745, 2.74, 2.735, 2.73};
    
    private static double _launchAngleAstar [] = {0.0698132, 0.0872665, 0.10472, 0.122173, 0.139626, 0.15708, 0.174533, 0.191986, 0.20944, 0.226893, 0.244346, 0.261799, 0.279253, 0.296706, 0.314159, 0.331613, 0.349066, 0.366519, 0.383972, 0.401426, 0.418879, 0.436332, 0.453786, 0.471239, 0.488692, 0.506145, 0.523599, 0.541052, 0.558505, 0.575959, 0.593412, 0.610865, 0.628319, 0.645772, 0.663225, 0.680678, 0.698132, 0.715585, 0.733038, 0.750492, 0.767945, 0.785398, 0.802851, 0.820305, 0.837758, 0.855211, 0.872665, 0.890118, 0.907571, 0.925025, 0.942478, 0.959931, 0.977384, 0.994838, 1.012290, 1.029740, 1.047200, 1.064650, 1.082100, 1.099560, 1.117010, 1.134460, 1.151920, 1.169370, 1.186820, 1.204280, 1.221730, 1.239180, 1.256640, 1.274090, 1.291540, 1.309000, 1.326450, 1.343900, 1.361360, 1.378810};
    private static double _launchVelocityAstar [] = {2.78571, 2.78323, 2.77290, 2.72967, 2.74747, 2.74946, 2.74762, 2.74765, 2.75169, 2.75992, 2.77147, 2.78507, 2.79933, 2.81297, 2.82493, 2.83443, 2.84097, 2.83430, 2.82441, 2.81147, 2.80584, 2.79793, 2.78825, 2.77734, 2.76572, 2.78389, 2.77228, 2.76128, 2.75119, 2.74222, 2.73450, 2.72811, 2.72302, 2.71916, 2.71641, 2.71461, 2.71357, 2.71309, 2.71298, 2.71304, 2.71312, 2.71307, 2.71278, 2.71219, 2.71125, 2.70995, 2.70834, 2.70646, 2.70438, 2.70218, 2.69996, 2.69778, 2.69573, 2.69385, 2.69217, 2.69070, 2.68941, 2.68826, 2.68721, 2.68618, 2.68514, 2.68404, 2.68287, 2.68163, 2.68035, 2.67901, 2.67752, 2.67554, 2.67231, 2.66625, 2.65447, 2.61058, 2.55100, 2.47435, 2.40219, 2.31633};
	
    /**
     * small modification to the scale and angle
     */
    private double _scaleFactor = 1.005;
    
    /**
     * conversion between the trajectory time and actual time in milliseconds
     */
    private double _timeUnit = 815;
    
    /**
     * boolean flag on set trajectory
     */
    private boolean _trajSet = false;
    
    /**
     * parameters of the set trajectory
     */
    private Point _release;
    private double _theta, _velocity, _ux, _uy, _a, _b;
    
    /**
     * the trajectory points
     */
    ArrayList<Point> _trajectory;
    
    /**
     * reference point and current scale
     */
    private Point _ref;
    private double _scale;

    /**
     * create a trajectory planner object
     */
    public TrajectoryPlanner() {
    
    }

    /**
     * Fit a quadratic to the given points and adjust parameters for the current scene
     * namely the scale factor, which is a number used to find the correct exit velocity
     *
     * @param   pts - list of points (on screen) the trajectory passed through
     * @param   slingshot - bounding box of the slingshot
     * @param   releasePoint - where the mouse click was released from
     */
    public void adjustTrajectory(final List<Point> pts, Rectangle slingshot, Point releasePoint)
    {
        double Sx2 = 0.0;
        double Sx3 = 0.0;
        double Sx4 = 0.0;
        double Syx = 0.0;
        double Syx2 = 0.0;

        // find scene scale and reference point
        double sceneScale = getSceneScale(slingshot);
        Point refPoint = getReferencePoint(slingshot);
        
        for (Point p : pts) {                
            // normalise the points
            double x = (p.x - refPoint.x) / sceneScale;
            double y = (p.y - refPoint.y) / sceneScale;
            
            final double x2 = x * x;
            Sx2 += x2;
            Sx3 += x * x2;
            Sx4 += x2 * x2;
            Syx += y * x;
            Syx2 += y * x2;
        }

        final double a = (Syx2 * Sx2 - Syx * Sx3) / (Sx4 * Sx2 - Sx3 * Sx3);
        final double b = (Syx - a * Sx3) / Sx2;
        
        /**
         * launch angle
         */
        double theta = -Math.atan2(refPoint.y - releasePoint.y, refPoint.x - releasePoint.x);
       
        /**
         *  find initial velocity
         */
        double ux = Math.sqrt(0.5/a);
        double uy = ux * b;
        
        //System.out.println("velocity: " + Math.sqrt(ux*ux+uy*uy));
        
        /**
         * adjust the scale and angle change
         */
        adjustScale(Math.sqrt(ux*ux + uy*uy), theta);
        Lib.debug(Lib.flagVision, "Scale factor = " + _scaleFactor);
        _trajSet = false;
    }
    
    /** 
     * Calculate the y-coordinate of a point on the set trajectory
     *
     * @param   sling - bounding rectangle of the slingshot
     *          releasePoint - point the mouse click is released from
     *          x - x-coordinate (on screen) of the requested point
     * @return  y-coordinate (on screen) of the requested point
     */
    public int getYCoordinate(Rectangle sling, Point releasePoint, int x)
    {
        setTrajectory(sling, releasePoint);
        
        // find the normalised coordinates
        double xn = (x - _ref.x) / _scale;
        
        return _ref.y - (int)((_a * xn * xn + _b * xn) * _scale);
    }
    
    /**
     * Estimate launch points given a desired target point using maximum velocity
     * If there are two launch point for the target, they are both returned in
     * the list {lower point, higher point)
     * Note - angles greater than 75 are not considered
     *
     * @param   slingshot - bounding rectangle of the slingshot
     *          targetPoint - coordinates of the target to hit
     * @return  A list containing 2 possible release points
     */
    public ArrayList<Point> estimateLaunchPoint(Rectangle slingshot, Point targetPoint) {
        
        // calculate relative position of the target (normalised)
        double scale = getSceneScale(slingshot);
        //System.out.println("scale " + scale);
        Point ref = getReferencePoint(slingshot);
            
        double x = (targetPoint.x - ref.x) / scale;
        double y = -(targetPoint.y - ref.y) / scale;
        
        double bestError = 1000;
        double theta1 = 0;
        double theta2 = 0;
        
        // first estimate launch angle using the projectile equation (constant velocity)
        double v = _scaleFactor * _launchVelocity[6];
        double v2 = v * v;
        double v4 = v2 * v2;
        double tangent1 = (v2 - Math.sqrt(v4 - (x * x + 2 * y * v2))) / x;
        double tangent2 = (v2 + Math.sqrt(v4 - (x * x + 2 * y * v2))) / x;
        double t1 = actualToLaunch(Math.atan(tangent1));
        double t2 = actualToLaunch(Math.atan(tangent2));

        ArrayList<Point> pts = new ArrayList<Point>();

        // search angles in range [t1 - BOUND, t1 + BOUND]
        for (double theta = t1 - BOUND; theta <= t1 + BOUND; theta += 0.001)
        {
            double velocity = getVelocity(theta);
            
            // initial velocities
            double u_x = velocity * Math.cos(theta);
            double u_y = velocity * Math.sin(theta);
            
            // the normalised coefficients
            double a = -0.5 / (u_x * u_x);
            double b = u_y / u_x;
            
            // the error in y-coordinate
            double error = Math.abs(a*x*x + b*x - y);
            if (error < bestError)
            {
                theta1 = theta;
                bestError = error;
            }
        }
        if (bestError < 1000)
        {
            theta1 = actualToLaunch(theta1);
            // add launch points to the list
            pts.add(findReleasePoint(slingshot, theta1));
        }
        bestError = 1000;
        
        // search angles in range [t2 - BOUND, t2 + BOUND]
        for (double theta = t2 - BOUND; theta <= t2 + BOUND; theta += 0.001)
        {
            double velocity = getVelocity(theta);
            
            // initial velocities
            double u_x = velocity * Math.cos(theta);
            double u_y = velocity * Math.sin(theta);
            
            // the normalised coefficients
            double a = -0.5 / (u_x * u_x);
            double b = u_y / u_x;
            
            // the error in y-coordinate
            double error = Math.abs(a*x*x + b*x - y);
            if (error < bestError)
            {
                theta2 = theta;
                bestError = error;
            }
        }
        
        theta2 = actualToLaunch(theta2);
        
        //System.out.println("Two angles: " + Math.toDegrees(theta1) + ", " + Math.toDegrees(theta2));
            
        
        // add the higher point if it is below 75 degrees and not same as first
        if (theta2 < Math.toRadians(75) && theta2 != theta1 && bestError < 1000)
            pts.add(findReleasePoint(slingshot, theta2));
        
        return pts;
    }
   
       
    /** 
     * the estimated tap time given the tap point
     *
     * @param   sling - bounding box of the slingshot
     *          release - point the mouse clicked was released from
     *          tapPoint - point the tap should be made
     * @return  tap time (relative to the release time) in milli-seconds
     */
    protected int getTimeByDistance(Rectangle sling, Point release, Point tapPoint)
    {
        // update trajectory parameters
        setTrajectory(sling, release);
        
        double pullback = _scale * STRETCH * Math.cos(_theta);
        double distance = (tapPoint.x - _ref.x + pullback) / _scale;
        
        System.out.println("distance " + distance);
        System.out.println("velocity " + _ux);
        
        return (int)(distance / _ux * _timeUnit);
    }
 
   
    /** 
     * Choose a trajectory by specifying the sling location and release point
     * Derive all related parameters (angle, velocity, equation of the parabola, etc)
     *
     * @param   sling - bounding rectangle of the slingshot
     *          releasePoint - point where the mouse click was released from
     */
    public void setTrajectory(Rectangle sling, Point releasePoint)
    {
        // don't update parameters if the ref point and release point are the same
        if (_trajSet &&
            _ref != null && _ref.equals(getReferencePoint(sling)) &&
            _release != null && _release.equals(releasePoint))
            return;
            
        // set the scene parameters
        _scale = sling.height + sling.width;
        _ref = getReferencePoint(sling);
        
        // set parameters for the trajectory
        _release = new Point(releasePoint.x, releasePoint.y);
        
        // find the launch angle
        _theta = Math.atan2(_release.y - _ref.y, _ref.x - _release.x);
        _theta = launchToActual(_theta);
        
        // work out initial velocities and coefficients of the parabola
        _velocity = getVelocity(_theta);
        _ux = _velocity * Math.cos(_theta);
        _uy = _velocity * Math.sin(_theta);
        _a = -0.5 / (_ux * _ux);
        _b = _uy / _ux;
        
        
        // work out points of the trajectory
	    _trajectory = new ArrayList<Point>();
        for (int x = 0; x < X_MAX; x++) {
            double xn = x / _scale;
            int y = _ref.y - (int)((_a * xn * xn + _b * xn) * _scale);
            _trajectory.add(new Point(x + _ref.x, y));
        }
        
        // turn on the setTraj flag
        _trajSet = true;
    }
   
    /** 
     * Plot a trajectory
     *
     * @param   canvas - the canvas to draw onto
     *          slingshot - bounding rectangle of the slingshot
     *          releasePoint - point where the mouse click was released from
     * @return  the canvas with trajectory drawn
     */
    public BufferedImage plotTrajectory(BufferedImage canvas, Rectangle slingshot, Point releasePoint) {
        List<Point> trajectory = predictTrajectory(slingshot, releasePoint);
        
        // draw estimated trajectory
        Graphics2D g2d = canvas.createGraphics();
        g2d.setColor(Color.RED);
        for (Point p : trajectory) {
            if ((p.y > 0) && (p.y < canvas.getHeight(null))) {
                g2d.drawRect(p.x, p.y, 1, 1);
            }
        }
        
        return canvas; 
    }

    // plot trajectory given the bounding box of the active bird
    public BufferedImage plotTrajectory(BufferedImage canvas, Rectangle slingshot, Rectangle activeBird) {
        
        // get active bird location
        Point bird = new Point((int)(activeBird.x + 0.5 * activeBird.width),
            (int)(activeBird.y + 0.85 * activeBird.height));
        
        return plotTrajectory(canvas, slingshot, bird);
    }

    /** 
     * find the release point given the sling location and launch angle, using maximum velocity
     *
     * @param   sling - bounding rectangle of the slingshot
     *          theta - launch angle in radians (anticlockwise from positive direction of the x-axis)
     * @return  the release point on screen
     */
    public Point findReleasePoint(Rectangle sling, double theta)
    {
        double mag = sling.height * 10;
        Point ref = getReferencePoint(sling);
        Point release = new Point((int)(ref.x - mag * Math.cos(theta)), (int)(ref.y + mag * Math.sin(theta)));
        
        return release;
    }
    
    /** 
     * find the release point given the sling location, launch angle and velocity
     *
     * @param   sling - bounding rectangle of the slingshot
     *          theta - launch angle in radians (anticlockwise from positive direction of the x-axis)
     *          v - exit velocity as a proportion of the maximum velocity (maximum STRETCH)
     * @return  the release point on screen
     */
    public Point findReleasePoint(Rectangle sling, double theta, double v)
    {
        double mag = getSceneScale(sling) * STRETCH * v;
        Point ref = getReferencePoint(sling);
        Point release = new Point((int)(ref.x - mag * Math.cos(theta)), (int)(ref.y + mag * Math.sin(theta)));
        
        return release;
    }
    
    /**
     * find the reference point given the sling
     * @param sling
     * @return
     */
    public Point getReferencePoint(Rectangle sling)
    {
        Point p = new Point((int)(sling.x + X_OFFSET * sling.width), (int)(sling.y + Y_OFFSET * sling.width));
        return p;
    }
    
    /**
     * get release angle
     * @param sling
     * @param releasePoint
     * @return
     */
    public double getReleaseAngle(Rectangle sling, Point releasePoint )
    {
        Point ref = getReferencePoint(sling);
        
        return -Math.atan2(ref.y - releasePoint.y, ref.x - releasePoint.x);
    }
    
    /**
     * predicts a trajectory
     * @param slingshot
     * @param launchPoint
     * @return
     */
    public List<Point> predictTrajectory(Rectangle slingshot, Point launchPoint) {
        setTrajectory(slingshot, launchPoint);
	    return _trajectory;
    }
    
    /**
     * take the initial angle of the desired trajectory and return the launch angle required
     * @param theta
     * @return
     */
    private double actualToLaunch(double theta)
    {
        for (int i = 1; i < _launchAngle.length; i++)
        {
            if (theta > _launchAngle[i-1] && theta < _launchAngle[i])
                return theta + _changeAngle[i-1];
        }
        return theta + _changeAngle[_launchAngle.length-1];
    }
    
    /**
     * take the launch angle and return the actual angle of the resulting trajectory
     * @param theta
     * @return
     */
    private double launchToActual(double theta)
    {
        for (int i = 1; i < _launchAngle.length; i++)
        {
            if (theta > _launchAngle[i-1] && theta < _launchAngle[i])
                return theta - _changeAngle[i-1];
        }
        return theta - _changeAngle[_launchAngle.length-1];
    }
    
    /**
     * adjust the scale setting for this scene
     * @param v
     * @param theta
     */
    private void adjustScale(double v, double theta)
    {    
        int i = 0;
        while (i < _launchVelocity.length && theta > _launchAngle[i]) ++i;
        if (i == 0) i = 1;
        
        double temp = v / _launchVelocity[i-1];
            
        // avoid setting velocity to NaN
        if (temp != temp) return;
            
        // ignore very large changes
        if (temp > 1.1 || temp < 0.9)
           {
        	//System.out.println(" temp : " + temp);
        	return;
           }
                   
        if (theta > Math.toRadians(50)) {   
            _scaleFactor = temp;
        } else if (theta > Math.toRadians(25)) {
            _scaleFactor = temp * 0.6 + _scaleFactor * 0.4;
        }               
    }
    
    /**
     * get the velocity for the desired angle
     * @param theta
     * @return
     */
    public double getVelocity(double theta)
    {
        if (theta < _launchAngle[0]) {
            return _scaleFactor * _launchVelocity[0];
        }
        
        for (int i = 1; i < _launchAngle.length; i++)
        {
            if (theta < _launchAngle[i])
                return _scaleFactor * _launchVelocity[i-1];
        }
        
        return _scaleFactor * _launchVelocity[_launchVelocity.length-1];
    }
    
    /**
     * return scene scale determined by the sling size
     * @param sling
     * @return
     */
    private double getSceneScale(Rectangle sling)
    {
        return sling.height + sling.width;
    }
    
    /**
     * finds the active bird, i.e., the one in the slingshot
     * @param birds
     * @return
     */
    public Rectangle findActiveBird(List<Rectangle> birds) {
        // assumes that the active bird is the bird at the highest position
        Rectangle activeBird = null;
        for (Rectangle r : birds) {
            if ((activeBird == null) || (activeBird.y > r.y)) {
                activeBird = r;
            }
        }      
        return activeBird;
    }  
    
    /**
     * Get tapping time
     * @param sling
     * @param release
     * @param target
     * @param tapInterval
     * @return
     */
	public int getTapTime(Rectangle sling, Point release, Point target, int tapInterval)
	{
		if (tapInterval == 0) return 0;
		Point tapPoint = new Point();
		int distance = target.x - sling.x;
		double r = ((double)tapInterval/100);
		tapPoint.setLocation(new Point((int)(distance * r + sling.x) , target.y));
		return getTimeByDistance(sling, release, tapPoint);
	}
    
  
}
