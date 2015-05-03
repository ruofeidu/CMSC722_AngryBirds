package ab.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ab.actor.Shot;
import ab.planner.TrajectoryPlanner;
import ab.vision.ABObject;
import ab.vision.Vision;
import ab.vision.real.shape.Poly;

/**
 * interface for communicating messages between javascript plugin and server
 * modification made by CMSC 722 team
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class ABUtil {
	
	public static int gap = 5; //vision tolerance.
	private static TrajectoryPlanner tp = new TrajectoryPlanner();

	/**
	 * If o1 supports o2, return true
	 * @param o2
	 * @param o1
	 * @return
	 */
	public static boolean isSupport(ABObject o2, ABObject o1)
	{
		if(o2.x == o1.x && o2.y == o1.y && o2.width == o1.width && o2.height == o1.height)
				return false;
		
		int ex_o1 = o1.x + o1.width;
		int ex_o2 = o2.x + o2.width;
		
		int ey_o2 = o2.y + o2.height;
		if(
			(Math.abs(ey_o2 - o1.y) < gap)
			&&  
 			!( o2.x - ex_o1  > gap || o1.x - ex_o2 > gap )
		  )
	        return true;	
		
		return false;
	}
	
	/**
	 * Detect all kinds of scene objects in the scene
	 * @param vision vision module
	 * @param objects hittable objects
	 * @param pigs targets
	 * @param pigsObjects pigs + hittable objects
	 * @param tnts TNT
	 * @param hills hills
	 * @return
	 */
	public static ABObject SceneDetection(Vision vision, List<ABObject> objects, List<ABObject> pigs, List<ABObject> pigsObjects, List<ABObject> tnts, List<Poly> hills) {
		Lib.debug(Lib.flagVision, "Detecting objects in the Angry Birds using vision module");
		// get all objects: wood, stone, ice
		List<ABObject> objectsReal = vision.findBlocksRealShape();
		List<ABObject> objectsMBR  = vision.findBlocksMBR();
		List<ABObject> objectsBirds = vision.findBirdsMBR(); 
	
		// Keep the representation which detects the objects correctly
		if(objectsReal.size() - objectsMBR.size() <= -10){
			objects.addAll(objectsMBR);
		}
		else{
			objects.addAll(objectsReal);
		}
		
		// detect TNTs and add them to list used for tree construction
		tnts.addAll(vision.getMBRVision().findTNTs());
		for (int i = 0; i < tnts.size(); i++) objects.add(tnts.get(i));
		Lib.debug(Lib.flagVision, "Detect TNTs " + tnts.size());
		
		// findRealShape() mis-detects Pigs, so we use findPigsMBR() for Pig detection
		pigs.addAll(vision.findPigsMBR());
			
		// pop "empty" and "unknown" objects from our list
		if (!objects.isEmpty()){
			for (int i = 0; i < objects.size(); i++){
				String test = new String(objects.get(i).type.toString());
				
				if (objects.get(i).isEmpty()){
					//Lib.debug(Lib.flagVision, "Empty object removed....");
					objects.remove(i);
					i = i-1;
				}
				else if (test.equals("Unknown")){
					//Lib.debug(Lib.flagVision, "Unknown object removed....");
					objects.remove(i);
					i = i-1;
				}
				test = null;
			}
		}
		
		// add pigs and rolling stones in a list
		List<ABObject> PigsAndRolling = new ArrayList<ABObject>();
		int numWoods = 0, numIces = 0, numStones = 0;
		
		if (!pigs.isEmpty()) PigsAndRolling.addAll(pigs);			
						
		if (!objects.isEmpty()) {
			for (int i = 0; i < objects.size(); i++){
				String test = new String(objects.get(i).type.toString());
						
				if (test.equals("Stone") && objects.get(i).shape.toString().equals("Circle")) {
					PigsAndRolling.add(objects.get(i));
				}		
				//
				//Ice(10), 
				//Wood(11), 
				//Stone(12), 
				if (test.equals("Ice")) numIces++; 
				if (test.equals("Stone")) numStones++; 
				if (test.equals("Wood")) numWoods++; 
				
				test = null;
			}
		}
		Lib.debug(Lib.flagVision, "Detect pigs and rolling " + PigsAndRolling.size() + ", glass " + numIces + ", stone " + numStones + ", wood " + numWoods +  ", birds" + objectsBirds.size());
			
		// find most distant pig or rolling stone in the scene
		// to make the most "right" Nodes infeasible
		ABObject mostDistantObj = null;
		if (!PigsAndRolling.isEmpty()){
			mostDistantObj = PigsAndRolling.get(0);
					
			for (int i = 1; i < PigsAndRolling.size(); i++) {
				if (PigsAndRolling.get(i).getCenterX() > mostDistantObj.getCenterX()) {
					mostDistantObj = PigsAndRolling.get(i);
				}
			}
		}
		
		// list of objects used at tree creation 
		pigsObjects.addAll(pigs);
		pigsObjects.addAll(objects);

		// Discover hills inside the scene
		List<ABObject> hillstmp = vision.findHills();
		
		for (int i=0; i<hillstmp.size(); i++)
			hills.add((Poly)hillstmp.get(i));
	
		hillstmp.clear();
		hillstmp = null;
		
		// return the most right object to make nodes infeasible
		return mostDistantObj;
	}
	
	/**
	 * return a link list of ABObjects that support o1 (test by isSupport function ). 
	 * objs refers to a list of potential supporters.
	 * empty list will be returned if no such supporters. 
	 * 
	 * @param o2
	 * @param objs
	 * @return
	 */
	public static List<ABObject> getSupporters(ABObject o2, List<ABObject> objs)
	{
		List<ABObject> result = new LinkedList<ABObject>();
		//Loop through the potential supporters
        for (ABObject o1: objs) {
        	if (isSupport(o2,o1)) result.add(o1);
        }
        return result;
	}

	/**
	 * Return true if the target can be hit by releasing the bird at the specified release point
	 * @param vision
	 * @param target
	 * @param shot
	 * @return
	 */
	public static boolean isReachable(Vision vision, Point target, Shot shot)
	{ 
		//test whether the trajectory can pass the target without considering obstructions
		Point releasePoint = new Point(shot.getX() + shot.getDx(), shot.getY() + shot.getDy()); 
		int traY = tp.getYCoordinate(vision.findSlingshotMBR(), releasePoint, target.x);
		if (Math.abs(traY - target.y) > 100)
		{	
			//System.out.println(Math.abs(traY - target.y));
			return false;
		}
		
		boolean result = true;
		List<Point> points = tp.predictTrajectory(vision.findSlingshotMBR(), releasePoint);		
		for (Point point : points) {
			if (point.x < 840 && point.y < 480 && point.y > 100 && point.x > 400)
				for (ABObject ab: vision.findBlocksMBR()) {
					if ( 
						((ab.contains(point) && !ab.contains(target))||Math.abs(vision.getMBRVision()._scene[point.y][point.x] - 72 ) < 10) 
						&& point.x < target.x )
						return false;
			}
		}
		return result;
	}
	
	
	/**
	 *  function for pause 
	 */
	public static void mySleep(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
