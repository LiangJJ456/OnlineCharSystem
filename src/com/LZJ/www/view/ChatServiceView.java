package com.LZJ.www.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.LZJ.www.view.ChatClientView.SendMassage;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

import static java.lang.System.out;


public class ChatServiceView extends JFrame {
	private static final long serialVersionUID = 1L;
	protected JFrame frame;
    protected JTextArea contentArea;
    protected JTextField txt_field;
    protected JButton btn_upload;
    protected JButton btn_sendFile;
    protected JButton btn_send;
    protected JButton closeConnectButton;
    protected JPanel northPanel;
    protected JPanel southPanel;
    protected JScrollPane containPanel;
    protected JPanel centerpanel;
    private File sendFile;
	public ChatServiceView(String username,String host) {
		try {
			//文件传输的线程
	        ReceiveFile receiveFile=new ReceiveFile("D:\\");
	        receiveFile.start();
	        //消息接收
	        BufferedReader br= new BufferedReader(new InputStreamReader(UserView.userSocket.getInputStream()));
	        ReceiveMassage rm=new ReceiveMassage(br);
			rm.start();
	        //按钮初始化
	        btn_upload = new JButton("传输文件");
	        btn_upload.addActionListener(new ActionListener() {							
				@Override
				public void actionPerformed(ActionEvent e) {
					if(sendFile!=null) {
						String absolutePath=sendFile.getAbsolutePath();
						SendFile sendFile=new SendFile(absolutePath,host);
						sendFile.uploadFile();
						sendFile=null;
					}
				}
			});
	        btn_send = new JButton("发送");
	        btn_send.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		PrintWriter pw;
					try {
						pw = new PrintWriter(UserView.userSocket.getOutputStream());
						String state="正常";
						SendMassage sm=new SendMassage(pw,username,host,state);
		    			sm.sendMassage();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	        	}
	        });
	
	        contentArea = new JTextArea();
	        contentArea.setEditable(false);
	        contentArea.setForeground(Color.blue);
	
	        txt_field = new JTextField();
	
	        //好友信息
	        btn_sendFile = new JButton("选择传输文件");
	        btn_sendFile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser jf = new JFileChooser();
					jf.showOpenDialog(btn_sendFile);//显示打开的文件对话框
					sendFile=  jf.getSelectedFile();//使用文件类获取选择器选择的文件
				}
			});
	        northPanel = new JPanel();
	        northPanel.setLayout(new GridLayout(1, 8));
	        closeConnectButton=new JButton("关闭TCP连接");
	        closeConnectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						PrintWriter pw;
						try {
							pw = new PrintWriter(UserView.userSocket.getOutputStream());
							//通知下线
							String state="我离开聊天室了";
							SendMassage sm=new SendMassage(pw,username,host,state);
			    			sm.sendMassage();
			    			contentArea.append("TCP连接已关闭，请关闭聊天窗口");
			    			closeConnectButton.setVisible(false);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						rm.stop();
						UserView.userSocket.close();
						UserView.userSocket=null;
						if(UserView.fileSocket!=null) {
							UserView.fileSocket.close();
							UserView.fileSocket=null;
						}
						receiveFile.exit=true;
						try {
							receiveFile.join();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
	        northPanel.add(closeConnectButton);
	        northPanel.add(btn_sendFile);
	        northPanel.add(btn_upload);
	        northPanel.setBorder(new TitledBorder("收到用户信息"));
	
	        
	        //发送消息组件
	        southPanel = new JPanel(new BorderLayout());
	        southPanel.setBorder(new TitledBorder("写消息"));
	        southPanel.add(txt_field, "Center");
	        southPanel.add(btn_send, "East");
	
	        //内容组件
	        centerpanel = new JPanel();
	        centerpanel.setBorder(new TitledBorder(null, "\u6D88\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	        centerpanel.setLayout(null);
	        containPanel = new JScrollPane(contentArea);
	        containPanel.setBounds(0, 24, 582, 208);
	        centerpanel.add(containPanel);
	
	        
	        //frame
	        frame = new JFrame("聊天室");
	        frame.getContentPane().setLayout(new BorderLayout());
	        frame.getContentPane().add(northPanel, "North");
	        frame.getContentPane().add(southPanel, "South");
	        frame.getContentPane().add(centerpanel, "Center");
	        
	        frame.setSize(600, 400);
	        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
	        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
	        frame.setLocation((screen_width - frame.getWidth()) / 2,
	                (screen_height - frame.getHeight()) / 2);
	        frame.setVisible(true);
	        //***************************************************//
	        this.addWindowListener(new WindowAdapter(){ //添加一个窗口监听
				public void windowClosing(WindowEvent e) { //这是窗口关闭事件
					PrintWriter pw;
					try {
						pw = new PrintWriter(UserView.userSocket.getOutputStream());
						String state="我离开聊天室了";
						SendMassage sm=new SendMassage(pw,username,host,state);
		    			sm.sendMassage();
		    			br.close();
						rm.stop();
						receiveFile.exit=true;
						try {
							receiveFile.join();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (Exception e1) {
						System.out.println("error");
						e1.printStackTrace();
					}
				}
			});
        //****************************************//
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 接受文件
	 * @author hasee
	 *
	 */
 class ReceiveFile extends Thread{
	 private String savePath;
	 public volatile boolean exit = false;
	public ReceiveFile(String savePath) {
		this.savePath=savePath;
	}
	public void run() {
		while(!exit) {
			try {
				//文件传输的的socket
				if(UserView.fileSocket!=null) {
					if(!UserView.fileSocket.isConnected()) {
						UserView.fileSocket=UserView.fileSercerSocket.accept();//注意此处处于阻塞状态
					}
				}else {
					UserView.fileSocket=UserView.fileSercerSocket.accept();//注意此处处于阻塞状态
				}
			}catch (IOException e) {
				System.out.println("stop");
			}
			try {
				DataInputStream inputStream = new DataInputStream(new BufferedInputStream(UserView.fileSocket.getInputStream()));
				//本地保存路径，文件名会自动从服务器端继承而来
				int bufferSize = 8192;
				byte[] buf = new byte[bufferSize];
				long passedlen = 0;
				long len = 0;
				
				//获取文件名
				String file = savePath + inputStream.readUTF();
				DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
				len = inputStream.readLong();
				contentArea.append("文件的长度为:  " + len  + "\n");
				contentArea.append("开始接收文件!" + "\n");
				
				while(true) {
						int read = 0;
						if (inputStream != null) {
							read = inputStream.read(buf);
						}
						passedlen += read;
						if(read == -1) {
							break;
						}
						//进度条输出
						//System.out.println("文件完成度:  " + (passedlen/len * 100) + "%");
						
						/*System.out.println(passedlen/len );
						
						System.out.println(passedlen/len * 100);*/
						
						fileOut.write(buf, 0, read);
						fileOut.flush();
						double a  = Double.valueOf(String.valueOf(passedlen))/Double.valueOf(String.valueOf(len))*100;
						contentArea.append("文件完成度:  " +a+ "%"+ "\n");
				}
				contentArea.append("文件保存在: " +file+ "\n");
					fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}
		
 }
    /**
	 * 发送文件
	 * @author hasee
	 *
	 */
 class SendFile{
	 private String absolutePath;
	 private String host;
	 public SendFile(String absolutePath,String host) {
		 this.absolutePath=absolutePath;
		 this.host=host;
	 }
	 public void uploadFile(){
			Socket s = null;
			try {
					s = new Socket(host, UserView.filePort);				
					//选择进行文件传输的文件
					File fi = new File(absolutePath);
					contentArea.append("传输文件为:" + fi.getAbsolutePath() +"\n");
					DataInputStream fis = new DataInputStream(new FileInputStream(fi.getAbsolutePath()));
					DataOutputStream ps  = new DataOutputStream(s.getOutputStream());
					ps.writeUTF(fi.getName());
					ps.flush();
					ps.writeLong((long) fi.length());
					ps.flush();
					
					int bufferSize = 8192;
					byte[] buf = new byte[bufferSize];
					
					while(true) {
							int read = 0;
							if (fis != null) {
									read = fis.read(buf);
							}
							
							if(read == -1) {
								break;
							} 
							ps.write(buf,0,read);
					}
					ps.flush();
					//注意关闭socket链接哦，不然客户端会等待server的数据过来
					//直到socket超时，导致数据不完整
					fis.close();
					ps.close();
					s.close();
					contentArea.append("文件传输完成"+"\n");
			}catch (Exception e) {
					e.printStackTrace();
			}
		}
 }
	/**
	 * 接收信息
	 * @author hasee
	 *
	 */
 class ReceiveMassage extends Thread {
		private BufferedReader br;//输入流
		public ReceiveMassage() {
			
		}
		
		public ReceiveMassage( BufferedReader br) {
			super();
			this.br = br;
		}

		public BufferedReader getBr() {
			return br;
		}

		public void setBr(BufferedReader br) {
			this.br = br;
		}

		public void run() {
			while(true) {
				try {
					String massage=br.readLine();
					contentArea.append(massage+"\r\n");
					String[] string=massage.split(":");
					if(string[1].equals("我离开聊天室了")) {
						contentArea.append("TCP连接已关闭，请关闭聊天窗口");
						closeConnectButton.setVisible(false);
						UserView.userSocket.close();
						UserView.userSocket=null;
						if(UserView.fileSocket!=null) {
							UserView.fileSocket.close();
							UserView.fileSocket=null;
						}
						//关闭窗口
						setVisible(false);
						dispose();
						break;
					}
				} catch (IOException e) {
					break;
				}
			}
		}
		
	}
 /**
  * 发送消息
  * @author hasee
  *
  */
 class SendMassage {
		private PrintWriter pw;//输出流
		private String username;//用户名
		private String host;
		private String state;
		public SendMassage() {
			
		}
		
		public SendMassage( PrintWriter pw,String username,String host,String state) {
			super();
			this.pw = pw;
			this.username=username;
			this.host=host;
			this.state=state;
		}
		
		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public PrintWriter getBw() {
			return pw;
		}

		public void setBw(PrintWriter pw) {
			this.pw = pw;
		}

		public void sendMassage() throws UnknownHostException {
			if(state.equals("我离开聊天室了")) {
				pw.println(UserView.nickName + "@" + InetAddress.getLocalHost().getHostAddress() + ":" + state);
				pw.flush();
			}else {
				if (txt_field.getText() != null) {
					pw.println(UserView.nickName + "@" + InetAddress.getLocalHost().getHostAddress() + ":" + txt_field.getText());
					pw.flush();
					contentArea.append(UserView.nickName + "@" + InetAddress.getLocalHost().getHostAddress() + ":" + txt_field.getText() + "\r\n");
					txt_field.setText(null);
				}
			}
		}
		
	}
}
