package client;

public class Main {
	
	public static void main(String args[]){
		//App a= new App(TC);
	
		TCPClient TC= new TCPClient("80.99.226.156",7658);
		authentikation a= new authentikation(TC);

	}
}
