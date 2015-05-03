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
public class ProxyClickMessage implements ProxyMessage<Object> {
	private int x, y;
	
	public ProxyClickMessage(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String getMessageName() {
		return "click";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSON() {
		JSONObject o = new JSONObject();
		o.put("x", x);
		o.put("y", y);
		return o;
	}
	
	@Override
	public Object gotResponse(JSONObject data) {
		return new Object();
	}
}
