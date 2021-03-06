package client;
import static client.BattleshipClientButton.CoordC;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ship.*;
import shipclient.CarrierC;
import shipclient.FrigateC;
import shipclient.MainShipC;
import shipclient.MinesweepC;
import shipclient.SubmarineC;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Battleship extends JFrame {
	BattleshipButton[][] ourBoard;
	BattleshipClientButton[][] theirBoard; 
	final int size = 10;
	int btnSize = 40;
	JPanel uPanel, cPanel, uShip;
	JButton connectBtn;
	JList<String> shipList;
	ArrayList<Integer> deployedShip;
	//ship type
	CarrierC carrier;
	FrigateC frigate;
	MainShipC battleship;
	MinesweepC minesweeper;
	SubmarineC submarine;
	int selectedShip = -1;
	
	private final Color[] shipColor = {Color.BLUE,Color.GRAY,Color.ORANGE,Color.yellow,Color.MAGENTA};
	
	public Battleship(){ 
		//main frame
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Battleship Client");		
		setBounds(0,0,btnSize*(size+1)*5/2,btnSize*(size+1));
		getContentPane().setLayout(null);
		//change windows
		initializeWindow();
		//add panel to frame
		add(uPanel);
		add(uShip);
		add(cPanel);
	}

	private void initializeWindow() {
		//user's panel and board
		String lblvalue = "";
		uPanel = new JPanel(); 
		setLayout(null);	
		uPanel.setLayout(new GridLayout(size+1, size+1));
		uPanel.setBounds(0,0,btnSize*(size+1),btnSize*(size+1));
		uPanel.add(new JLabel("     "));
		for (int i=1;i<=size;i++){
			lblvalue = String.format("%4s", String.valueOf(Character.toChars(i+64)));
			uPanel.add(new JLabel(lblvalue));
		}
		ourBoard= new BattleshipButton[size][size];
		for (int i=0;i<size;i++){
			lblvalue = String.format("%2d", i+1);
			uPanel.add(new JLabel(lblvalue));
			for (int j=0;j<size;j++){
				ourBoard[i][j] = new BattleshipButton(i,j,this);
				ourBoard[i][j].setPreferredSize(new Dimension(btnSize,btnSize));
				uPanel.add(ourBoard[i][j]);
			}
		}
		//ship list
		selectedShip = -1;		//index of selected ship
		uShip = new JPanel(new BorderLayout());	
		uShip.setBounds(btnSize*(size+1),0,btnSize*(size+1)/2,btnSize*(size+1));
		JLabel lbl1 = new JLabel("Ship list: ");
		lbl1.setSize(50,btnSize);
		uShip.add(lbl1,BorderLayout.NORTH);	
		//Ship choice button
		shipList = new JList(CellRenderer.db);
	    shipList.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
	    shipList.setCellRenderer(new CellRenderer());
	    
	    
	    class LL implements ListSelectionListener { 
	    	//selection listener
	    	private Battleship prog;
	    	public LL(Battleship prog){
	    		this.prog = prog;
	    	}
	    	@Override
	    	public void valueChanged (ListSelectionEvent e) {
	    		if (e.getValueIsAdjusting()==true) 
	    			return;
	    		final JList l = (JList) e.getSource();
	    		final int selected = l.getSelectedIndex();
	    		System.out.printf ("Client Selecting: %s\n",CellRenderer.db[selected]);
	    		l.setSelectedIndex(selected);
	    		prog.selectedShip = selected;	//change the index of selected ship
		     }
	    }
	    
	    final LL llsl = new LL(this);
	    shipList.addListSelectionListener(llsl);
	    shipList.setSize(80,btnSize*CellRenderer.db.length);
	    
	    deployedShip = new ArrayList<Integer>(); //list of deployed ship
	    
	    
	    uShip.add(shipList,BorderLayout.CENTER);	//add the list to the panel
	    //connect Button
	    connectBtn = new JButton("Connect");
	    connectBtn.setPreferredSize(new Dimension(50,20));
	    connectBtn.setEnabled(false);
	    connectBtn.putClientProperty("prog", this);
	    connectBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();
				Battleship prog = (Battleship) btn.getClientProperty("prog");
				//Battleship battle = new Battleship();
				//try {
				//	prog.run("Connected.");
				//} catch (Exception e1) {
					// TODO Auto-generated catch block
				//	e1.printStackTrace();
				//}
				
				
				
				
			}
		});
	    uShip.add(connectBtn,BorderLayout.SOUTH);
		
		//computer board //Server
		cPanel = new JPanel(); 
		cPanel.setLayout(new GridLayout(size+1, size+1));
		cPanel.setBounds(btnSize*(size+1)*3/2,0,btnSize*(size+1),btnSize*(size+1));
		cPanel.add(new JLabel("     "));
		for (int i=1;i<=size;i++){
			lblvalue = String.format("%4s", String.valueOf(Character.toChars(i+64)));
			cPanel.add(new JLabel(lblvalue));
		}
		theirBoard= new BattleshipClientButton[size][size];
		for (int i=0;i<size;i++){
			lblvalue = String.format("%2d", i+1);
			cPanel.add(new JLabel(lblvalue));
			for (int j=0;j<size;j++){
				theirBoard[i][j] = new BattleshipClientButton(i,j,this);
				theirBoard[i][j].putClientProperty("prog", this);
				theirBoard[i][j].putClientProperty("coord", new int[]{i,j});
				theirBoard[i][j].setPreferredSize(new Dimension(btnSize,btnSize));
				cPanel.add(theirBoard[i][j]);
			}
		}
		//ship and play	
	}
	
	public void displayAvailCoord(int y,int x){
		Point[] pos;
		if (deployedShip.contains(selectedShip)){
			JOptionPane.showMessageDialog(null,"Ship's already deployed");
			return;
		}		
		switch (selectedShip){
			case 0: //carrier; length 5
				carrier = new CarrierC(this);
				carrier.setStartCoord(y, x);
				pos = carrier.getEndCoord();
				break;
			case 1: //battleship, length 4 
				battleship = new MainShipC(this);
				battleship.setStartCoord(y, x);
				pos = battleship.getEndCoord();
				break;
			case 2: //frigate
				frigate = new FrigateC(this);
				frigate.setStartCoord(y, x);
				pos = frigate.getEndCoord();
				break;
			case 3: //submarine
				submarine = new SubmarineC(this);
				submarine.setStartCoord(y, x);
				pos = submarine.getEndCoord();
				break;
			case 4:
				minesweeper = new MinesweepC(this);
				minesweeper.setStartCoord(y, x);
				pos = minesweeper.getEndCoord();
				break;
			default://ship is not selected
				JOptionPane.showMessageDialog(null,"Please select the ship");
				return;
		}
		if (pos.length>0){	//checking if there are end coordinate available
			String[] availCoordStr = new String[pos.length+1];
			for (int i=0;i<pos.length;i++){
				availCoordStr[i] = convertCoord(pos[i].y, pos[i].x);
			} 
			availCoordStr[pos.length] = "Nevermind";
			JList<String> availCoordList = new JList<String>(availCoordStr);  //available coordinate
			availCoordList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			JOptionPane.showMessageDialog(this, availCoordList, 
					String.format("Original: [%s]",convertCoord(y, x)),
					JOptionPane.INFORMATION_MESSAGE);
			
			int selectEndPos = availCoordList.getSelectedIndex(); 
			//get End position coordinate
			
			if (selectEndPos>-1 && selectEndPos<pos.length){
				//DEPLOY SHIP HERE
				Point p = pos[selectEndPos];
				System.out.printf("Selected Option: %d; Coord: %s\n",selectEndPos,availCoordStr[selectEndPos]);
				switch (selectedShip){
				case 0:
					carrier.deploy();
					carrier.setEndCoord(p.y, p.x);
					break;
				case 1: //battleship, length 4
					battleship.deploy();
					battleship.setEndCoord(p.y, p.x);
					break;
				case 2: //frigate
					frigate.deploy();
					frigate.setEndCoord(p.y, p.x);
					break;
				case 3: //submarine
					submarine.deploy();
					submarine.setEndCoord(p.y, p.x);
					break;
				case 4:
					minesweeper.deploy();
					minesweeper.setEndCoord(p.y, p.x);
					break;				
				}		
				deployedShip.add(selectedShip);
								
				if(deployedShip.size()==5){	// Check if all 5 ships are deployed All
					JOptionPane.showMessageDialog(this,"All ships are deployed.\nReady to Connect?");
					connectBtn.setEnabled(true);
					this.shipList.setEnabled(false);
				}				
				return;
			}
		} else JOptionPane.showMessageDialog(null,"No available coordinate");
	}
	public boolean isFree(int startY, int startX, int endY, int endX){
		int[] range = new int[4];
		if (startY<=endY){ 
			range[0] = startY;
			range[1] = endY;				
		} else {
			range[1] = startY;
			range[0] = endY;	
		}
		if (startX<=endX){ 
			range[2] = startX;
			range[3] = endX;				
		} else {
			range[3] = startX;
			range[2] = endX;	
		}
		for (int i=range[0];i<=range[1];i++)
			for (int j=range[2];j<=range[3];j++){
				if (ourBoard[i][j].isOccupy())
					return false;
			}
		return true;
	}
	public void placeShip(int startY, int startX, int endY, int endX, int type){
		//display ship onto the Button Board
		int[] range = new int[4];
		if (startY<=endY){ 
			range[0] = startY;
			range[1] = endY;				
		} else {
			range[1] = startY;
			range[0] = endY;	
		}
		if (startX<=endX){ 
			range[2] = startX;
			range[3] = endX;				
		} else {
			range[3] = startX;
			range[2] = endX;	
		}
		System.out.printf("%d %d %d %d \n",range[0],range[1],range[2],range[3]);
		for (int i=range[0];i<=range[1];i++)
			for (int j=range[2];j<=range[3];j++){
				ourBoard[i][j].setBackground(shipColor[type]);
				ourBoard[i][j].occupy = true;
			}
		
		
	}
	public String convertCoord(int y,int x){
		StringBuilder coord = new StringBuilder();
		coord.append(String.valueOf((char)(x+65)));
		coord.append(String.valueOf(y+1));
		return coord.toString();
	}
	public static void main(String[] args){
            
               
                                
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new Battleship().setVisible(true);
                            
			}
		});
                
                 Battleship battle = new Battleship();
                          
                    try {
                                battle.run();
                            } catch (Exception ex) {
                                Logger.getLogger(Battleship.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    
	}
	
	
	static Socket sock;
	void run() throws Exception {
		sock = new Socket("localhost",9090);
		DataOutputStream writer = new DataOutputStream(sock.getOutputStream());
		DataInputStream reader = new DataInputStream(sock.getInputStream());
                
                
                
                writer.writeUTF(CoordC);
                String serverCoord = reader.readUTF();
		System.out.println("Sent By Server & Read by Client: "+ serverCoord);
                
                
		
	
		
		
		
		
	}
	

	
}

