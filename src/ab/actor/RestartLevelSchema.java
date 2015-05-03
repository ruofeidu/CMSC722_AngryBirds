package ab.actor;

import ab.server.Proxy;
import ab.server.proxy.message.ProxyClickMessage;
import ab.server.proxy.message.ProxyMouseWheelMessage;
import ab.utils.StateUtil;
import ab.vision.GameStateExtractor.GameState;

public class RestartLevelSchema {
private Proxy proxy;

/**
 * This schema is used for automatically restarting levels in the standalone version. 
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and Dr. Vikas Shivashankar 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public RestartLevelSchema(Proxy proxy)
{
	this.proxy = proxy;
}
public boolean restartLevel()
{
	GameState state = StateUtil.getGameState(proxy);
	
	if(state == GameState.WON || state == GameState.LOST)
	{
		proxy.send( new ProxyClickMessage(420,380));//Click the left most button at the end page
		System.out.println(" restart the level ");
		  try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
	}
	else if(state == GameState.PLAYING)
		{
			proxy.send(new ProxyClickMessage(100,39));
			  try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
		}

	  //Wait 4000 seconds for loading the level
	   try {
			Thread.sleep(4000);
		} catch (InterruptedException e1) {
		
			e1.printStackTrace();
		}
	   //Zooming out
	   System.out.println("Zooming out");
	   for (int k = 0; k < 15; k++) 
	   {
		   proxy.send(new ProxyMouseWheelMessage(-1));
	   }
	   try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
	
			e1.printStackTrace();
		}

	   return true;

}
}
