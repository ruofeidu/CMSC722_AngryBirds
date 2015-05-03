package ab.demo;

import ab.planner.AbstractAgent;
import ab.planner.AstarAgent;
import ab.planner.DFFSAgent;
import ab.planner.GreedyAgent;
import ab.planner.abTrajectory;
import ab.utils.GameImageRecorder;
import ab.utils.Lib; 
import ab.vision.ShowSeg;

/**
 * Angry Bird AI Agents for CMSC 722
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and Dr. Vikas Shivashankar 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class MainEntry {
	public static void main(String args[])
	{
		Lib.enableDebugFlags("svpare");
		
		String command = "";
		if (args.length == 0) {
			Lib.debug(Lib.flagError, "Please input the correct command");
			return; 
		}
		AbstractAgent agent = null; 
		command = args[0];

		Lib.args = command;
		Lib.debug(Lib.flagSystem, "Program starts with configuration " + Lib.args);

		Lib.IRPE = false; 
		if (command.contains("i")) {
			Lib.IRPE = true; 
		}
		if (command.contains("r")) {
			Lib.repeatLevels = true; 
		}
		
		if (command.contains("g")) {
			agent = new GreedyAgent(Lib.IRPE);
		} else 
		if (command.contains("d")) {
			agent = new DFFSAgent(Lib.IRPE);
		} else {
			agent = new AstarAgent(Lib.IRPE); 
		}	
		
		if (args.length == 1)
		{
			agent.run();
		}
		else 
		if (args.length == 2)
		{
			int initialLevel = 1;
			try{
				initialLevel = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e)
			{
				Lib.debug(Lib.flagError, "wrong level number, will use the default one");
			}
			
			agent.setCurrentLevel(initialLevel);
			agent.run();
		}
		else {
			Lib.debug(Lib.flagError, "Please input the correct command");
		}
	}
}
