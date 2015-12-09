package client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;

import javax.swing.*;


public class registration extends JFrame {
	TCPClient T;
	String sendregusername;
	String sendregpassword;
	public registration(TCPClient t){
		T=t;
		JButton reg = new JButton("Registration");
		JButton back = new JButton("Back");
		JTextField name = new JTextField();
		name.setColumns(15);
		JTextField word= new JTextField();
		word.setColumns(15);
		JLabel titel =new JLabel("Registration");
		titel.setFont(new Font(null, Font.PLAIN, 18));
		JLabel p= new JLabel("Password:");
		JLabel u= new JLabel("Username:");
		JLabel choose= new JLabel("Choose a Username and Password");
		JPanel top= new JPanel();
		JPanel down= new JPanel();
		JPanel mid = new JPanel();
		setSize(300,300);
		setResizable(false);
		top.setSize(300,100);
		top.add(titel);
		top.add(choose,BorderLayout.SOUTH);
		mid.add(u,BorderLayout.WEST);
		mid.add(name);
		mid.add(p);
		mid.add(word);
		down.add(back);
		down.add(reg);

		add(top, BorderLayout.NORTH);
		add(mid, BorderLayout.CENTER);
		add(down , BorderLayout.SOUTH);

		String infoMessage= new String("hsguhfdg");
		back.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				authentikation.login.setEnabled(true);
				authentikation.reg.setEnabled(true);
				
			}

		});
		reg.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!name.getText().contains("'") && !(name.getText().length()>15) && !name.getText().contains("-") && !name.getText().contains("$") && (!name.getText().equals("")) && (!word.getText().equals("")) && (name.getText().matches("\\A\\p{ASCII}*\\z"))) {
					//name.setText("");
					//world.setText("");

					String command="fake";
					try {
						sendregusername=name.getText();
						sendregpassword=MD.Mdhash(word.getText());
								T.sendCommand("$-register-"+sendregusername+"-"+sendregpassword+"-$");
						//command=T.getCommand();

					} catch (NoSuchAlgorithmException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					while(command.equals("fake")){
						command=T.getCommand();
					};
					System.out.println(command);
					//System.out.println(T.getAllCommand().size());

					System.out.println(T.getAllCommand().size());
					if(command!=null && command.equals("$-ok-$")){
						dispose();
						authentikation.login.setEnabled(true);
						authentikation.reg.setEnabled(true);
					}
					else{
						JOptionPane.showMessageDialog(null,"USERNAME IS ALREADY IN USE", "Ooopss...",  JOptionPane.INFORMATION_MESSAGE);
					}

				}
				else
				{
					JOptionPane.showMessageDialog(null, "INCORRECT USRNAME OR PASSWORD\n -Maximal 15 characters\n -Only ASCII characters\n -Forbidden:  '  ","Opsss..", JOptionPane.OK_OPTION);
					//name.setText("");
					//word.setText("");
				}
			}
		});
		addWindowListener(new WindowListener(){

			

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				authentikation.login.setEnabled(true);
				authentikation.reg.setEnabled(true);
				
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		setSize(300,300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);

	}
}
