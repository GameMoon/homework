package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.*;

public class authentikation extends JFrame {
	public authentikation(){
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
						MD.Mdhash(word.getText());
					} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null,"nem tudok angolul", "Ooopss...",  JOptionPane.INFORMATION_MESSAGE);
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