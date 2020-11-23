import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

import java.awt.Window.Type;
import java.awt.desktop.ScreenSleepEvent;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Scrollbar;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;


public class ServerGUI {

	private JFrame frmPackageServer;
	private JTextField newUsername;
	private JPasswordField newPassword;
	private JTextArea terminal;
	private Socket connection;
	private ServerSocket socket;
	private Tracking tracking;
	private Users users;
	private Server server;
	private JLabel lblAddress;
	private JLabel lblStatus;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI window = new ServerGUI();
					window.frmPackageServer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerGUI() {
		initialize();
		server=new Server();
		server.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPackageServer = new JFrame();
		frmPackageServer.setTitle("Package Server");
		frmPackageServer.setBounds(100, 100, 1405, 646);
		frmPackageServer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmPackageServer.getContentPane().setLayout(null);
		
		
		
		terminal = new JTextArea();
		terminal.setText("Terminal");
		terminal.setToolTipText("Terminal");
		terminal.setBounds(38, 124, 882, 802);
		terminal.setEditable(false);
		terminal.setForeground(Color.RED);
		terminal.setBackground(Color.BLACK);
		
		JScrollPane scrollbar = new JScrollPane(terminal, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollbar.setBounds(38, 124, 882, 416);
		
		frmPackageServer.getContentPane().add(scrollbar);
		
		JLabel lblTitle = new JLabel("PACKAGES SERVER");
		lblTitle.setBounds(196, 10, 1166, 96);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 64));
		frmPackageServer.getContentPane().add(lblTitle);
		
		newUsername = new JTextField();
		newUsername.setBounds(930, 279, 281, 41);
		newUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
		newUsername.setText("Username (CAP)");
		frmPackageServer.getContentPane().add(newUsername);
		newUsername.setColumns(10);
		
		newPassword = new JPasswordField();
		newPassword.setBounds(930, 327, 281, 41);
		frmPackageServer.getContentPane().add(newPassword);
		
		JButton btnlAddUser = new JButton("ADD USER");
		btnlAddUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String username=newUsername.getText();
				String password=newPassword.getText();
				if (username.equals("") || username.equals(" ") || username.length()!=5 || password.equals("") || password.equals(" ") || password.length()<6)
				{
					terminal.append("\n*ERROR, USERNAME OR PASSWORD FORMATS ARE NOT VALID.*");
				}else
				{
					try{users.add(username, password);terminal.append("\n*Users Added*");}catch(Exception e) {terminal.append("\n"+e.getMessage());}
				}
			}
		});
		btnlAddUser.setBounds(1224, 303, 138, 41);
		btnlAddUser.setFont(new Font("Tahoma", Font.PLAIN, 20));
		frmPackageServer.getContentPane().add(btnlAddUser);
		
		JLabel lblUserSetup = new JLabel("USERS SETUP");
		lblUserSetup.setBounds(929, 227, 433, 41);
		lblUserSetup.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserSetup.setFont(new Font("Tahoma", Font.PLAIN, 30));
		frmPackageServer.getContentPane().add(lblUserSetup);
		
		JButton btnlViewUsers = new JButton("VIEW ALL USERS");
		btnlViewUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				terminal.append(users.get());
			}
		});
		btnlViewUsers.setBounds(930, 378, 221, 41);
		btnlViewUsers.setFont(new Font("Tahoma", Font.PLAIN, 20));
		frmPackageServer.getContentPane().add(btnlViewUsers);
		
		JLabel lblInfo = new JLabel("SERVER INFO");
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfo.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblInfo.setBounds(930, 97, 433, 41);
		frmPackageServer.getContentPane().add(lblInfo);
		
		lblAddress = new JLabel("ADDRESS 0.0.0.0:9696");
		lblAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblAddress.setBounds(930, 148, 433, 41);
		frmPackageServer.getContentPane().add(lblAddress);
		
		lblStatus = new JLabel("STATUS: ON LOAD");
		lblStatus.setForeground(new Color(255, 102, 51));
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(930, 183, 433, 41);
		frmPackageServer.getContentPane().add(lblStatus);
		
		JButton btnShutDown = new JButton("SERVER SHUT DOWN");
		
		JButton btnSwitchOn = new JButton("SERVER SWITCH ON");
		btnSwitchOn.setVisible(false);
		btnSwitchOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					lblStatus.setText("STATUS: SWITCHING ON");
					lblStatus.setForeground(new Color(0, 255, 0));
					server=new Server();
					server.start();
					lblStatus.setForeground(new Color(0, 255, 0));
					btnlViewUsers.setEnabled(true);
					btnlAddUser.setEnabled(true);
					btnShutDown.setVisible(true);
					btnSwitchOn.setVisible(false);
					frmPackageServer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				} catch (Exception e) {
					terminal.append("\n"+e.getMessage());
				}
			}
		});
		btnSwitchOn.setBounds(929, 452, 433, 88);
		btnSwitchOn.setFont(new Font("Tahoma", Font.PLAIN, 30));
		frmPackageServer.getContentPane().add(btnSwitchOn);
		
		
		btnShutDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					lblStatus.setText("STATUS: SWITCHING OFF");
					lblStatus.setForeground(new Color(255, 102, 51));
					btnlViewUsers.setEnabled(false);
					btnlAddUser.setEnabled(false);
					server.interrupt();
					server.join();
					btnShutDown.setVisible(false);
					btnSwitchOn.setVisible(true);
					frmPackageServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
				} catch (InterruptedException e) {
					terminal.append("\n"+e.getMessage());
				}
			}
		});
		btnShutDown.setBounds(929, 452, 433, 88);
		btnShutDown.setFont(new Font("Tahoma", Font.PLAIN, 30));
		frmPackageServer.getContentPane().add(btnShutDown);
	}
	
	/*
	 * Barone Lorenzo Packages Protocol
	 * 01/CAP/code access, create connection with host
	 * 02/PackageCod Status, Status of package
	 * 11/PackageCode/CapSource/CapDestination create new package
	 * 12/PackageCode/CapDestinationsend to, used for send a new package or for send a received package that is not arrived to destination place
	 * 13/PackageCode Received Package, Arrived package it is waiting to be sent to the next destination
	 * 
	 */
	public class Server extends Thread
	{
		
		public void run()
		{	
			
			try
			{
				lblStatus.setText("STATUS: SWITCHING ON");
				loadCSV();
				sleep(1000);
				users=new Users();
				sleep(1000);
				socket=new ServerSocket(9696);
				lblAddress.setText(InetAddress.getLocalHost().getHostAddress()+":"+socket.getLocalPort());
				terminal.append("\n*"+new Date()+" Server ON*");
				lblStatus.setText("STATUS: ON");
				lblStatus.setForeground(new Color(0, 255, 0));
				socket.setSoTimeout(25000);
				while (!Thread.interrupted())
				{
					try{connection=socket.accept();
					Service service=new Service(connection);
					service.start();
					}catch(SocketTimeoutException tm) {}
				}
				lblStatus.setText("STATUS: SWITCHING OFF");
				lblStatus.setForeground(new Color(255, 102, 51));
				socket.close();
				saveCSV();
				sleep(1000);
				users.download();
				sleep(1000);
				LogFile.writeLOG("logServer.log", "***"+new Date()+"***\n"+terminal.getText());
				terminal.append("\n*"+new Date()+" Server OFF*");
				lblStatus.setText("STATUS: OFF");
				lblStatus.setForeground(new Color(255, 0, 0));
			}
			catch (Exception e)
			{
				terminal.append("\n"+e);
				try{saveCSV();
				users.download();}catch(Exception a) {terminal.append("\n"+a.getMessage());}
			}
		}
		
		private void loadCSV()throws Exception
		{
			tracking = new Tracking();
			FileCSV csv=new FileCSV("%", "server.csv");
			ArrayList<String[]> issues=csv.read();//0 code/1 src/2 dest/3 time/4 time
			int count=-1;
			for (int i=0;i<issues.size();i++)
			{
				if (!issues.get(i)[0].equals("null"))
				{
					tracking.add(issues.get(i)[0], issues.get(i)[1], issues.get(i)[2]);
					count++;
				}else
				{
					try {
					    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
					    Date parsedDate = dateFormat.parse(issues.get(i)[3]);
					    Timestamp time1 = new java.sql.Timestamp(parsedDate.getTime());
					    parsedDate = dateFormat.parse(issues.get(i)[4]);
					    Timestamp time2 = new java.sql.Timestamp(parsedDate.getTime());
					    tracking.get(count).addIssue(issues.get(i)[1], issues.get(i)[2], time1, time2);
					} catch(Exception e) { 
						terminal.append(e.getMessage());
					}
					
				}
			}
		}
		private void saveCSV()throws Exception
		{
			FileCSV csv=new FileCSV("%", "server.csv");
			ArrayList<String[]> list=new ArrayList<String[]>();
			
			for (int i=0;i<tracking.size();i++)
			{
				list.add(tracking.get(i).getInfo());
				for (int y=0;y<tracking.get(i).get().size();y++)
				{
					list.add(tracking.get(i).get().get(y).info());
				}
			}
			csv.write(list);
		}
		
		
		class Service extends Thread
		{
			Socket connection;
			BufferedReader input;
			PrintWriter output;
			String hostCAP;
			public Service(Socket conn)
			{
				try
				{
					connection=conn;
					input=new BufferedReader(new InputStreamReader(conn.getInputStream()));
					output=new PrintWriter(conn.getOutputStream(),true);
				}
				catch (Exception e)
				{
					terminal.append("\n"+e.getMessage());
				}
			}
		     
			public void run()
			{
				hostCAP="null";
				String request;
				try
				{
					while ((request=Crypter.decrypt(input.readLine(), "BaRoNeLoReNzO20011701"))!=null)
					{
						terminal.append("\nFrom "+connection.getInetAddress()+"> Request: "+request);
						String reply=Crypter.encrypt(getReply(request), "BaRoNeLoReNzO20011701");
						output.println(reply);
						terminal.append("\nTo "+connection.getInetAddress()+"> Reply: "+Crypter.decrypt(reply, "BaRoNeLoReNzO20011701"));
					}
				}
				catch (Exception e)
				{
					terminal.append("\n"+e.getMessage());
				}		
			}
			
			private String getReply(String request) //Request manager
			{
				String[] msg=request.split("/");
				String reply="Successfull";
				if (msg.length<2)
					return "Protocol error.";
				String command=msg[0];
				switch(command)
				{
				case "01": //login
					if (msg.length==3)
					{
						/*if (hostCAP.equals("null"))
							return ("Error, you can't change your CAP Code;");*/
						try {
							users.login(msg[1], msg[2]); hostCAP=msg[1];
							}catch(Exception e) {
								return e.getMessage();
								}
						reply="Successfull login";
					}else return "Protocol error.";
					break;
				case "02": //status
					if (msg.length==2)
					{
						try {
							output.println("Success");
							reply=tracking.get(tracking.search(msg[1])).getStatus();
						}catch(Exception e) {return e.getMessage();}
					}else return "Protocol error";
					break;
				case "11": //create a new shipment
					if (msg.length==4)
					{
						if (!hostCAP.equals("null"))
						{
							try {
								tracking.add(msg[1], msg[2], msg[3]);
								reply="Successfull";
							}catch(Exception e) {return e.getMessage();}
						}else return "Login required";
					}else return "Protocol error";
					break;
				case "12"://send to
					if (msg.length==3)
					{
						if (!hostCAP.equals("null"))
						{
							try {
								tracking.get(tracking.search(msg[1])).sendTo(hostCAP, msg[2]);
								reply="Successfull";
							}catch(Exception e) {return e.getMessage();}
						}else return "Login required";
					}else return "Protocol error";
					break;
				case "13": //Received Package
					if (msg.length==2)
					{
						if (!hostCAP.equals("null"))
						{
							try
							{
								tracking.get(tracking.search(msg[1])).received(hostCAP);
								reply="Successfull";
							}catch(Exception e) {return e.getMessage();}
						}else return "Login Required";
					}else return "Protocol error";
					break;
				}
				
				return reply;
			}
		}
	}
}
