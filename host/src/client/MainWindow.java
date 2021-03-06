package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Component;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainWindow {

	private JFrame frame;
	private JTextField textUser= new JTextField();
	private JTextField textServerIp;
	private JPasswordField textPassword;
	private JButton btnConnect;
	private JLabel textConnection;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 345, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//textUser = new JTextField();
		textUser.setColumns(10);

		JLabel lblUser = new JLabel("User");
		lblUser.setHorizontalAlignment(SwingConstants.RIGHT);

		textServerIp = new JTextField();
		textServerIp.setColumns(10);
		textServerIp.setText("46.149.80.62");

		JLabel lblIpserver = new JLabel("IpServer");
		lblIpserver.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel lblPassword = new JLabel("password");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);

		textPassword = new JPasswordField();

		//hide password input
		textPassword.setVisible(false);
		lblPassword.setVisible(false);
		//hide password input


		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onConnectClicked();
			}
		});

		textConnection = new JLabel("Not connected");
		textConnection.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
										.addContainerGap()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(lblUser)
												.addComponent(lblPassword)
												.addComponent(lblIpserver))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
												.addComponent(textServerIp)
												.addComponent(textPassword)
												.addComponent(textUser, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)))
								.addGroup(groupLayout.createSequentialGroup()
										.addGap(128)
										.addComponent(btnConnect))
								.addGroup(groupLayout.createSequentialGroup()
										.addContainerGap()
										.addComponent(textConnection, GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)))
						.addContainerGap())
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblUser)
								.addComponent(textUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPassword)
								.addComponent(textPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(textServerIp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblIpserver))
						.addGap(11)
						.addComponent(btnConnect)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(textConnection)
						.addContainerGap(34, Short.MAX_VALUE))
				);
		groupLayout.linkSize(SwingConstants.VERTICAL, new Component[] {textUser, lblUser, textServerIp, lblIpserver, lblPassword, textPassword});
		frame.getContentPane().setLayout(groupLayout);
	}

	private void onConnectClicked(){
		JettyClient client=new JettyClient();
		if(client.connectTo(getTextServer().getText(), getTextUser().getText(), null)==0){
			getTextConnection().setText("Connect success");
			getBtnConnect().setEnabled(false);
		}else getTextConnection().setText("Connection error");

	}
	public JButton getBtnConnect() {
		return btnConnect;
	}
	public JTextField getTextServer() {
		return textServerIp;
	}
	public JPasswordField getTextPassword() {
		return textPassword;
	}
	public JTextField getTextUser() {
		return textUser;
	}
	public JLabel getTextConnection() {
		return textConnection;
	}
}
