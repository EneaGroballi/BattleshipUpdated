package shipclient;

import client.Battleship;
import server.BattleshipServer;

public class MainShipC extends SHIPC {
	
	public MainShipC(Battleship battleship){
		isDeployed = false;
		length = 4; 
		this.prog = battleship;
		shipType = SHIP_TYPE.BATTLESHIP;
	}
	
}
