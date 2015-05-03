package ab.server;

import org.json.simple.JSONObject;

/**
 * interface for communicating messages between javascript plugin and server
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public interface ProxyMessage<T> {
    public String getMessageName();
    public JSONObject getJSON();
    public T gotResponse(JSONObject data);
}
