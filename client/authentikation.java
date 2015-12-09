package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;

import javax.swing.*;

public class authentikation extends JFrame {
	private TCPClient T;
	String sendpassword;
	String sendusername;
	static JButton login;
	static JButton reg;
	int money;
	JPanel top;
	JPanel mid;
	JPanel down;
	JLabel user;
	JLabel pass;
	JLabel topLabel;
	public authentikation(TCPClient TA){
		T=TA;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/3-this.getSize().width/2,(dim.height/2-this.getSize().height/2));
		setTitle("Login");
		top= new JPanel();
		mid= new JPanel();
		down= new JPanel();
		user= new JLabel("Username:");
		pass= new JLabel("Password:");
		topLabel= new JLabel("Please Login:");
		topLabel.setFont(new Font(null, Font.PLAIN, 18));
		JTextField name= new JTextField();
		name.setColumns(15);
		JPasswordField word= new JPasswordField();
		word.setColumns(15);
		login=new JButton("Login");
		login.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if((!name.getText().equals("")) && (!word.getText().equals(""))) {
					String command="fake";
					try {
						sendpassword=MD.Mdhash(word.getText());
						sendusername=Normalizer.normalize(name.getText(),Normalizer.Form.NFD);
						if(T.isAlive()){
							T.sendCommand("$-verify-"+sendusername+"-"+sendpassword+"-$");
						}
						else {
							System.out.println("Nincs Socket");
							return;
						}

					} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}



					while(command.equals("fake")){
						command=T.getCommand();
					};

					if(command!=null && command.equals("$-ok-$")){
						try {
							T.sendCommand("$-start-"+sendusername+"-"+sendpassword+"-$");
							App poker= new App(T,sendusername);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}					
						dispose();
					}
					else{
						JOptionPane.showMessageDialog(null,"PASSWORD OR USERNAME INCORRECT", "Ooopss...",  JOptionPane.INFORMATION_MESSAGE);
					}

				}
				else
				{
					JOptionPane.showMessageDialog(null,"PASSWORD OR USERNAME INCORRECT(1)", "Ooopss...",  JOptionPane.INFORMATION_MESSAGE);
				}
			}

		});
		reg = new JButton("Registration");
		reg.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				registration rg=new registration(T); 
				login.setEnabled(false);
				reg.setEnabled(false);
				word.setText("");
				name.setText("");
			}

		});
		top.setSize(300,50);
		top.add(topLabel);
		mid.add(user,BorderLayout.WEST);
		mid.add(name);
		mid.add(pass);
		mid.add(word);

		down.add(login);
		down.add(reg);

		add(top, BorderLayout.NORTH);
		add(mid, BorderLayout.CENTER);
		add(down , BorderLayout.SOUTH);
		setSize(300,200);
		setResizable(false);
		setVisible(true);
		//Jt.pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}


}