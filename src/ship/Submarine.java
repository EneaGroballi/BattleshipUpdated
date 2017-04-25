package ship;

import client.Battleship;
import server.BattleshipServer;

public class Submarine extends SHIP{
	
	public Submarine(BattleshipServer prog){
		length = 3;
		this.prog = prog;
		isDeployed = false;
		shipType = SHIP_TYPE.SUBMARINE;
	}

}
