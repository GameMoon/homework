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

import javax.swing.*;

public class authentikation extends JFrame {
	private TCPClient T;
	String sendpassword;
	String sendusername;
	public authentikation(TCPClient TA){
		T=TA;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/3-this.getSize().width/2,(dim.height/2-this.getSize().height/2));
		setTitle("Login");
		JPanel top= new JPanel();
		JPanel mid= new JPanel();
		JPanel down= new JPanel();
		JLabel user= new JLabel("Username:");
		JLabel pass= new JLabel("Password:");
		JLabel topLabel= new JLabel("Please Login:");
		topLabel.setFont(new Font(null, Font.PLAIN, 18));
		JTextField name= new JTextField();
		name.setColumns(15);
		JTextField word= new JTextField();
		word.setColumns(15);
		JButton login=new JButton("Login");
		login.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if((!name.getText().equals("")) && (!word.getText().equals(""))) {
					//name.setText("");
					//world.setText("");
					try {
						//MD.Mdhash(name.getText());
						sendpassword=MD.Mdhash(word.getText());
						sendusername=name.getText();
						T.sendCommand("$-verify-"+sendusername+"-"+sendpassword+"-$");

					} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					while(T.getAllCommand().size()==0);
					//System.out.println(T.getAllCommand().size());
					String command=T.getCommand();
					System.out.println(command);
					if(command.equals("$-ok-$")){
						try {
							T.sendCommand("$-start-"+sendusername+"-"+sendpassword+"-$");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						App poker= new App();
					}
					else{
						JOptionPane.showMessageDialog(null,"PASSWORD OR USERNAME INCORRECT", "Ooopss...",  JOptionPane.INFORMATION_MESSAGE);
					}
					T.getAllCommand().clear();
				}
				else
				{
					JOptionPane.showMessageDialog(null,"PASSWORD OR USERNAME INCORRECT(1)", "Ooopss...",  JOptionPane.INFORMATION_MESSAGE);
					//name.setText("");
					//word.setText("");
				}
			}

		});
		JButton reg = new JButton("Registration");
		reg.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				registration rg=new registration(); 

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