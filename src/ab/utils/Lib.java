package ab.utils;

/**
 * Library for the AI Agents
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @email {ruofei, gaozebao, xuzh} @ cs.umd.edu
 * @organization Unversity of Maryland, College Park
 * @date 04/04/2015
 * @advisor Prof. Dana Nau and 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz, Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class Lib {
	
	public static boolean LOCAL_TEST = true; 
	public static long initTime = System.nanoTime(); 
	public static String args = ""; 
	public static boolean repeatLevels = false; 
	public static boolean IRPE = false; 
	public static int tryTimes = 0; 

	private static void printFlag(char flag) {
	}
	
	/**
	 * Enable all the debug flags in <i>flagsString</i>.
	 * 
	 * @param flagsString
	 *            the flags to enable.
	 */
	public static void enableDebugFlags(String flagsString) {
		if (debugFlags == null)
			debugFlags = new boolean[0x80];

		char[] newFlags = flagsString.toCharArray();
		for (int i = 0; i < newFlags.length; i++) {
			char c = newFlags[i];
			if (c >= 0 && c < 0x80)
				debugFlags[(int) c] = true;
		}
	}
	/**
	 * Tests if <i>flag</i> was enabled on the command line.
	 * 
	 * @param flag
	 *            the debug flag to test.
	 * 
	 * @return <tt>true</tt> if this flag was enabled on the command line.
	 */
	public static boolean test(char flag) {
		if (debugFlags == null)
			return false;
		else if (debugFlags[(int) '+'])
			return true;
		else if (flag >= 0 && flag < 0x80 && debugFlags[(int) flag])
			return true;
		else
			return false;
	}
	
	public static void debug(char flag, String message) {
		if (test(flag)) {
			System.out.print((int)((System.nanoTime() - initTime) / 1000000) + "\t");
			if (flag == flagSystem) System.out.print("[System]\t"); 
			if (flag == flagVision) System.out.print("[Vision]\t");
			if (flag == flagPlanner) System.out.print("[Planner]\t");
			if (flag == flagActor) System.out.print("[Actor]\t\t");
			if (flag == flagResult) System.out.print("[Result]\t"); 
			if (flag == flagError) System.out.print("[Error]\t\t"); 
			System.out.println(message);
		}
	}
	
	public static void debugsp(char flag) {
		if (test(flag))
			System.out.println("==========================================================");
	}
	
	public static void debugsq(char flag) {
		if (test(flag))
			System.out.println("----------------------------------------------------------");
	}
	public static void assertTrue(boolean expression, String message) {
		if (!expression)
			throw new AssertionFailureError(message);
	}
	
	private static boolean debugFlags[];
	
	public static char flagSystem = 's'; 
	public static char flagVision = 'v'; 
	public static char flagPlanner = 'p';
	public static char flagActor = 'a'; 
	public static char flagResult = 'r'; 
	public static char flagError = 'e'; 
	
}
