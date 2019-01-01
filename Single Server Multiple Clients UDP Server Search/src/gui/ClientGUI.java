package gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

public class ClientGUI extends JPanel {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Menu");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ClientGUI menu = new ClientGUI("Cade Test");
		frame.getContentPane().add(menu, BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	static final int PADDING = 40;

	private ArrayList<String> addresses = new ArrayList<String>();
    private DefaultListModel availableServers = new DefaultListModel();;
	private JList serverList = new JList(availableServers);
	private JScrollPane scroll = new JScrollPane(serverList);
	private JButton create = new JButton("Create Server");
	private JButton join = new JButton("Join Server");
	private JLabel title = new JLabel("Available Servers: ");
	private int selected;
	
	public ClientGUI(String usr) {
				
		serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		serverList.setVisibleRowCount(-1);
		
		serverList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
			      selected = serverList.getSelectedIndex();
			      System.out.println(selected);
			}
		});
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		this.add(title);
		this.add(scroll);
		this.add(create);
		this.add(join);

		// title constraints
		layout.putConstraint(SpringLayout.WEST, title, 
				PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, title, 
				PADDING, 
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, this, 
				PADDING, 
				SpringLayout.EAST, title);

		// list constraints
		layout.putConstraint(SpringLayout.WEST, scroll, 
				0, 
				SpringLayout.WEST, title);
		layout.putConstraint(SpringLayout.NORTH, scroll, 
				0, 
				SpringLayout.SOUTH, title);
		layout.putConstraint(SpringLayout.EAST, title, 
				0, 
				SpringLayout.EAST, scroll);
		layout.putConstraint(SpringLayout.SOUTH, scroll, 
				-PADDING, 
				SpringLayout.NORTH, create);

		// toSend text field constraints
		layout.putConstraint(SpringLayout.WEST, create, 
				PADDING, 
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, create, 
				-PADDING, 
				SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, create, 
				-PADDING / 2, 
				SpringLayout.HORIZONTAL_CENTER, this);

		// join button constraints
		layout.putConstraint(SpringLayout.EAST, join, 
				-PADDING, 
				SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, join, 
				-PADDING, 
				SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.WEST, join, 
				PADDING / 2, 
				SpringLayout.HORIZONTAL_CENTER, this);
	}

}
