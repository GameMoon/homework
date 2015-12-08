package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class KeyChatListener implements KeyListener {
App app;
	public KeyChatListener(App a) {
		app=a;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
			try {
				app.T.sendCommand("$-chat-"+app.chatfield.getText().replace(" ", "_")+"-$");
				app.chatfield.setText("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
