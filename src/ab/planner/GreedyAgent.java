package ab.planner;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ab.actor.ActionRobot;
import ab.actor.Shot;
import ab.utils.ABUtil;
import ab.utils.StateUtil;
import ab.vision.ABObject;
import ab.vision.GameStateExtractor.GameState;
import ab.vision.real.shape.Poly;
import ab.vision.Vision;
import ab.utils.Lib;

/**
 * Angry Bird AI Greedy Agent for CMSC 722
 * 
 * Add back tracing and visited list based upon greedy heuristics 
 * Refinement after each planning enabled by letting IRPE = true; 
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @email {ruofei, gaozebao, xuzh} @ cs.umd.edu
 * @organization Unversity of Maryland, College Park
 * @date 04/04/2015
 * @advisor Prof. Dana Nau and Dr. Vikas Shivashankar 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz, Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class GreedyAgent implements AbstractAgent {

	private Map<Integer,Integer> scores = new LinkedHashMap<Integer,Integer>();
	private ActionRobot aRobot;
	private Random randomGenerator;
	TrajectoryPlanner tp;
	private static List<ABObject> pigList = null; 
	public int currentLevel = 1;
	public static int time_limit = 12;
	private boolean firstShot = true;
	private boolean IRPE = false; 
	private Point prevTarget;
	
	/**
	 * Initialize a greedy agent without IRPE
	 */
	public GreedyAgent(){
		this(false); 
	}
	
	/**
	 * Initialize a greedy agent
	 */
	public GreedyAgent(boolean enableIRPE) {
		aRobot = new ActionRobot();
		tp = new TrajectoryPlanner();
		prevTarget = null;
		firstShot = true;
		IRPE = enableIRPE; 
		randomGenerator = new Random();
		ActionRobot.GoFromMainMenuToLevelSelection();
	}

	/**
	 * Solve current angry bird level
	 * @return GameState
	 */
	public GameState solveCurrentLevel()
	{
		// capture screen shot
		BufferedImage screenshot = ActionRobot.doScreenShot();

		// process image using vision module
		Vision vision = new Vision(screenshot);

		// find the slingshot
		Rectangle sling = vision.findSlingshotMBR();
		
		// confirm the slingshot
		while (sling == null && aRobot.getState() == GameState.PLAYING) {
			Lib.debug(Lib.flagVision, "No slingshot detected, try to zoom out.");
			ActionRobot.fullyZoomOut();
			screenshot = ActionRobot.doScreenShot();
			vision = new Vision(screenshot);
			sling = vision.findSlingshotMBR();
		}
        // get all the pigs
 		//List<ABObject> pigs = vision.findPigsMBR();

		// scene detection
		List<ABObject> objects 		= new ArrayList<ABObject>();
		List<ABObject> pigs 		= new ArrayList<ABObject>();
		List<ABObject> pigsObjects  = new ArrayList<ABObject>();
		List<ABObject> tnts 		= new ArrayList<ABObject>();
		List<Poly> hills 			= new ArrayList<Poly>();
		ABObject mostDistantObj     = ABUtil.SceneDetection(vision, objects, pigs, pigsObjects, tnts, hills);
		if (this.firstShot) pigList = pigs; 
		Lib.debug(Lib.flagVision, "Statistics: " + pigs.size() + ", " + pigsObjects.size() + ", " + hills.size());
		
		GameState state = aRobot.getState();

		// if there is a sling, then play, otherwise just skip.
		if (sling != null) {
			if (IRPE) state = this.destroyAllPigs(sling, pigs, pigsObjects, tnts, hills, state); else
				state = this.destroyAllPigs(sling, pigList, pigsObjects, tnts, hills, state); 
		}
		return state;
	}
	
	/**
	 * Record score of current level
	 */
	public void recordScore() {
		int score = StateUtil.getScore(ActionRobot.proxy);
		if (!scores.containsKey(currentLevel))
			scores.put(currentLevel, score);
		else
		{
			if (scores.get(currentLevel) < score) scores.put(currentLevel, score);
		}
		int totalScore = 0;
		for (Integer key: scores.keySet()){
			totalScore += scores.get(key);
			Lib.debug(Lib.flagResult, "Level " + key + " gets " + scores.get(key));
		}
		Lib.debug(Lib.flagResult, "Total Score: " + totalScore);
		if (!Lib.repeatLevels) {
			++currentLevel;
		}
		aRobot.loadLevel(currentLevel);
		tp = new TrajectoryPlanner();
		firstShot = true;
	}
	
	/**
	 * Run the agent
	 */
	public void run() {
		if (this.IRPE) Lib.debug(Lib.flagSystem, "Running Greedy agent with IRPE"); else
			Lib.debug(Lib.flagSystem, "Running Greedy agent without IRPE");
		
		aRobot.loadLevel(currentLevel);
		while (true) {
			GameState state = solveCurrentLevel();
			if (state == GameState.WON) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.recordScore();
			} else if (state == GameState.LOST) {
				Lib.debug(Lib.flagSystem, "Restart");
				aRobot.restartLevel();
			} else if (state == GameState.LEVEL_SELECTION) {
				Lib.debug(Lib.flagSystem, "Unexpected level selection page, go to level : " + currentLevel);
				aRobot.loadLevel(currentLevel);
			} else if (state == GameState.MAIN_MENU) {
				Lib.debug(Lib.flagSystem, "Unexpected main menu page, go to level : " + currentLevel);
				ActionRobot.GoFromMainMenuToLevelSelection();
				aRobot.loadLevel(currentLevel);
			} else if (state == GameState.EPISODE_MENU) {
				Lib.debug(Lib.flagSystem, "Unexpected episode menu page, go to level : " + currentLevel);
				ActionRobot.GoFromMainMenuToLevelSelection();
				aRobot.loadLevel(currentLevel);
			}
		}
	}

	/**
	 * Eclead distance between two points
	 * @param p1
	 * @param p2
	 * @return distance in double
	 */
	private double distance(Point p1, Point p2) {
		return Math.sqrt((double) ((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)));
	}
	
	/**
	 * Get hit point
	 * @param pig
	 * @return
	 */
	public Point getHitPoint(ABObject pig) {
		Point hitPoint = pig.getCenter();// if the target is very close to before, randomly choose a
		// point near it
		if (prevTarget != null && distance(prevTarget, hitPoint) < 10) {
			double _angle = randomGenerator.nextDouble() * Math.PI * 2;
			hitPoint.x = hitPoint.x + (int) (Math.cos(_angle) * 10);
			hitPoint.y = hitPoint.y + (int) (Math.sin(_angle) * 10);
			Lib.debug(Lib.flagError, "Randomly changing to " + hitPoint);
		}

		prevTarget = new Point(hitPoint.x, hitPoint.y);
		return hitPoint; 
	}
	
	/**
	 * Estimate Launch Point
	 * @param sling
	 * @param hitPoint
	 * @return
	 */
	public Point estimateLaunchPoint(Rectangle sling, Point hitPoint) {
		Point releasePoint = null; 
		// estimate the trajectory
		ArrayList<Point> pts = tp.estimateLaunchPoint(sling, hitPoint);

		Lib.debug(Lib.flagPlanner, "Estimate launch point from " + pts.size());
		// do a high shot when entering a level to find an accurate velocity
		if ((firstShot || !IRPE) && pts.size() > 1) 
		{
			releasePoint = pts.get(1);
		}
		else if (pts.size() == 1)
			releasePoint = pts.get(0);
		else if (pts.size() == 2)
		{
			// randomly choose between the trajectories, with a 1 in
			// 6 chance of choosing the high one
			if (randomGenerator.nextInt(6) == 0)
				releasePoint = pts.get(1);
			else
				releasePoint = pts.get(0);
		}
		else if(pts.isEmpty())
		{
			Lib.debug(Lib.flagError, "no release point is found, try 45 degree");
			releasePoint = tp.findReleasePoint(sling, Math.PI / 4);
		}
		
		return releasePoint; 
	}
	
	/**
	 * Destroy a single pig
	 * @param sling
	 * @param pig
	 * @param pigsObjects
	 * @param tnts
	 * @param hills
	 * @param state
	 * @return
	 */
	public GameState destroyPig(Rectangle sling, ABObject pig, List<ABObject> pigsObjects, List<ABObject> tnts, List<Poly> hills, GameState state) {		
		Lib.debug(Lib.flagPlanner, "m2-destroyPig " + pig);
		Point hitPoint = this.getHitPoint(pig);
		Point launchPoint = this.estimateLaunchPoint(sling, hitPoint); 
		return this.shot(launchPoint, hitPoint, sling, pig, pigsObjects, tnts, hills, state);
	}
	
	/**
	 * Shot from launchPoint to hitPoint
	 * @param launchPoint
	 * @param hitPoint
	 * @param sling
	 * @param pig
	 * @param pigsObjects
	 * @param tnts
	 * @param hills
	 * @param state
	 * @return
	 */
	public GameState shot(Point launchPoint, Point hitPoint, Rectangle sling, ABObject pig, List<ABObject> pigsObjects, List<ABObject> tnts, List<Poly> hills, GameState state) {
		Shot shot = new Shot();
		int dx = 0, dy = 0;
		Point slingPoint = tp.getReferencePoint(sling);
		
		if (launchPoint != null) {
			double releaseAngle = tp.getReleaseAngle(sling, launchPoint);
			Lib.debug(Lib.flagPlanner, "launch point " + launchPoint + ", angle " + Math.toDegrees(releaseAngle));
			
			int tapTime = tp.getTapTime(sling, launchPoint, hitPoint, aRobot.getTapInterval());
			dx = (int)launchPoint.getX() - slingPoint.x;
			dy = (int)launchPoint.getY() - slingPoint.y;
			shot = new Shot(slingPoint.x, slingPoint.y, dx, dy, 0, tapTime);
		}
		else
		{
			Lib.debug(Lib.flagError, "failed shot, no release point found");
			return state; 
		}
		
		// check whether the slingshot is changed. the change of the slingshot indicates a change in the scale.
		ActionRobot.fullyZoomOut();
		BufferedImage screenshot = ActionRobot.doScreenShot();
		Vision vision = new Vision(screenshot);
		Rectangle _sling = vision.findSlingshotMBR();
		if (_sling != null)
		{
			double scale_diff = Math.pow((sling.width - _sling.width), 2) +  Math.pow((sling.height - _sling.height), 2);
			if (scale_diff < 25)
			{
				if (dx < 0)
				{
					aRobot.cshoot(shot);
					state = aRobot.getState();
					if ( state == GameState.PLAYING )
					{
						screenshot = ActionRobot.doScreenShot();
						vision = new Vision(screenshot);
						List<Point> traj = vision.findTrajPoints();
						tp.adjustTrajectory(traj, sling, launchPoint);
						firstShot = false;
					}
				}
			}
			else 
			{
				Lib.debug(Lib.flagActor, "scale is changed, can not execute the shot, will re-segement the image");
			}
		}
		else 
		{
			Lib.debug(Lib.flagActor, "no sling detected, can not execute the shot, will re-segement the image");
		}
		
		// garbage collection
		vision = null; 
		screenshot = null; 
		return state; 
	}
	
	/**
	 * Nondeterministically choose a pig
	 * @param pigs
	 * @return
	 */
	public ABObject nondeterministicallyChoosePig(List<ABObject> pigs, List<ABObject> pigsObjects) {
		// random pick up a pig as a start, then use heuristics to choose one
		ABObject pig = pigs.get(randomGenerator.nextInt(pigs.size())); 
		int argopt = pig.getGreedyHeuristics(); 
		for (ABObject p : pigs) {
			if (p.getGreedyHeuristics() < argopt) {
				argopt = p.getGreedyHeuristics(); 
				pig = p; 
			}
		}
		return pig; 
	}
	
	/**
	 * Refinement method: destroyAllPigs
	 * @param pigs
	 */
	public GameState destroyAllPigs(Rectangle sling, List<ABObject> pigs, List<ABObject> pigsObjects, List<ABObject> tnts, List<Poly> hills, GameState state) {
		Lib.debug(Lib.flagPlanner, "m1-destroyAllPigs: " + pigs.size() + " pigs");
		if (!pigs.isEmpty()) {
			ABObject pig = nondeterministicallyChoosePig(pigs, pigsObjects); 
			state = this.destroyPig(sling, pig, pigsObjects, tnts, hills, state);
			if (!IRPE) pigList.remove(pig); 
		}
		return state;
	}
	

	public void setCurrentLevel(int lvl) {
		this.currentLevel = lvl; 
	}


	/**
	 * Agent test
	 * @param args
	 */
	public static void main(String args[]) {
		GreedyAgent agent = new GreedyAgent();
		if (args.length > 0) agent.currentLevel = Integer.parseInt(args[0]);
		agent.run();

	}
}
