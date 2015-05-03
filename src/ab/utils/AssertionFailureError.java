package ab.utils;

/**
 * Thrown when an assertion fails.
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public class AssertionFailureError extends Error {
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;

	AssertionFailureError() {
		super();
	}

	AssertionFailureError(String message) {
		super(message);
	}
}