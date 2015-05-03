package ab.server.proxy.message;

import org.json.simple.JSONObject;

import ab.server.ProxyMessage;

/**
 * interface for communicating messages between javascript plugin and server
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class ProxyDragMessage implements ProxyMessage<Object> {
	private int x, y, dx, dy;
	
	public ProxyDragMessage(int x, int y, int dx, int dy) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
	}
	
	@Override
	public String getMessageName() {
		return "drag";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSON() {
		JSONObject o = new JSONObject();
		o.put("x", x);
		o.put("y", y);
		o.put("dx", dx);
		o.put("dy", dy);
		return o;
	}
	
	@Override
	public Object gotResponse(JSONObject data) {
		return new Object();
	}
}
