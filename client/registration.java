package client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;


public class registration extends JFrame {
	public registration(){
		
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
				//JOptionPane.showMessageDialog(null, "Ooopss...", "i", JOptionPane.INFORMATION_MESSAGE);
			}
			
		});
		setSize(300,300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
}
}
