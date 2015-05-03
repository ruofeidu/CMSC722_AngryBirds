package ab.planner;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import ab.server.Proxy;
import ab.server.proxy.message.ProxyScreenshotMessage;
import ab.utils.ImageSegFrame;
import ab.utils.Lib;
import ab.vision.GameStateExtractor;
import ab.vision.VisionMBR;
import ab.vision.VisionUtils;

/**
 * User Interface of the trajectory module
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and Dr. Vikas Shivashankar 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class abTrajectory {
    private static Proxy server;

    /**
     * Initialization
     */
    public abTrajectory() {
        if (server == null) {
            try {
            	server = new Proxy(9000) {
			@Override
            public void onOpen() {
				Lib.debug(Lib.flagSystem, "Client connected");
			}

			@Override
			public void onClose() {
				Lib.debug(Lib.flagSystem, "Client disconnected");
			}
                    };
		server.start();

		Lib.debug(Lib.flagSystem, "Server waited on port: " + server.getPort());
		server.waitForClients(1);

            } catch (UnknownHostException e) {
		e.printStackTrace();
            }
        }
    }

    /**
     * do a screen shot
     * @return
     */
    public BufferedImage doScreenShot() {
    	byte[] imageBytes = server.send(new ProxyScreenshotMessage());
        BufferedImage image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (IOException e) {
            // do something
        }
        return image;
    }

    /**
     * Test trajectory
     * @param args
     */
    public static void main(String args[]) {
        abTrajectory ar = new abTrajectory();

        ImageSegFrame frame = null;
        GameStateExtractor gameStateExtractor = new GameStateExtractor();
        TrajectoryPlanner trajectory = new TrajectoryPlanner();

        while (true) {
            // capture image
            BufferedImage screenshot = ar.doScreenShot();
            final int nHeight = screenshot.getHeight();
            final int nWidth = screenshot.getWidth();

            System.out.println("captured image of size " + nWidth + "-by-" + nHeight);

            // extract game state
            GameStateExtractor.GameState state = gameStateExtractor.getGameState(screenshot);
            if (state != GameStateExtractor.GameState.PLAYING) {
                continue;
            }

            // process image
            VisionMBR vision = new VisionMBR(screenshot);
            //List<Rectangle> pigs = vision.findPigsMBR();
            
            List<Rectangle> redBirds = vision.findRedBirdsMBRs();
            List<Rectangle> yellowBirds = vision.findYellowBirdsMBRs();
            List<Rectangle> blueBirds = vision.findBlueBirdsMBRs();
            List<Rectangle> whiteBirds = vision.findWhiteBirdsMBRs();
            List<Rectangle> blackBirds = vision.findBlackBirdsMBRs();
            List<Rectangle> birds = new LinkedList<Rectangle>();
            birds.addAll(redBirds);
            birds.addAll(yellowBirds);
            birds.addAll(blueBirds);
            birds.addAll(blackBirds);
            birds.addAll(whiteBirds);
            
            Rectangle sling = vision.findSlingshotMBR();
            if (sling == null) {
                System.out.println("...could not find the slingshot");
                continue;
            }
           // System.out.println("...found " + pigs.size() + " pigs and " + redBirds.size() + " birds");
            System.out.println("...found slingshot at " + sling.toString());

            // convert screenshot to grey scale and draw bounding boxes
            //screenshot = VisionUtils.convert2grey(screenshot);
            //VisionUtils.drawBoundingBoxes(screenshot, pigs, Color.GREEN);
            VisionUtils.drawBoundingBoxes(screenshot, redBirds, Color.PINK);
            VisionUtils.drawBoundingBox(screenshot, sling, Color.ORANGE);

            // find active bird
            Rectangle activeBird = trajectory.findActiveBird(birds);
            if (activeBird == null) {
                System.out.println("...could not find active bird");
                continue;
            }

            trajectory.plotTrajectory(screenshot, sling, activeBird);

            // show image
            if (frame == null) {
                frame = new ImageSegFrame("trajectory", screenshot);
            } else {
                frame.refresh(screenshot);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
}
