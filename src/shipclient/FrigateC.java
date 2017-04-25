package shipclient;

import client.Battleship;
import server.BattleshipServer;

public class FrigateC extends SHIPC {
	public FrigateC(Battleship battleship){
		isDeployed = false;
		length = 3;
		this.prog = battleship;
		shipType = SHIP_TYPE.FRIGATE;
	}
	
	
}
