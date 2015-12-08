package client;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Main {
	
	public static void main(String args[]){
		TCPClient TC= new TCPClient(args[0],7658);    // IP-Adresse und Socket
		authentikation a= new authentikation(TC);

	}
}
