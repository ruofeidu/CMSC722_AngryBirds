package ab.actor;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import ab.server.Proxy;
import ab.server.proxy.message.ProxyDragMessage;

/**
 * Shooting Schema for standalone version.
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and Dr. Vikas Shivashankar 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class ShootingSchema {
private boolean shootImmediately = true;

/**
 * Shoot a list of shots via proxy
 * @param proxy
 * @param csc
 */
public void shoot(final Proxy proxy, List<Shot> csc)
{
	int count = 0;
	LinkedList<Shot> shots = new LinkedList<Shot>();	
    for (Shot shot : csc)
    {
    	if (shot.getT_shot() != 0) shootImmediately = false;
    	if (shootImmediately)
    	{
    		int t_shot = 5000 * count++;
    		shots.add(new Shot(shot.getX(), shot.getY(), shot.getDx(), shot.getDy(), t_shot));
    		if (shot.getT_tap() > 0)
        		shots.add(new Shot(0, 0, 0, 0, shot.getT_tap() + t_shot));
    	}
    	else
    	{
    		shots.add(new Shot(shot.getX(), shot.getY(), shot.getDx(), shot.getDy(), shot.getT_shot()));
    		if (shot.getT_tap() > 0)
        		shots.add(new Shot(0, 0, 0, 0, shot.getT_tap() + shot.getT_shot()));
    	}
    	
    }
	
	Collections.sort(shots, new Comparator<Shot>(){
		@Override
		public int compare(Shot arg0, Shot arg1) {
			return ((Integer)arg0.getT_shot()).compareTo(arg1.getT_shot());
		}
    });
	
	if (!shots.isEmpty())
	{
	   int start_time = shots.getFirst().getT_shot();
	   //Optimize for one shot one time..
	   if (shots.size() < 3)
	   {
		   Shot shot = shots.getFirst();
		   if (shots.size() == 2)
		   {
			   Shot _shot = shots.getLast();
			   int wait_time = (_shot.getT_shot() - start_time)==0? start_time:(_shot.getT_shot()- start_time);
			   long _gap = System.currentTimeMillis();
			   proxy.send(new ProxyDragMessage(shot.getX(),shot.getY(),shot.getDx(),shot.getDy()));
			   long gap = System.currentTimeMillis() - _gap;
			   wait_time -= gap;
			   if (wait_time < 0) wait_time = 0;
			   //long time = System.nanoTime();
			   try {
				   Thread.sleep(wait_time);
			   } catch (InterruptedException e) {
				   e.printStackTrace();
			   }
			   /*
			    * long _time = System.nanoTime();
			    * System.out.println(" waiting time:" + (time - _time));
			    **/
			   proxy.send(new ProxyDragMessage(_shot.getX(),_shot.getY(),_shot.getDx(),_shot.getDy()));
		  } else 
		  {
			  proxy.send(new ProxyDragMessage(shot.getX(),shot.getY(),shot.getDx(),shot.getDy()));
			  //System.out.println(shot.getX() + "  " + shot.getY() + "  " + shot.getDx() + " " + shot.getDy());
		  }   
	   }
	   else
	   {  
		   long gap = 0;
		   for (Shot _shot: shots)
		   {
			   long wait_time = (_shot.getT_shot() - start_time - gap) <=0 ? 0 : (_shot.getT_shot() - start_time - gap);
			   
			   try {
				   Thread.sleep(wait_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			   
			    start_time = _shot.getT_shot();
				gap = System.currentTimeMillis();
				proxy.send(new ProxyDragMessage(_shot.getX(), _shot.getY(), _shot.getDx(), _shot.getDy()));
		        gap = System.currentTimeMillis() - gap;
		   }
	   }
	}
	}

}
