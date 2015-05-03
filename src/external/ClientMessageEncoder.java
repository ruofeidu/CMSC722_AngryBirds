package external;

/**
 * encode the messages to byte[]
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and Dr. Vikas Shivashankar 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class ClientMessageEncoder {

	/**
	 * encode screenshot message
	 * @return
	 */
	public static byte[] encodeDoScreenShot() {
		byte[] message = { ClientMessageTable
				.getValue(ClientMessageTable.doScreenShot) };
		return message;

	}
  
	/**
	 * encode configure message
	 * @param id
	 * @return
	 */
	public static byte[] configure(byte[] id) {
		byte[] message = new byte[1 + id.length];
		message = mergeArray(
				new byte[] { ClientMessageTable
						.getValue(ClientMessageTable.configure) },
				id);
	  
		return message;
	}
	
	/**
	 * encode loadlevel message
	 * allow 0 or 1 input argument
	 * @param level
	 * @return
	 */
	public static byte[] loadLevel(byte... level) {
		byte[] message = {
				ClientMessageTable.getValue(ClientMessageTable.loadLevel),
				((level.length == 0) ? 0 : level[0]) };
		return message;
	}

	/**
	 * encode restart message
	 * @return
	 */
	public static byte[] restart() {
		byte[] message = { ClientMessageTable
				.getValue(ClientMessageTable.restartLevel) };
		return message;
	}

	/**
	 * encode cshoot message (safe mode)
	 * @param fx
	 * @param fy
	 * @param dx
	 * @param dy
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static byte[] cshoot(byte[] fx, byte[] fy, byte[] dx, byte[] dy,
			byte[] t1, byte[] t2) {
		byte[] message = new byte[1 + fx.length + fy.length + dx.length
				+ dy.length + t1.length + t2.length];
		message = mergeArray(
				new byte[] { ClientMessageTable
						.getValue(ClientMessageTable.cshoot) },
				fx, fy, dx, dy, t1, t2);
		return message;

	}

	/**
	 * encode pshoot message (safe mode)
	 * @param fx
	 * @param fy
	 * @param dx
	 * @param dy
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static byte[] pshoot(byte[] fx, byte[] fy, byte[] dx, byte[] dy,
			byte[] t1, byte[] t2) {
		byte[] message = new byte[1 + fx.length + fy.length + dx.length
				+ dy.length + t1.length + t2.length];
		message = mergeArray(
				new byte[] { ClientMessageTable
						.getValue(ClientMessageTable.pshoot) },
				fx, fy, dx, dy, t1, t2);
		return message;
	}
	
	/**
	 * encode pshoot message in fast mode
	 * @param fx
	 * @param fy
	 * @param dx
	 * @param dy
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static byte[] pFastshoot(byte[] fx, byte[] fy, byte[] dx, byte[] dy,
			byte[] t1, byte[] t2) {
		byte[] message = new byte[1 + fx.length + fy.length + dx.length
				+ dy.length + t1.length + t2.length];
		message = mergeArray(
				new byte[] { ClientMessageTable
						.getValue(ClientMessageTable.pFastshoot) },
				fx, fy, dx, dy, t1, t2);
		return message;
	}
	
	/**
	 * encode cshoot message in fast mode
	 * @param fx
	 * @param fy
	 * @param dx
	 * @param dy
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static byte[] cFastshoot(byte[] fx, byte[] fy, byte[] dx, byte[] dy,
			byte[] t1, byte[] t2) {
		byte[] message = new byte[1 + fx.length + fy.length + dx.length
				+ dy.length + t1.length + t2.length];
		message = mergeArray(
				new byte[] { ClientMessageTable
						.getValue(ClientMessageTable.cFastshoot) },
				fx, fy, dx, dy, t1, t2);
		return message;

	}
	
	/**
	 * encode fully zoom out message 
	 * @return
	 */
	public static byte[] fullyZoomOut() {
		byte[] message = { ClientMessageTable
				.getValue(ClientMessageTable.fullyZoomOut) };
		return message;
	}
	
	/**
	 * 
	 * @return
	 */
	public static byte[] fullyZoomIn() {
		byte[] message = { ClientMessageTable.getValue(ClientMessageTable.fullyZoomIn) };
		return message;
	}
	
	/**
	 * 
	 * @return
	 */
	public static byte[] clickInCenter()
	{
		byte[] message = { ClientMessageTable
				.getValue(ClientMessageTable.clickInCentre) };
		return message;
	}
	
	/**
	 * encode getState message
	 * @return
	 */
	public static byte[] getState() {
		byte[] message = { ClientMessageTable
				.getValue(ClientMessageTable.getState) };
		return message;
	}
	/**
	 * encode get best scores message 
	 * @return
	 */
	public static byte[] getBestScores() 
	{
		byte[] message = {ClientMessageTable.getValue(ClientMessageTable.getBestScores)};
		return message;
	} 
	
	/**
	 * get my score message
	 * @return
	 */
	public static byte[] getMyScore()
	{
		byte[] message = {ClientMessageTable.getValue(ClientMessageTable.getMyScore)};
		return message;
	}
	
	/**
	 * merge byte arrays into one array
	 * @param arrays
	 * @return
	 */
	public static byte[] mergeArray(final byte[]... arrays) {
		int size = 0;
		for (byte[] a : arrays) {
			size += a.length;
		}
		byte[] res = new byte[size];

		int destPos = 0;
		for (int i = 0; i < arrays.length; i++) {
			if (i > 0)
				destPos += arrays[i - 1].length;
			int length = arrays[i].length;
			System.arraycopy(arrays[i], 0, res, destPos, length);
		}
		return res;
	}
}
