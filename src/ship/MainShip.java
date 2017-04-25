package ship;

import client.Battleship;
import server.BattleshipServer;

public class MainShip extends SHIP {
	
	public MainShip(BattleshipServer battleshipServer){
		isDeployed = false;
		length = 4; 
		this.prog = battleshipServer;
		shipType = SHIP_TYPE.BATTLESHIP;
	}
	
}
