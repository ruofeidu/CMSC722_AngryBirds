package ab.vision;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Vision Module - framework
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class Vision {
	private BufferedImage image;
	private VisionMBR visionMBR = null;
	private VisionRealShape visionRealShape = null;
	
	public Vision(BufferedImage image)
	{
		this.image = image;
	}
	
	/**
	 * Find all birds~~~
	 * @return
	 */
	public List<ABObject> findBirdsMBR()
	{
		if (visionMBR == null)
		{
			visionMBR = new VisionMBR(image);
		} 
		return visionMBR.findBirds();
			
	}
	/**
	 * Find all kinds of blocks
	 * @return a list of MBRs of the blocks in the screenshot. Blocks: Stone, Wood, Ice
	 * */
	public List<ABObject> findBlocksMBR()
	{
		if (visionMBR == null)
		{
			visionMBR = new VisionMBR(image);
		}
		return visionMBR.findBlocks();
	}
	
	/**
	 * Find all TNTs
	 * @return
	 */
	public List<ABObject> findTNTs()
	{
		if(visionMBR == null)
		{
			visionMBR = new VisionMBR(image);
		}
		return visionMBR.findTNTs();
	}
	
	/**
	 * Find all Pigs
	 * @return
	 */
	public List<ABObject> findPigsMBR()
	{
		if (visionMBR == null)
		{
			visionMBR = new VisionMBR(image);
		}
		return visionMBR.findPigs();
	}
	
	/**
	 * Find the real shape of pigs
	 * @return
	 */
	public List<ABObject> findPigsRealShape()
	{
		if(visionRealShape == null)
		{
			visionRealShape = new VisionRealShape(image);
		}
		
		return visionRealShape.findPigs();
	} 
	
	/**
	 * Find the real shape of birds
	 * @return
	 */
	public List<ABObject> findBirdsRealShape()
	{
		if(visionRealShape == null)
		{
			visionRealShape = new VisionRealShape(image);
		}
		
		return visionRealShape.findBirds();
	}
	
	/**
	 * Final hills (triangles)
	 * @return
	 */
	public List<ABObject> findHills()
	{
		if(visionRealShape == null)
		{
			visionRealShape = new VisionRealShape(image);
		}
		
		return visionRealShape.findHills();
	} 
	
	/**
	 * Find the sling
	 * @return
	 */
	public Rectangle findSlingshotMBR()
	{
		if (visionMBR == null)
		{
			visionMBR = new VisionMBR(image);
		}
		return visionMBR.findSlingshotMBR();
	}
	
	/**
	 * Find trajectory point
	 * @return
	 */
	public List<Point> findTrajPoints()
	{
		if (visionMBR == null)
		{
			visionMBR = new VisionMBR(image);
		}
		return visionMBR.findTrajPoints();
	}
	
	/**
	 * Find the real shape of all objects
	 * @return a list of real shapes (represented by Body.java) of the blocks in the screenshot. Blocks: Stone, Wood, Ice 
	 * */
	public List<ABObject> findBlocksRealShape()
	{
		if(visionRealShape == null)
		{
			visionRealShape = new VisionRealShape(image);
		}
		List<ABObject> allBlocks = visionRealShape.findObjects();
		
		return allBlocks;
	}
	
	/**
	 * get visionMBR
	 * @return
	 */
	public VisionMBR getMBRVision()
	{
		if(visionMBR == null)
			visionMBR = new VisionMBR(image);
		return visionMBR;
	}
}
