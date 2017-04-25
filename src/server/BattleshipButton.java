package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class BattleshipButton extends JButton { 
	//Client button
	private int y,x;
	private BattleshipServer prog;
	boolean occupy;
	
	public boolean isOccupy(){
		return occupy;
	}
	public BattleshipButton(int y, int x, BattleshipServer prog){
		this.y = y;
		this.x = x;
		this.occupy = false;
		this.prog = prog;
		BattleshipButtonActionListener bbal = new BattleshipButtonActionListener(y,x,prog);
		this.addActionListener(bbal);
	}
	@Override
	public String toString(){
		return prog.convertCoord(y, x);
	}
	private class BattleshipButtonActionListener implements ActionListener{
		private int y,x;
		private BattleshipServer prog;
		public BattleshipButtonActionListener(int y, int x, BattleshipServer prog){
			this.y=y;
			this.x=x;
			this.prog = prog;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			BattleshipButton source = (BattleshipButton)e.getSource();
			if (!source.isOccupy()){
				//print out coordinate
				String coord =String.valueOf((char)(x+65));
				coord+=String.valueOf(y+1);
				System.out.printf("Server select: %s\n",coord);				
				prog.displayAvailCoord(y, x);
			} else {
				JOptionPane.showMessageDialog(null, "Can't place your ship here!");
			}
		}		
	}
	
}
