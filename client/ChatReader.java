package client;

import javax.swing.JTextArea;

public class ChatReader extends Thread {
	App app;
	public ChatReader(App a) {
		app=a;
	}
	public void run() {
		while(app.T.isAlive()){
			String[] command = app.T.getCommand().split("-");
			if (command[0].equals("$") && command[command.length - 1].equals("$")) {
				if(command[1].equals("chat")){
					app.chatarea.setText(app.chatarea.getText()+command[2]+":"+command[3].replace("_", " ")+"\n");
				}
			}
		}
	}
}
