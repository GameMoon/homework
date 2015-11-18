package client;

public class Main {
	public static TCPClient TC;
	public static void main(String args[]){
		TC= new TCPClient("92.249.223.118",7658);
		authentikation a= new authentikation(TC);
        
	}
}
