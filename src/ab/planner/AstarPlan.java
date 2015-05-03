package ab.planner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import ab.utils.Lib;

/**
 * An better plan for destorying all the pigs
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @email {ruofei, gaozebao, xuzh} @ cs.umd.edu
 * @organization Unversity of Maryland, College Park
 * @date 04/04/2015
 * @advisor Prof. Dana Nau and Dr. Vikas Shivashankar 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz, Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class AstarPlan {
	HashSet<String> visited; 
	String currentPlan = ""; 
	
	/**
	 * Get DFFS Plan
	 * @param size
	 */
	public AstarPlan() {
		visited = new HashSet<String>(); 
	}
	
	/**
	 * Get an unvisited plan to destroy n pigs
	 * @param n
	 * @return
	 */
	public boolean getPlan(int n, boolean IRPE) {
		int total = 1; 
		int iteration = 0; 
		for (int i = 1; i <= n; ++i) total *= i; 
		Random r = new Random(); 
		String s = ""; 
		if (IRPE) s = this.currentPlan; 
		do {
			s = getPermutation(n, r.nextInt(total));
			if (++iteration > (1 << 10)) return false;  
		} while (visited.contains(s)); 
		visited.add(s); 
		currentPlan = s; 
		return true; 
	}
	
	/**
	 * generate kth plan out of permutation of n numbers
	 * @param n
	 * @param k
	 * @return
	 */
	static public String getPermutation(int n, int k) {
		boolean[] output = new boolean[n];
		StringBuilder buf = new StringBuilder("");
 
		int[] res = new int[n];
		res[0] = 1;
 
		for (int i = 1; i < n; i++)
			res[i] = res[i - 1] * i;
 
		for (int i = n - 1; i >= 0; i--) {
			int s = 1;
 
			while (k > res[i]) {
				s++;
				k = k - res[i];
			}
 
			for (int j = 0; j < n; j++) {
				if (j + 1 <= s && output[j]) {
					s++;
				}
			}
 
			output[s - 1] = true;
			buf.append(Integer.toString(s));
		}
 
		return buf.toString();
	}
	
	/**
	 * Nondeterministically choose a pig from current plan
	 */
	public int nondeterministicallyChoosePig(int step) {
		if (step < currentPlan.length()) return Character.getNumericValue( currentPlan.charAt(step) ); else
			return 0; 
	}
	
	/**
	 * Agent test
	 * @param args
	 */
	public static void main(String args[]) {
		DFFSPlan plan = new DFFSPlan();

	}
}
