package com.LZJ.www.service;

import java.io.IOException;

import com.LZJ.www.view.ChatServiceView;
import com.LZJ.www.view.UserView;

import static java.lang.System.out;

public class SocketConnect extends Thread {
	private String name;
	public SocketConnect(String name) {
		this.name=name;
	}
	public void run() {
		int state=1;
		while(true) {
			if(UserView.userSocket!=null) {
				if(UserView.userSocket.isConnected()) {
					if(state==1) {
						String host = UserView.userSocket.getInetAddress().getHostAddress();
						new ChatServiceView(name, host);
						state++;
					}
					System.out.println("connect");
				}else {
					try {
						state=1;
						UserView.userSocket=UserView.ss.accept();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else {
				try {
					state=1;
					UserView.userSocket=UserView.ss.accept();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
