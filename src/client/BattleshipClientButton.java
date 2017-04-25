package client;

import static client.Battleship.sock;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;

public class BattleshipClientButton extends JButton{
	//server Button
        public static String CoordC = "Connected";
	private int y,x;
	private Battleship prog;
	boolean occupy;

    
    
	public boolean isOccupy(){
		return occupy;
	}
        
        public String getCoordC(){
            
            return CoordC;
        }
	public BattleshipClientButton(int y, int x, Battleship prog){
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
		private Battleship prog;
		public BattleshipServerActionListener(int y, int x, Battleship prog){
			this.y=y;
			this.x=x;
			this.prog = prog;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//print out coordinate
			String coord =String.valueOf((char)(x+65));
			coord+=String.valueOf(y+1);
			System.out.printf("Client: %s\n",coord);
			
                        CoordC = coord;
                        
                    try {
                        DataOutputStream writer = new DataOutputStream(sock.getOutputStream());
                        DataInputStream reader = new DataInputStream(sock.getInputStream());
                        writer.writeUTF(CoordC);
                        String serverCoord = reader.readUTF();
                        System.out.println("Sent By Server & Read by Client: "+ serverCoord);
                
                        
                    } catch (IOException ex) {
                        Logger.getLogger(BattleshipClientButton.class.getName()).log(Level.SEVERE, null, ex);
                    }
		
		}
		
	}
}
