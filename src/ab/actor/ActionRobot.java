package ab.actor;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import ab.server.Proxy;
import ab.server.proxy.message.ProxyClickMessage;
import ab.server.proxy.message.ProxyDragMessage;
import ab.server.proxy.message.ProxyMouseWheelMessage;
import ab.server.proxy.message.ProxyScreenshotMessage;
import ab.utils.StateUtil;
import ab.utils.Lib;
import ab.vision.ABObject;
import ab.vision.ABType;
import ab.vision.GameStateExtractor.GameState;
import ab.vision.Vision;

/**
 * Angry Bird AI Action Robot
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @email {ruofei, gaozebao, xuzh} @ cs.umd.edu
 * @organization Unversity of Maryland, College Park
 * @date 04/04/2015
 * @advisor Prof. Dana Nau and Dr. Vikas Shivashankar 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz, Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class ActionRobot {
	public static Proxy proxy;
	public String level_status = "UNKNOWN";
	public int current_score = 0;
	private LoadLevelSchema lls;
	private RestartLevelSchema rls;
	
	/**
	 * Singleton
	 */
	static {
		if (proxy == null) {
			try {
				proxy = new Proxy(9000) {
					@Override
					public void onOpen() {
						Lib.debug(Lib.flagSystem, "Client connected");
					}

					@Override
					public void onClose() {
						Lib.debug(Lib.flagSystem, "Client disconnected");
					}
				};
				proxy.start();
				Lib.debug(Lib.flagSystem, "Server waiting on port: " + proxy.getPort());
				proxy.waitForClients(1);

			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A java util class for the standalone version. 
	 * It provides common functions an agent would use. e.g. get the screenshot
	 */
	public ActionRobot() {
		lls = new LoadLevelSchema(proxy);
		rls = new RestartLevelSchema(proxy);
	}

	/**
	 * restart a level
	 */
	public void restartLevel() {
		rls.restartLevel();
	}

	/**
	 * go from the main menu to the episode menu
	 * @default go to the Poached Eggs
	 */
	public static void GoFromMainMenuToLevelSelection() {
		GameState state = StateUtil.getGameState(proxy);
		while (state == GameState.MAIN_MENU) {

			Lib.debug(Lib.flagSystem, "Go to the Episode Menu");
			proxy.send(new ProxyClickMessage(305, 277));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			state = StateUtil.getGameState(proxy);
		}

		while (state == GameState.EPISODE_MENU) {
			Lib.debug(Lib.flagSystem, "Select the Poached Eggs Episode");
			proxy.send(new ProxyClickMessage(150, 300));
			state = StateUtil.getGameState(proxy);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			state = StateUtil.getGameState(proxy);
		}

	}

	/**
	 * Shoot with state info returned
	 * @param csc
	 * @return NKNOWN, MAIN_MENU, EPISODE_MENU, LEVEL_SELECTION, LOADING, PLAYING, WON, LOST
	 */
	public GameState shootWithStateInfoReturned(List<Shot> csc) {
		ShootingSchema ss = new ShootingSchema();
		ss.shoot(proxy, csc);
		System.out.println("Shooting Completed");
		GameState state = StateUtil.getGameState(proxy);
		return state;

	}

	/**
	 * Get current gaming state
	 * @return UNKNOWN, MAIN_MENU, EPISODE_MENU, LEVEL_SELECTION, LOADING, PLAYING, WON, LOST
	 */
	public synchronized GameState getState() {
		GameState state = StateUtil.getGameState(proxy);
		return state;
	}

	/**
	 * Conduct a list of shots
	 * @param csc
	 */
	public void shoot(List<Shot> csc) {
		ShootingSchema ss = new ShootingSchema();

		ss.shoot(proxy, csc);
		Lib.debug(Lib.flagActor, "Shooting Completed");
		Lib.debug(Lib.flagActor, "wait 15 seconds to ensure all objects in the scene static");
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shoot and wait 10s
	 * @param shot
	 */
	public void cshoot(Shot shot) {
		Lib.debug(Lib.flagActor, "Shooting starts " + shot.getX() + ", " + shot.getY() + ", " + shot.getDx() + ", " + shot.getDy());
		ShootingSchema ss = new ShootingSchema();
		LinkedList<Shot> shots = new LinkedList<Shot>();
		shots.add(shot);
		ss.shoot(proxy, shots);
		try {
			Thread.sleep(10000);
			Lib.debug(Lib.flagActor, "Shooting completes");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shoot without any waiting
	 * @param shot
	 */
	public void cFastshoot(Shot shot) {
		ShootingSchema ss = new ShootingSchema();
		LinkedList<Shot> shots = new LinkedList<Shot>();
		shots.add(shot);
		ss.shoot(proxy, shots);
	}

	/**
	 * Fast shoot
	 * @param shot
	 */
	public void fshoot(Shot shot) {
		ShootingSchema ss = new ShootingSchema();
		LinkedList<Shot> shots = new LinkedList<Shot>();
		shots.add(shot);
		ss.shoot(proxy, shots);
		
		Lib.debug(Lib.flagActor, "Tap time: " + shot.getT_tap());
		Lib.debug(Lib.flagActor, "Shooting Completed");
	}

	/**
	 * Trigger a click message
	 */
	public void click() {
		proxy.send(new ProxyClickMessage(100, 100));
	}

	/**
	 * Trigger a void dragging action
	 */
	public void drag() {
		proxy.send(new ProxyDragMessage(0, 0, 0, 0));
	}

	/**
	 * Load levels
	 * @param i
	 */
	public void loadLevel(int... i) {
		int level = 1;
		
		if (i.length > 0) {
			level = i[0];
		}

		lls.loadLevel(level);
	}

	/**
	 * Zoom out to see everything
	 */
	public static void fullyZoomOut() {
		for (int k = 0; k < 15; k++) {
			proxy.send(new ProxyMouseWheelMessage(-1));
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Zoom in
	 */
	public static void fullyZoomIn() {
		for (int k = 0; k < 15; k++) {
			proxy.send(new ProxyMouseWheelMessage(1));
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * capture the screen
	 * @return buffered image
	 */
	public static BufferedImage doScreenShot() {
		byte[] imageBytes = proxy.send(new ProxyScreenshotMessage());
		BufferedImage image = null;
		try {
			image = ImageIO.read(new ByteArrayInputStream(imageBytes));
		} catch (IOException e) {

		}

		return image;
	}
	
	/**
	 * See what's on the sling
	 * @return type of the bird
	 */
	public ABType getBirdTypeOnSling()
	{
		fullyZoomIn();
		BufferedImage screenshot = doScreenShot();
		Vision vision = new Vision(screenshot);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		fullyZoomOut();
		List<ABObject> _birds = vision.findBirdsMBR();
		if(_birds.isEmpty())
			return ABType.Unknown;
		Collections.sort(_birds, new Comparator<Rectangle>(){
			@Override
			public int compare(Rectangle o1, Rectangle o2) {
				return ((Integer)(o1.y)).compareTo((Integer)(o2.y));
			}	
		});
		return _birds.get(0).getType();
	}
	
	public int getTapInterval() {
		Random randomGenerator = new Random();
		
		switch (this.getBirdTypeOnSling()) 
		{
			case RedBird: 
				return 0; 
			case YellowBird:
				return 65 + randomGenerator.nextInt(25);  // 65-90% of the way
			case WhiteBird:
				return 70 + randomGenerator.nextInt(20);  // 70-90% of the way
			case BlackBird:
				return 70 + randomGenerator.nextInt(20);  // 70-90% of the way
			case BlueBird:
				return 65 + randomGenerator.nextInt(20);  // 65-85% of the way
			default:
				return 60;
		}
	}
	

	/**
	 * A test of the actor
	 * @param args
	 */
	public static void main(String args[]) {
		long time = System.currentTimeMillis();
		ActionRobot.doScreenShot();
		time = System.currentTimeMillis() - time;
		Lib.debug(Lib.flagActor, "Cost: " + time);
		time = System.currentTimeMillis();
		int count = 0;
		while (count < 40) {
			ActionRobot.doScreenShot();
			count++;
		}

		Lib.debug(Lib.flagActor, "Time to take 40 screenshots" + (System.currentTimeMillis() - time));
		System.exit(0);

	}

	/**
	 * Get the score from the screenshot
	 * @return
	 */
	public int getScore() {
		return StateUtil.getScore(ActionRobot.proxy);
	}
}
