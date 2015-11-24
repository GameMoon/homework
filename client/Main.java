package client;

public class Main {
	public static TCPClient TC;
	public static void main(String args[]){
		TC= new TCPClient("192.168.43.2",7658);
		authentikation a= new authentikation(TC);
        
	}
}
