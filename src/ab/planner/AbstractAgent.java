package ab.planner;

import java.awt.Rectangle;
import java.util.List;

import ab.vision.ABObject;
import ab.vision.GameStateExtractor.GameState;
import ab.vision.real.shape.Poly;

/**
 * Angry Bird Abstract Agent
 * 
 * May use IRPE or NOT
 * May use Greedy, DFFS and Astar with better heuristic functions
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @email {ruofei, gaozebao, xuzh} @ cs.umd.edu
 * @organization Unversity of Maryland, College Park
 * @date 04/04/2015
 * @advisor Prof. Dana Nau and Dr. Vikas Shivashankar 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz, Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public interface AbstractAgent extends Runnable {

	public abstract void setCurrentLevel(int lvl); 
	public abstract GameState solveCurrentLevel();
	public GameState destroyAllPigs(Rectangle sling, List<ABObject> pigs, List<ABObject> pigsObjects, List<ABObject> tnts, List<Poly> hills, GameState state);
	public GameState destroyPig(Rectangle sling, ABObject pig, List<ABObject> pigsObjects, List<ABObject> tnts, List<Poly> hills, GameState state);			
	public abstract void run();	
	
	
}
