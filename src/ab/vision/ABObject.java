package ab.vision;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Generic object in Angry Birds
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class ABObject extends Rectangle {
	private static final long serialVersionUID = 1L;
	private static int counter = 0;
	public int weight; 
	public int id;
	/**
	 * object type
	 */
	public ABType type;
	/**
	 * rectangle area
	 */
	public int area = 0;
	/**
	 * For all MBRs, the shape is Rect by default.
	 */
	public ABShape shape = ABShape.Rect;
 
	/**
	 * For all MBRs, the angle is 0 by default.
	 */
	public double angle = 0;
 
	/**
	 * is Hollow or not
	 */
	public boolean hollow = false;
 
	/**
	 * 
	 * @param mbr
	 * @param type
	 */
	public ABObject(Rectangle mbr, ABType type) {
		super(mbr);
		this.type = type;
		this.id = counter++;
	}
	
	public ABObject(Rectangle mbr, ABType type, int id) {
		super(mbr);
		this.type = type;
		this.id = id;
	}
	
	
	
	public ABObject(ABObject ab)
	{
		super(ab.getBounds());
		this.type = ab.type;
		this.id = ab.id;
	}
	
	public ABObject()
	{
		this.id = counter ++;
		this.type = ABType.Unknown;
	}
	
	/**
	 * get type
	 * @return
	 */
	public ABType getType()
	{
		return type;
	}
	
	public int getGreedyHeuristics() {
		return (int)getCenterX(); 
	}
	
	/**
	 * get center
	 * @return
	 */
	public Point getCenter() {
	   return new Point((int)getCenterX(), (int)getCenterY());
	}
	
	/**
	 * reset counter
	 */
	public static void resetCounter() {
		counter = 0;	
	}
}
