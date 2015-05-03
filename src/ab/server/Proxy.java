package ab.server;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.SynchronousQueue;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Client-server proxy
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class Proxy extends WebSocketServer {
    private Long id = 0L;
    private HashMap<Long, ProxyResult<?>> results = new HashMap<Long, ProxyResult<?>>();
    
    /**
     * return proxy result
     * @author duruofei
     *
     * @param <T>
     */
    private class ProxyResult<T> {
        public ProxyMessage<T> message;
        public SynchronousQueue<Object> queue = new SynchronousQueue<Object>();
    }
	
    public Proxy(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public Proxy(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    	//System.out.println(conn.getRemoteSocketAddress().toString());
    	onOpen();
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        onClose();
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        JSONArray j = (JSONArray) JSONValue.parse(message);
        Long id = (Long) j.get(0);
        JSONObject data = (JSONObject) j.get(1);
      
        ProxyResult<?> result = results.get(id);
	
        if (result != null) {
            results.remove(id);
            try {
                result.queue.put(result.message.gotResponse(data));
            } catch (InterruptedException e) {
              
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }
    
    @SuppressWarnings("unchecked")
    public synchronized <T> T send(ProxyMessage<T> message) {
    	//Long t1 = System.nanoTime();
        JSONArray a = new JSONArray();
        a.add(id);
        a.add(message.getMessageName());
        a.add(message.getJSON());
        
        ProxyResult<T> result = new ProxyResult<T>();
        result.message = message;
        results.put(id, result);

        for (WebSocket conn : connections()) {	
            conn.send(a.toJSONString());
        }
        
        id++;
	
        try {
            return (T)result.queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void onOpen() { }
    
    public void onClose() { }
    
    public void waitForClients(int numClients) {
        while (connections().size() < numClients) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
}
