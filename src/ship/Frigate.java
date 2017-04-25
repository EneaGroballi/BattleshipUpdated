package ship;

import client.Battleship;
import server.BattleshipServer;

public class Frigate extends SHIP {
	public Frigate(BattleshipServer prog){
		isDeployed = false;
		length = 3;
		this.prog = prog;
		shipType = SHIP_TYPE.FRIGATE;
	}
	
	
}
