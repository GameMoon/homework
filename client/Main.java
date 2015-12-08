package client;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Main {
	static authentikation a;
	public static void main(String args[]){
		TCPClient TC= new TCPClient(args[0],7658);    // IP-Adresse und Socket
		a= new authentikation(TC);

	}
}
