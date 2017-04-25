package shipclient;

import client.Battleship;
import server.BattleshipServer;

public class CarrierC extends SHIPC{
	
	public CarrierC(Battleship battleship){
		isDeployed = false;
		length = 5;
		this.prog = battleship;
		shipType = SHIP_TYPE.CARRIER;
	}

	

	
	
}
