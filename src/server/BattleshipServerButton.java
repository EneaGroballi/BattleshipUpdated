package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import static server.BattleshipServer.sock;

public class BattleshipServerButton extends JButton{
	//server Button
        public static String CoordS = "Connected";
	private int y,x;
	private BattleshipServer prog;
	boolean occupy;

    
	public boolean isOccupy(){
		return occupy;
	}
        
        public String getCoordS(){
            
            return CoordS;
        }
        
	public BattleshipServerButton(int y, int x, BattleshipServer prog){
		this.y = y;
		this.x = x;
		this.occupy = false;
		this.prog = prog;
		BattleshipServerActionListener bsal = new BattleshipServerActionListener(y,x,prog);
		this.addActionListener(bsal);
	}
	@Override
	public String toString(){
		String coord =String.valueOf((char)(x+65));
		coord+=String.valueOf(y+1);
		return coord;
	}
	private class BattleshipServerActionListener implements ActionListener{
		private int y,x;
		private BattleshipServer prog;
		public BattleshipServerActionListener(int y, int x, BattleshipServer prog){
			this.y=y;
			this.x=x;
			this.prog = prog;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//print out coordinate
			String coord =String.valueOf((char)(x+65));
			coord+=String.valueOf(y+1);
			System.out.printf("Server: %s\n",coord);
                        CoordS = coord;
                        
                        
                    try {
                        DataInputStream reader = new DataInputStream(sock.getInputStream());
                        DataOutputStream writer = new DataOutputStream(sock.getOutputStream());
		
                        writer.writeUTF(CoordS);
                        String clientCoord = reader.readUTF();
                        System.out.println("Sent By Client & Read by Server: "+ clientCoord);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(BattleshipServerButton.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
			
			
		}
		
	}
}
