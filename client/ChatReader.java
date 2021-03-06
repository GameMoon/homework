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
	public synchronized void wakeup(){ notifyAll();}

	public synchronized void run() {
		while(app.T.isAlive()){
			String[] command = app.T.getCommand().split("-");

			if (command[0].equals("$") && command[command.length - 1].equals("$")) {
				if(command[1].equals("chat")){
					String format=app.chatarea.getText()+command[2];
					app.chatarea.setFont(new Font(null,Font.ITALIC| Font.BOLD, 12));
					app.chatarea.setText((app.chatarea.getText()+"["+command[2])+"]"+":  "+command[3].replace("_", " ")+"\n");
					//app.chatarea.setFont(new Font(null,Font.ITALIC, 12));
					//app.chatarea.setText((app.chatarea.getText()+command[3].replace("_", " ")+"\n"));
				}
				else if(command[1].equals("info")){
					app.money=Integer.parseInt(command[3]);  // own money
					app.name=command[2];
					app.acounttable.setText(command[3]);
				}
				else if(command[1].equals("playerinfo")){
					//System.out.println("valami");
					app.playersnum=(command.length-2)/2;
					app.players.clear();
					for(int i=2;i<command.length-2;i=i+4){
						int[] playerint= new int[3];
						if(command[i].equals(app.name)) app.acounttable.setText(command[i+1]);
						playerint[0]=Integer.parseInt(command[i+1]); // money
						playerint[1]=Integer.parseInt(command[i+2]); // kartya1 
						playerint[2]=Integer.parseInt(command[i+3]); // kartya2
						app.players.put(command[i], playerint);      // playername
					}
					app.game.repaint();
				}
				else if(command[1].equals("waitingforyou")){
					app.ballowed();
					if(app.raisecount==1 || app.players.get(app.name)[0]==0){   // elvileg null�n�l nem lehet emelni
						app.raise.setEnabled(false);
					}
					java.awt.Toolkit.getDefaultToolkit().beep();

				}
				else if(command[1].equals("pot")){
					app.main.setText(command[2]);
				}
				else if (command[1].equals("flop")){
					for(int i=2;i<5;i++){
						app.game.flop[i-2]=Integer.parseInt(command[i]);
						app.raisecount=0;
					}
					app.game.repaint();
				}
				else if (command[1].equals("turn")){
					app.game.turn=Integer.parseInt(command[2]);
					app.game.repaint();
					app.raisecount=0;
				}
				else if (command[1].equals("river")){
					app.game.river=Integer.parseInt(command[2]);
					app.game.repaint();
					app.raisecount=0;
				}
				else if (command[1].equals("winner")){
					app.bnotallowed();
					JOptionPane.showMessageDialog(null,"Congratulations to: "+command[2]+"\n"+"Prize:"+command[3],"End of game",JOptionPane.OK_OPTION,app.game.icon);
					app.ready.setEnabled(true);
					app.game.repaint(); 
					app.raisecount=0;
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
			try {
				if(app.T.getAllCommand().size() == 0) wait();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
