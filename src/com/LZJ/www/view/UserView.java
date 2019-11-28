package com.LZJ.www.view;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.LZJ.www.po.User;
import com.LZJ.www.service.MultiChatSocket;
import com.LZJ.www.service.SocketConnect;

import javax.swing.JSeparator;
import javax.swing.JTextArea;

import java.awt.Color;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;


public class UserView extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  static String nickName;
	public static JPanel panel_3;
	public static Socket userSocket;
	public static Socket fileSocket;
	public static ServerSocket ss;
	public static ServerSocket fileSercerSocket=null;
	public static int port=1234;
	public static int filePort=1235;
	public List<User> onlineUsers=new ArrayList<User>();
	public List<String> receviceMessageList=new ArrayList<>();
	private JPanel contentPane;
	JInternalFrame internalFrame;

	/**
	 * Create the frame.
	 */
	public  UserView(String nickName,String host) throws Exception {
		this.nickName=nickName;
		MultiChatSocket multiChatSocket=new MultiChatSocket(nickName,host);
		//广播上线信息
		multiChatSocket.sendMessage("我上线啦!!!!");
		multiChatSocket.sendMessage("我上线啦!!!!");
		//***********************************************************//
		setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("ico/user.png")));
		setTitle("WeChat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 493);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		/**
		 * 桌面1
		 */
		internalFrame = new JInternalFrame("用户管理");
		internalFrame.setResizable(true);
		internalFrame.setBounds(10, 10, 704, 400);
		contentPane.add(internalFrame);
		internalFrame.getContentPane().setLayout(null);
		
		JPanel panel =new JPanel();
		panel.setBounds(0, 0, 150, 360);
		internalFrame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel(" 在线用户");
		label.setFont(new Font("宋体", Font.PLAIN, 30));
		label.setForeground(Color.GREEN);
		label.setBounds(0, 0, 150, 35);
		JSeparator js=new JSeparator();
		js.setBackground(Color.YELLOW);
		js.setSize(336, 1);
		js.setLocation(0, 33);
		panel.add(label);
		panel.add(js);
		//在线好友列表
		panel_3 = new JPanel();
		panel_3.setBounds(0, 33, 150, 303);
		panel.add(panel_3);
		panel_3.setLayout(null);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(130, 0, 21, 303);
		panel_3.add(scrollBar);
		//消息显示窗口
		JPanel jPanel2=new JPanel();
		internalFrame.getContentPane().add(jPanel2);
		jPanel2.setLayout(null);
		jPanel2.setBounds(150, 0, 530, 360);
		// 创建聊天消息展示窗口
		JTextArea messageDisplay = new JTextArea();
		messageDisplay.setBounds(0, 0, 530, 360);
		messageDisplay.setEditable(false);
		messageDisplay.setLineWrap(true);
		JScrollPane scroll = new JScrollPane(messageDisplay);
		scroll.setBounds(0, 0, 530, 360);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// 把聊天消息展示窗口加入到第二个容器中
		jPanel2.add(scroll);
		//****************************************************//
		//开启线程广播接收上线信息
		Thread receivedThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					String revMessage = null;
					revMessage = multiChatSocket.receivedMessage();
					receviceMessageList.add(revMessage);
					int revFlag=0;
					for(int index=0;index<receviceMessageList.size();index++) {
						if(revMessage.equals(receviceMessageList.get(index))) {
							revFlag++;
						}
					}
					if(revFlag==1) {
						messageDisplay.append(revMessage);
						messageDisplay.setSelectionStart(messageDisplay
						.getText().length());
					}
					String[] strings=revMessage.split(" ");
					String[] stringss=strings[0].split("@");
					User newUser=new User();
					newUser.setName(stringss[0]);
					newUser.setHost(stringss[1]);
					if("我上线啦!!!!\n".equals(strings[3])) {
						//回复已经收到消息
						try {
							onlineUsers.add(newUser);
							if(!nickName.equals(stringss[0])) {
								multiChatSocket.sendMessage("我收到了!!!!");
								multiChatSocket.sendMessage("我收到了!!!!");
							}
						} catch (UnknownHostException e1) {
							e1.printStackTrace();
						}
					}
					if("我收到了!!!!\n".equals(strings[3])) {
						if(!nickName.equals(stringss[0])) {
							onlineUsers.add(newUser);
						}
					}
					if("我下线啦!!!!\n".equals(strings[3])) {
						User outlineUser=new User();
						for(User onUser:onlineUsers) {
							if(stringss[0].equals(onUser.getName())) {
								outlineUser=onUser;
							}
						}
						onlineUsers.remove(outlineUser);
					}
					//**************************************************//
					//重构内容
					UserView.panel_3.removeAll();
					UserView.panel_3.validate();// 重构内容面板
					UserView.panel_3.repaint();// 重绘内容面板
					int i=0;
					List<User> temUser=new ArrayList<>();
					for(int j=0;j<onlineUsers.size();j++) {
						String temName=onlineUsers.get(j).getName();
						int flag=0;
						for(int k=j+1;k<onlineUsers.size();k++) {
							if(temName.equals(onlineUsers.get(k).getName())) {
								flag=1;
							}
						}
						if(flag==0) {
							temUser.add(onlineUsers.get(j));
						}
					}
					onlineUsers=temUser;
					if(onlineUsers.isEmpty()!=true) {
						for(User user:onlineUsers) {
							JButton btnNewButton;
							if(nickName.equals(user.getName())) {
								btnNewButton = new JButton(user.getName()+"(自己)");
							}else {
								btnNewButton = new JButton(user.getName());
							}
							btnNewButton.setBounds(0, i, 150, 27);
							btnNewButton.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									new ChatClientView(user.getName(),user.getHost());
								}
							});
							UserView.panel_3.add(btnNewButton);
							i=i+28;
						}
					}
					UserView.panel_3.setVisible(true);
					//******************************************************//
				}
			}
		}); 
		receivedThread.start();
		//******************开启服务端端口*******************************//
		ss=new ServerSocket(port);
		fileSercerSocket=new ServerSocket(filePort);
		SocketConnect sc=new SocketConnect(nickName);
		sc.start();
		
		
		JButton button = new JButton("退出登录");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//发送下线消息
				try {
					multiChatSocket.sendMessage("我下线啦!!!!");
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				setVisible(false);
				dispose();
				try {
					sc.stop();
					receivedThread.stop();
					ss.close();
					if(fileSercerSocket!=null) {
						fileSercerSocket.close();
					}
					if(userSocket!=null) {
						userSocket.close();
					}
					if(fileSocket!=null) {
						fileSocket.close();
					}
					multiChatSocket.chatClose();
				} catch (IOException e) {
					e.printStackTrace();
				}
				LoginView loginview=new LoginView();
				loginview.setVisible(true);
			}
		});
		button.setBounds(621, 421, 93, 23);
		contentPane.add(button);
		
		//****************************************************//
		internalFrame.setVisible(true);
		this.addWindowListener(new WindowAdapter(){ //添加一个窗口监听
			public void windowClosing(WindowEvent e) { //这是窗口关闭事件
				try {
					//发送下线消息
					try {
						multiChatSocket.sendMessage("我下线啦!!!!");
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
					multiChatSocket.chatClose();
					sc.stop();
					receivedThread.stop();
					if(userSocket!=null) {
						userSocket.close();
					}
					if(fileSocket!=null) {
						fileSocket.close();
					}
					ss.close();
					if(fileSercerSocket!=null) {
						fileSercerSocket.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}
