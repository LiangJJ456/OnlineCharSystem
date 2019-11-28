package com.LZJ.www.service;

import java.awt.EventQueue;

import com.LZJ.www.view.LoginView;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 LoginView lv = new  LoginView();
					lv.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
