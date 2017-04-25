package shipclient;

import client.Battleship;
import server.BattleshipServer;

public class MinesweepC extends SHIPC{
	
	public MinesweepC(Battleship battleship){
		isDeployed = false;
		length = 2;
		this.prog = battleship;
		shipType = SHIP_TYPE.MINESWEEPER;
	}
	
	
}
