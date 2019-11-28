package com.LZJ.www.view;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


public class LoginView extends JFrame {
	private static final long serialVersionUID = 1L;
	JTextField textField;
	JTextField groupField;
	JRadioButton rdbtnNewRadioButton;
	JRadioButton rdbtnNewRadioButton1;
	JButton btnNewButton1;
	JButton btnNewButton;
	JLabel label;
	JLabel label1;
	
	public LoginView() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("ico/gdut.png"));
		setTitle("WeChat");
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭窗口
		setBounds(100, 100, 740, 493);
		JPanel contentPane = new JPanel();
		contentPane.setForeground(new Color(204, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		/**
		 * 用标签容器设置背景图片
		 */
//		ImageIcon backgroud=new ImageIcon("ico/background.jpg");
		ImageIcon backgroud=new ImageIcon(this.getClass().getClassLoader().getResource("ico/background.jpg"));
		JLabel labels=new JLabel(backgroud);
		labels.setBounds(0, 0, this.getWidth(), this.getHeight());
		JPanel imagejpanel=(JPanel) this.getContentPane();//把内容窗格转化成JPanel，否则不能用setOpaque来使内容窗格透明
		imagejpanel.setOpaque(false);
		this.getLayeredPane().add(labels,new Integer(Integer.MIN_VALUE));//把背景图片添加到分层窗格最底层		

		
		JButton btnNewButton = new JButton("登录");
		btnNewButton.setBounds(162, 343, 122, 50);
		btnNewButton.addActionListener(new ActionListener(){
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				UserView userView;
				try {
					//groupField.getText():225.0.0.1,225.0.0.2,225.0.0.3
					userView = new UserView(textField.getText(), groupField.getText());
					userView.setVisible(true);
					setVisible(false);
					dispose();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		
		
		JButton btnNewButton1 = new JButton("重置");
		btnNewButton1.setBounds(387, 343, 122, 50);
		btnNewButton1.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
				textField.setText("用户名");
				groupField.setText("");
			}
		});
		
		JLabel label = new JLabel("用户名");
		label.setOpaque(true);//必须设置不透明否则将不显示颜色
		label.setBackground(UIManager.getColor("Panel.background"));
		label.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("ico/user.png")));
		label.setBounds(162, 122, 73, 39);
		
		JLabel label1 = new JLabel("分组");
		label1.setOpaque(true);//必须设置不透明否则将不显示颜色
		label1.setBackground(UIManager.getColor("Panel.background"));
		label1.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("ico/password.png")));
		label1.setBounds(162, 205, 73, 39);
		
		contentPane.setLayout(null);
		contentPane.add(btnNewButton);
		contentPane.add(btnNewButton1);
		contentPane.add(label);
		contentPane.add(label1);
		
		
		textField = new JTextField();
		label.setLabelFor(textField);
		textField.setText("用户名");
		textField.setBackground(Color.WHITE);
		textField.setToolTipText("");
		textField.setBounds(265, 122, 244, 39);
		contentPane.add(textField);
		textField.setColumns(10);
		
		
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if (textField.getText().isEmpty()==false) {
					textField.setText("");
				}
					}
			public void focusLost(FocusEvent arg0) {
				if (textField.getText().isEmpty()==true) {
					textField.setText("用户名");
				}
			}
		});
		
		groupField = new JTextField();
		label1.setLabelFor(groupField);
		groupField.setBounds(265, 205, 244, 39);
		contentPane.add(groupField);
	}
}
