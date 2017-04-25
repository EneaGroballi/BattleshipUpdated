package shipclient;

import client.Battleship;
import server.BattleshipServer;

public class SubmarineC extends SHIPC{
	
	public SubmarineC(Battleship battleship){
		length = 3;
		this.prog = battleship;
		isDeployed = false;
		shipType = SHIP_TYPE.SUBMARINE;
	}

}
