package client;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Font;
import java.rmi.activation.ActivateFailedException;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.html.HTML;

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
					String format=app.chatarea.getText()+command[2];
					//format.setFont(new Font(null,Font.ITALIC| Font.BOLD, 12));
					app.chatarea.setText((app.chatarea.getText()+command[2])+":"+command[3].replace("_", " ")+"\n");
				}
				else if(command[1].equals("info")){
					app.money=Integer.parseInt(command[3]);  // own money
					app.name=command[2];
					app.acounttable.setText(command[3]);
				}
				else if(command[1].equals("playerinfo")){
					app.playersnum=(command.length-2)/2;
					app.players.clear();
					for(int i=2;i<command.length-2;i=i+4){
						int[] playerint= new int[3];
						if(command[i].equals(app.name)) app.acounttable.setText(command[i+1]);
						playerint[0]=Integer.parseInt(command[i+1]); // money
						playerint[1]=Integer.parseInt(command[i+2]); // kartya1 
						//System.out.println(playerint[1]);
						playerint[2]=Integer.parseInt(command[i+3]); // kartya2
						//System.out.println(playerint[2]);
						app.players.put(command[i], playerint);      // playername
					}
					app.game.repaint();
				}
				else if(command[1].equals("waitingforyou")){
					app.ballowed();
					java.awt.Toolkit.getDefaultToolkit().beep();

				}
				else if(command[1].equals("pot")){
					app.main.setText(command[2]);
				}
				else if (command[1].equals("flop")){
					for(int i=2;i<5;i++){
						app.game.flop[i-2]=Integer.parseInt(command[i]);
					}
					app.game.repaint();
				}
				else if (command[1].equals("turn")){
					app.game.turn=Integer.parseInt(command[2]);
					app.game.repaint();
				}
				else if (command[1].equals("river")){
					app.game.river=Integer.parseInt(command[2]);
					app.game.repaint();
				}
				else if (command[1].equals("winner")){
					app.bnotallowed();
					JOptionPane.showMessageDialog(null,"Congratulations to: "+command[2]+"\n"+"Prize:"+command[3],"End of game",JOptionPane.OK_OPTION,app.game.icon);
					app.ready.setEnabled(true);
					app.game.repaint(); 
				}
				else if (command[1].equals("dealer")){
					app.game.dealerId=Integer.parseInt(command[2]);
					app.game.repaint();
				}
				else if (command[1].equals("active")){
					app.game.actualId=Integer.parseInt(command[2]);
					app.game.repaint();
				}
			}

		}
	}
}
