package server;

public class Main {


    public static void main(String[] args){
        System.out.println("Server started " + Constants.versionNumber);

        GameManager gameManager = new GameManager();
        CommandListener commandListener = new CommandListener(gameManager);
    }

}

