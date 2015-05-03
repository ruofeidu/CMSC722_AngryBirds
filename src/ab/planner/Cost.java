package ab.planner;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import ab.vision.ABObject;
import ab.vision.real.shape.Poly;
import ab.planner.TrajectoryPlanner;
import ab.vision.ABType;

public class Cost {

		private Rectangle ROOM;
		private TrajectoryPlanner tp;
		
		public Cost(Rectangle room, TrajectoryPlanner tp_){
			ROOM = new Rectangle(room);
			tp = tp_;
		}
		
	}
