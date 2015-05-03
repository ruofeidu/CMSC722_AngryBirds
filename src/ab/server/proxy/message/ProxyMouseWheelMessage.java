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
public class ProxyMouseWheelMessage implements ProxyMessage<Object> {
	private int delta;
	
	/**
	 * Simulate a scroll of the mouse wheel
	 * 
	 * @param delta the direction to scroll (-1 = up, 1 = down)
	 */
	public ProxyMouseWheelMessage(int delta) {
		this.delta = delta;
	}
	
	@Override
	public String getMessageName() {
		return "mousewheel";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSON() {
		JSONObject o = new JSONObject();
		o.put("delta", delta);
		return o;
	}
	
	@Override
	public Object gotResponse(JSONObject data) {
		return new Object();
	}
}
