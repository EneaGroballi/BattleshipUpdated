package ship;

import client.Battleship;
import server.BattleshipServer;

public class Carrier extends SHIP{
	
	public Carrier(BattleshipServer prog){
		isDeployed = false;
		length = 5;
		this.prog = prog;
		shipType = SHIP_TYPE.CARRIER;
	}
	
}
