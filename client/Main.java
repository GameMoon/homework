package client;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Main {
	
	public static void main(String args[]){
		TCPClient TC= new TCPClient("152.66.155.125",7658);
		authentikation a= new authentikation(TC);

	}
}
