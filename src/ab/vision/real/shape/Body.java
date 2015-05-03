package ab.vision.real.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import ab.vision.ABObject;

/**
 * Vision Module - framework
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and Dr. Vikas Shivashankar 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public abstract class Body extends ABObject

{
	private static final long serialVersionUID = 1L;
	public Body()
	{
		super();
	}
    // position (x, y) as center of the object
    public double centerX = 0;
    public double centerY = 0;
 
    
    public static int round(double i)
    {
        return (int) (i + 0.5);
    }
    
    @Override
    public Point getCenter()
    {
    	Point point = new Point();
    	point.setLocation(centerX, centerY);
    	return point;
    }
    
    @Override
    public double getCenterX()
    {
    	return centerX;
    }
    
    @Override 
    public double getCenterY()
    {
    	return centerY;
    }

    public abstract void draw(Graphics2D g, boolean fill, Color boxColor);
}
