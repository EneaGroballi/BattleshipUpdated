package ship;

import client.Battleship;
import server.BattleshipServer;

public class Minesweep extends SHIP{
	
	public Minesweep(BattleshipServer prog){
		isDeployed = false;
		length = 2;
		this.prog = prog;
		shipType = SHIP_TYPE.MINESWEEPER;
	}
	
	
}
