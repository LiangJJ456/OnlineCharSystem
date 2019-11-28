package com.LZJ.www.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class MultiChatSocket {
	private InetAddress group = null;
	private MulticastSocket multiSocket = null;
	private String nickName = null;
	private int port;
	private String host;

	public MultiChatSocket(String nickName,String host) {
		this.host=host;
		this.nickName = nickName;
		try {
			port=12346;
			group = InetAddress.getByName(host);
			multiSocket = new MulticastSocket(port);
			multiSocket.setTimeToLive(32);
			multiSocket.joinGroup(group);
			// System.out.println("Address------>"+multiSocket.getInetAddress().toString());
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "无法连接到聊天室！", "警告信息", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "无法绑定端口" + port, "警告信息", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) throws UnknownHostException {
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String time = timeFormat.format(date);
		// 给要发送的信息加上昵称和时间
		String finalMessage = nickName + "@"+InetAddress.getLocalHost().getHostAddress() +" "+ time + "\n" + " " + message + "\n";
		byte[] buffer = finalMessage.getBytes();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group,port);
		try {

			multiSocket.send(packet);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "无法发送数据：" + message, "警告信息", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
	}

	public String receivedMessage() {
		String revMessage = null;
		byte[] buffer = new byte[8192];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		try {
			multiSocket.receive(packet);
			revMessage = new String(packet.getData(), 0, packet.getLength());

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "无法接收消息！", "警告信息", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
		return revMessage;
	}

	public void chatClose() {
		try {
			multiSocket.leaveGroup(group);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "关闭聊天室出错！", "警告信息", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		} finally {
			if (multiSocket != null) {
				multiSocket.close();
			}
		}

	}
}
