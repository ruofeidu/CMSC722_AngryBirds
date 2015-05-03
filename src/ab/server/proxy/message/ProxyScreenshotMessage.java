package ab.server.proxy.message;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;

import ab.server.ProxyMessage;

/**
 * request a screenshot from the game
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class ProxyScreenshotMessage implements ProxyMessage<byte[]> {
    @Override
    public String getMessageName() {
        return "screenshot";
    }

    @Override
    public JSONObject getJSON() {
        return new JSONObject();
    }

    @Override
    public byte[] gotResponse(JSONObject data) {
        String imageStr = (String) data.get("data");
        imageStr = imageStr.split(",", 2)[1];
        byte[] imageBytes = Base64.decodeBase64(imageStr);
        return imageBytes;
    }
}
