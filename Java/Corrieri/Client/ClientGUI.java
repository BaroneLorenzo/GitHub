import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.SwingConstants;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class ClientGUI {

	private JFrame frame;
	private JFrame frameMain;
	
	private JTextField txtCap;
	private JTextField txtNewCode;
	private JTextField txtNewTo;
	private JTextField txtCode;
	private JTextField txtTo;
	private JTextField txtCodeR;
	private JTextField txtCodeStatus;
	
	private JLabel lblOffice;
	
	private JTextArea status;
	
	private JPasswordField password;
	
	private User user;
	
	private Client client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if (args.length!=1)
		{System.out.println("Error, specifies the ip address in the command line");return;}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI(args[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void uploadUser()
	{
		try {
			user=BinaryFileUser.read("userlogin.dat");
			client.login(user.getCap(),  user.getPassword());
			main();
		} catch (Exception e) {
			login();
			user=null;
		}
	}
	private void saveUser()
	{
		try {
			BinaryFileUser.write(user, "userlogin.dat");
		} catch (Exception e) {
			File file=new File("userlogin.dat");
			if (!file.exists())
				try {
					file.createNewFile();
					BinaryFileUser.write(user, "userlogin.dat");
				} catch (Exception e1) {
				}
		}
	}
	/**
	 * Create the application.
	 */
	public ClientGUI(String ip) {
		client=new Client(ip);
		client.start();
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frameMain=new JFrame();
		uploadUser();
	}
	
	public void login() {
		frame.setVisible(true);
		frameMain.setVisible(false);
		frame.setBounds(100, 100, 494, 405);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblLogin = new JLabel("LOGIN");
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblLogin.setBounds(10, 10, 460, 37);
		frame.getContentPane().add(lblLogin);
		
		JLabel lblCap = new JLabel("CAP");
		lblCap.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCap.setHorizontalAlignment(SwingConstants.CENTER);
		lblCap.setBounds(10, 139, 145, 37);
		frame.getContentPane().add(lblCap);
		
		JLabel lblPassword = new JLabel("PASSWORD");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(10, 186, 145, 37);
		frame.getContentPane().add(lblPassword);
		
		txtCap = new JTextField();
		txtCap.setHorizontalAlignment(SwingConstants.CENTER);
		txtCap.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtCap.setBounds(165, 139, 186, 37);
		frame.getContentPane().add(txtCap);
		txtCap.setColumns(10);
		
		password = new JPasswordField();
		password.setHorizontalAlignment(SwingConstants.CENTER);
		password.setFont(new Font("Tahoma", Font.PLAIN, 20));
		password.setBounds(165, 186, 186, 37);
		frame.getContentPane().add(password);
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setBounds(223, 250, 85, 37);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					client.login(txtCap.getText(), password.getText());
					main();//frame.setVisible(false);frameMain.setVisible(true);
				}catch(Exception e ) { JOptionPane.showMessageDialog (frame, e.getMessage() );} 
			}
		});
		frame.getContentPane().add(btnLogin);
		frameMain.setVisible(false);
	}
	
	public void main()
	{
		frame.setVisible(false);
		frameMain.setVisible(true);
		frameMain.setBounds(100, 100, 1077, 746);
		frameMain.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frameMain.getContentPane().setLayout(null);
		
		lblOffice = new JLabel("OFFICE N. "+user.getCap());
		lblOffice.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblOffice.setHorizontalAlignment(SwingConstants.CENTER);
		lblOffice.setBounds(10, 10, 1015, 55);
		frameMain.getContentPane().add(lblOffice);
		
		status = new JTextArea();
		status.setLineWrap(true);
		status.setToolTipText("PackageStatus");
		status.setText("PackageStatus");
		status.setEditable(false);
		status.setForeground(Color.GREEN);
		status.setBackground(Color.DARK_GRAY);
		status.setBounds(20, 375, 1021, 324);
		JScrollPane scrollbar = new JScrollPane(status, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollbar.setBounds(20, 375, 1021, 324);
		
		frameMain.getContentPane().add(scrollbar);
		
		JLabel lblNewLabel = new JLabel("CODE PACKAGE");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 110, 175, 39);
		frameMain.getContentPane().add(lblNewLabel);
		
		txtNewCode = new JTextField();
		txtNewCode.setHorizontalAlignment(SwingConstants.CENTER);
		txtNewCode.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtNewCode.setBounds(180, 110, 175, 39);
		frameMain.getContentPane().add(txtNewCode);
		txtNewCode.setColumns(10);
		
		txtNewTo = new JTextField();
		txtNewTo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtNewTo.setHorizontalAlignment(SwingConstants.CENTER);
		txtNewTo.setBounds(576, 110, 190, 39);
		frameMain.getContentPane().add(txtNewTo);
		txtNewTo.setColumns(10);
		
		JButton btnNew = new JButton("CREATE NEW SHIPMENT");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{client.createShipment(txtNewCode.getText(), txtNewTo.getText());txtNewCode.setText("");txtNewTo.setText("");}catch(Exception e) {JOptionPane.showMessageDialog (frameMain, e.getMessage());}
			}
		});
		btnNew.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNew.setBounds(786, 110, 255, 39);
		frameMain.getContentPane().add(btnNew);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 159, 1046, 25);
		frameMain.getContentPane().add(separator);
		
		JLabel lblNewLabel_2 = new JLabel("CODE PACKAGE");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_2.setBounds(10, 180, 175, 39);
		frameMain.getContentPane().add(lblNewLabel_2);
		
		txtCode = new JTextField();
		txtCode.setHorizontalAlignment(SwingConstants.CENTER);
		txtCode.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtCode.setColumns(10);
		txtCode.setBounds(180, 180, 175, 41);
		frameMain.getContentPane().add(txtCode);
		
		JLabel lblNewLabel_1_1 = new JLabel("TO (CAP CODE)");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("Dialog", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(391, 180, 175, 39);
		frameMain.getContentPane().add(lblNewLabel_1_1);
		
		txtTo = new JTextField();
		txtTo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtTo.setHorizontalAlignment(SwingConstants.CENTER);
		txtTo.setColumns(10);
		txtTo.setBounds(576, 180, 190, 39);
		frameMain.getContentPane().add(txtTo);
		
		JButton btnSend = new JButton("SEND PACKAGE");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{client.send(txtCode.getText(), txtTo.getText());txtCode.setText("");txtTo.setText("");}catch(Exception e) {JOptionPane.showMessageDialog (frameMain, e.getMessage());}
			}
		});
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnSend.setBounds(786, 180, 190, 39);
		frameMain.getContentPane().add(btnSend);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 229, 1046, 25);
		frameMain.getContentPane().add(separator_1);
		
		JLabel lblNewLabel_2_1 = new JLabel("CODE PACKAGE");
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_2_1.setBounds(10, 250, 175, 39);
		frameMain.getContentPane().add(lblNewLabel_2_1);
		
		txtCodeR = new JTextField();
		txtCodeR.setHorizontalAlignment(SwingConstants.CENTER);
		txtCodeR.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtCodeR.setColumns(10);
		txtCodeR.setBounds(180, 250, 175, 41);
		frameMain.getContentPane().add(txtCodeR);
		
		JButton btnReceived = new JButton("RECEIVED PACKAGE");
		btnReceived.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {client.received(txtCodeR.getText());txtCodeR.setText("");}catch(Exception e) {JOptionPane.showMessageDialog (frameMain, e.getMessage());}
			}
		});
		btnReceived.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnReceived.setBounds(391, 250, 222, 39);
		frameMain.getContentPane().add(btnReceived);
		
		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setBounds(10, 299, 1046, 25);
		frameMain.getContentPane().add(separator_1_1);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("CODE PACKAGE");
		lblNewLabel_2_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_2_1_1.setBounds(10, 320, 175, 39);
		frameMain.getContentPane().add(lblNewLabel_2_1_1);
		
		txtCodeStatus = new JTextField();
		txtCodeStatus.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtCodeStatus.setHorizontalAlignment(SwingConstants.CENTER);
		txtCodeStatus.setColumns(10);
		txtCodeStatus.setBounds(180, 320, 175, 39);
		frameMain.getContentPane().add(txtCodeStatus);
		
		JButton btnStatus = new JButton("GET STATUS");
		btnStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {status.setText(client.status(txtCodeStatus.getText()));txtCodeStatus.setText("");}catch(Exception e) {JOptionPane.showMessageDialog(frameMain, e.getMessage());status.setText("PackageStatus");}
			}
		});
		btnStatus.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnStatus.setBounds(391, 320, 190, 39);
		frameMain.getContentPane().add(btnStatus);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("TO (CAP CODE)");
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setFont(new Font("Dialog", Font.PLAIN, 20));
		lblNewLabel_1_1_1.setBounds(391, 110, 175, 39);
		frameMain.getContentPane().add(lblNewLabel_1_1_1);
		
		JButton btnClose = new JButton("CLOSE");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				status.setEnabled(false);
				txtNewCode.setEnabled(false);
				btnNew.setEnabled(false);
				txtCode.setEnabled(false);
				txtTo.setEnabled(false);
				btnSend.setEnabled(false);
				txtCodeR.setEnabled(false);
				btnReceived.setEnabled(false);
				txtCodeStatus.setEnabled(false);
				btnStatus.setEnabled(false);
				//saveUser();
			}
		});
		btnClose.setIcon(null);
		btnClose.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnClose.setBounds(786, 10, 255, 90);
		frameMain.getContentPane().add(btnClose);
		
	}
	public class Client extends Thread{
		private Socket socket;
		private BufferedReader input;
		private PrintWriter output;
		private InetSocketAddress server;
		
		Client(String ip)
		{
			server=new InetSocketAddress(ip, Integer.parseInt("9696"));
			socket=new Socket();
			connect();
		}
		
		private void connect() {
			socket=new Socket();
			try{
				socket.connect(server, (10)*(1000));
				input=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output=new PrintWriter(socket.getOutputStream(), true);
				
				}catch(Exception e) 
			{System.out.println(e);}
		}
		private void check()throws Exception{
			if (!socket.isConnected() || socket.isClosed())
			{
				connect();
				login(user.getCap(), user.getPassword());
			}
		}
		public void login(String cap, String password) throws Exception
		{
			//PROTOCOL 01/CAP/PASSWORD
			String message="01/"+cap+"/"+password;
			check();
			output.println(Crypter.encrypt(message, "BaRoNeLoReNzO20011701"));
			message=Crypter.decrypt(input.readLine(), "BaRoNeLoReNzO20011701");
			if (message.equals("Successfull login"))
				user=new User(cap, password);
			else 
				throw new Exception("Login Failed");
			
		}
		
		public void createShipment(String code, String to)throws Exception
		{
			//PROTOCOL 11/packagecode/capsrc/capdest
			String message="11/"+code+"/"+user.getCap()+"/"+to;
			check();
			output.println(Crypter.encrypt(message, "BaRoNeLoReNzO20011701"));
			message=Crypter.decrypt(input.readLine(), "BaRoNeLoReNzO20011701");
			if (message.equals("Successfull"))
				throw new Exception("Package Added");
			throw new Exception("Error. "+message);
		}
		
		public void send(String code, String to)throws Exception{
			//PROTOCOL 12/packagecode/capdest
			String message="12/"+code+"/"+to;
			check();
			output.println(Crypter.encrypt(message, "BaRoNeLoReNzO20011701"));
			message=Crypter.decrypt(input.readLine(), "BaRoNeLoReNzO20011701");
			if (message.equals("Successfull"))
				throw new Exception("Sended");
			throw new Exception ("Erro. "+message);
		}
		
		public void received(String code)throws Exception{
			//PROTOCOL 13/packageCode
			String message="13/"+code;
			check();
			output.println(Crypter.encrypt(message, "BaRoNeLoReNzO20011701"));
			message=Crypter.decrypt(input.readLine(), "BaRoNeLoReNzO20011701");
			if (message.equals("Successfull"))
				throw new Exception("Success");
			throw new Exception ("Error. "+message);
		}
		
		public String status(String code)throws Exception{
			//PROTOCOL 02/packageCode
			String message="02/"+code;
			check();
			output.println(Crypter.encrypt(message, "BaRoNeLoReNzO20011701"));
			message=Crypter.decrypt(input.readLine(), "BaRoNeLoReNzO20011701");
			if (message.equals("Success"))
			{
				message=input.readLine();
				String txt="";
				String[] compose=message.split("%");
				for (int i=0;i<compose.length;i++)
					{
						txt+="\n"+compose[i]+"\n";
					}
				return txt;
			}else
				throw new Exception("Error. "+message);
		}
	}

}
