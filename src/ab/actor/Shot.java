package ab.actor;

/**
 * Shooting Schema
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and Dr. Vikas Shivashankar 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class Shot {
	
	private int x;
	private int y;
	private int dx;
	private int dy;
	
	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}


	public int getDy() {
		return dy;
	}

	/**
	 * Shoot a targeted position
	 * @param x
	 * @param y
	 * @param dx
	 * @param dy
	 * @param t_shot
	 * @param t_tap
	 */
	public Shot(int x, int y, int dx, int dy, int t_shot, int t_tap) {
		super();
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.t_shot = t_shot;
		this.t_tap = t_tap;
	}
	
	public Shot(int x, int y, int dx, int dy, int t_shot) {
		super();
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.t_shot = t_shot;
	}


	public void setDy(int dy) {
		this.dy = dy;
	}
	
	private int t_shot;
	private int t_tap;

	public Shot()
	{
		x = 0;
		y = 0;
		dx = 0;
		dy = 0;
		t_shot = 0;
		t_tap = 0;
	}


	public Shot(int x, int y, int t_shot, int t_tap) {
		super();
		this.x = x;
		this.y = y;
		this.t_shot = t_shot;
		this.t_tap = t_tap;
	}


	public int getX() {
		return x;
	}
	
	
	public void setX(int x) {
		this.x = x;
	}
	
	
	public int getY() {
		return y;
	}
	
	
	public void setY(int y) {
		this.y = y;
	}
	
	
	public int getT_shot() {
		return t_shot;
	}
	
	
	public void setT_shot(int t_shot) {
		this.t_shot = t_shot;
	}
	
	
	public int getT_tap() {
		return t_tap;
	}
	
	
	public void setT_tap(int t_tap) {
		this.t_tap = t_tap;
	}
	
	public String toString()
	{
		String result = "";
		if (x == 0 && y == 0)
		{
			if (t_tap != 0) result += "tap at:  " + t_tap;
		}
		else
			result += "Shoot from: ("+ (x + dx)+ "  " + (y + dy) +" )" + " at time  " + t_shot ;	
		
	   return  result;
	
	}

}
