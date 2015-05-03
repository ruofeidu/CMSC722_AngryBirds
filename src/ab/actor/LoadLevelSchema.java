package ab.actor;

import ab.server.Proxy;
import ab.server.proxy.message.ProxyClickMessage;
import ab.server.proxy.message.ProxyMouseWheelMessage;
import ab.utils.Lib;
import ab.utils.StateUtil;
import ab.vision.GameStateExtractor.GameState;

/**
 * Schema for loading level 
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @email {ruofei, gaozebao, xuzh} @ cs.umd.edu
 * @organization Unversity of Maryland, College Park
 * @date 04/04/2015
 * @advisor Prof. Dana Nau and Dr. Vikas Shivashankar 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz, Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class LoadLevelSchema {
private Proxy proxy;
private boolean pageSwitch = false;

/**
 * Load level schema from proxy
 * @param proxy
 */
public LoadLevelSchema(Proxy proxy)
{
	this.proxy = proxy;
}

/**
 * Load certain level and page switch
 * @param i
 * @return
 */
public boolean loadLevel(int i)
{
	
	if (i > 21)
	{	 
		if ( i == 22 || i == 43 ) pageSwitch = true;
		i =(  (i % 21) == 0) ? 21 : i%21;	    	
	} 
	loadLevel(StateUtil.getGameState(proxy),i);

	GameState state = StateUtil.getGameState(proxy);
	
	while (state!= GameState.PLAYING)
	{
		Lib.debug(Lib.flagSystem, "In state:   " + state + " Try reloading...");
		loadLevel(state,i);
		try {
			Thread.sleep(12000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		state = StateUtil.getGameState(proxy);
	}
	return true;
}

/**
 * Load level with state
 * @param state
 * @param i
 * @return
 */
private boolean loadLevel(GameState state,int i)
{
    // if still at main menu or episode menu, skip it. 
	ActionRobot.GoFromMainMenuToLevelSelection();
	
	if(state == GameState.WON || state == GameState.LOST)
	{
		/*if(state == GameState.WON && i >= current + 1)
			  proxy.send(new ProxyClickMessage(500,375)); // go to the next level
*/	/*if(state == GameState.WON)*/ 
		{ 
		 
		 	proxy.send(new ProxyClickMessage(342,382));//Click the left most button at the end page
		 	try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		 	if(pageSwitch)
		 	{
		 		 proxy.send(new ProxyClickMessage(378, 451)); 
		 		try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
		 		pageSwitch = false;
		 	}
			 proxy.send(new ProxyClickMessage(54 + ((i-1)%7) * 86,110 + ((i-1)/7) * 100)); 
		 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
	 }
		 if(i == 1)
		//skip the animation, the animation does not appear in the SD mode.
			 proxy.send(new ProxyClickMessage(1176,704)); 
	}
	else if(state == GameState.PLAYING)
		{
			proxy.send(new ProxyClickMessage(48,44));//Click the left most button, pause
			  try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {

					e1.printStackTrace();
				}
			  proxy.send(new ProxyClickMessage(168,28));//Click the left most button, pause
			  try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			  if(pageSwitch)
			 	{
			 		 proxy.send(new ProxyClickMessage(378, 451)); 
			 		try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
			 		pageSwitch = false;
			 	}
				 proxy.send(new ProxyClickMessage(54 + ((i-1)%7) * 86,110 + ((i-1)/7) * 100)); 
			  try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
		
					e.printStackTrace();
				}
				 if(i == 1)
					 proxy.send(new ProxyClickMessage(1176,704)); 
		}
	else
	{
		if(pageSwitch)
	 	{
	 		 proxy.send(new ProxyClickMessage(378, 451)); 
	 		try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
	 		pageSwitch = false;
	 	}
		proxy.send(new ProxyClickMessage(54 + ((i-1)%7) * 86,110 + ((i-1)/7) * 100)); 
		 try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		
			e.printStackTrace();
		}
		 if(i == 1)
			 proxy.send(new ProxyClickMessage(1176,704)); 
	}
	
	//Wait 9000 seconds for loading the level
	GameState _state = StateUtil.getGameState(proxy);
	int count = 0; // at most wait 10 seconds
	while (_state!= GameState.PLAYING && count < 3)
	{
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		count ++;
		_state = StateUtil.getGameState(proxy);
	}
	   
	if (_state == GameState.PLAYING) {
	   for (int k = 0; k < 15; k++) 
	   {
		   proxy.send(new ProxyMouseWheelMessage(-1));
	   }
	   
	   try {
			Thread.sleep(2500);
	   } catch (InterruptedException e1) {	
			e1.printStackTrace();
	   }
     }
     return true;

}
}
