/*****************************************************************************
 ** ANGRYBIRDS AI AGENT FRAMEWORK
 ** Copyright (c) 2014, XiaoYu (Gary) Ge, Stephen Gould, Jochen Renz
 **  Sahan Abeyasinghe,Jim Keys,  Andrew Wang, Peng Zhang
 ** All rights reserved.
**This work is licensed under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
**To view a copy of this license, visit http://www.gnu.org/licenses/
 *****************************************************************************/
package ab.vision;

/**
 * Types
 * 
 * @author Ruofei Du, Zebao Gao, Zheng Xu
 * @advisor Prof. Dana Nau and 
 * @framework forked from IJCAI AIBirds.org by XiaoYu (Gary) Ge, Jochen Renz,Stephen Gould
 * @license GNU Affero General Public License http://www.gnu.org/licenses/
 */
public enum ABType {
	
	Ground(1),
	Hill(2),
	Sling(3),
	RedBird(4), 
	YellowBird(5), 
	BlueBird(6), 
	BlackBird(7), 
	WhiteBird(8), 
	Pig(9),
	Ice(10), 
	Wood(11), 
	Stone(12), 
	TNT(18),
	Unknown(0);
	
	public int id;
	
	private ABType(int id)
	{
		this.id = id;
	}
	
	/**
     * Get the size of each bird
     * @return bird radius
	 */
	public int getBirdRadius()
	{
		switch (this) {
			case RedBird: return 6; 
			case YellowBird: return 7; 
			case WhiteBird: return 13; 
			case BlackBird: return 8; 
			case BlueBird: return 4; 
			default: return 5; 
		}
	}
}
