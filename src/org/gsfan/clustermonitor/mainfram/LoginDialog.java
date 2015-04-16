package org.gsfan.clustermonitor.mainfram;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.gsfan.clustermonitor.dbconnector.ObtainUsersInformation;

@SuppressWarnings("serial")
public class LoginDialog extends JFrame implements ActionListener{
	
//	private JFrame loginWinFrame = new JFrame("Cluster Monitor");//Creat a JFrame for Login Window,specified title named Easy to use Cluster
	private JPanel loginPanel = new JPanel();
	
	private JLabel userLabel = new JLabel("�û�");
	private JLabel passwdLabel = new JLabel("����");
	private JLabel error = new JLabel();//�û��������������ʾ
	
	private JTextField userText = new JTextField(20);
	private JPasswordField passwdText = new JPasswordField(20);
	
	private Font loginFont = new Font("����", Font.PLAIN, 20);//instantiation Font
	private Font check = new Font("����", Font.PLAIN, 14);//instantiation Font
	
	private JButton submitButton = new JButton();
	private JButton registerButton = new JButton();
	
	private JCheckBox savePasswd = new JCheckBox();
	private JCheckBox autoLogin = new JCheckBox();
	
	private String username = null;
	private String passwd = null;
	public LoginDialog(){
		this.setTitle("Cluster Monitor");
		loginPanel.setLayout(null);//set layout manager
		
		userLabel.setFont(loginFont);//��������
		passwdLabel.setFont(loginFont);//��������
		
		setAllComponentPosition();
		this.add(loginPanel);//���Ƕ�������JPanel�ӵ���������JFrame֮��
		
//		this.setUndecorated(true);//�����Ƿ���ʾ������
		this.setTitle("Cluster Monitor");
		this.setResizable(false);
		this.setBounds(500,300,400,300); //set window's position and size,This method inherited from Window.class
		this.setVisible(true);//set window visable
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);//���ùرմ��ڳ���������У��������ڴ�
	}
	
	private void setAllComponentPosition(){ //set position for all components
		userLabel.setBounds(70, 40, 40, 25);
		loginPanel.add(userLabel);
		userText.setBounds(120, 40, 180, 25);
		loginPanel.add(userText);
		
		passwdLabel.setBounds(70, 90, 40, 25);
		loginPanel.add(passwdLabel);
		passwdText.setBounds(120, 90, 180, 25);
		loginPanel.add(passwdText);
		
		//����¼�
		savePasswd.addActionListener(this);
		savePasswd.setFont(check);
		savePasswd.setText("��ס����");
		savePasswd.setBounds(70, 130, 90, 30);
		loginPanel.add(savePasswd);
		
		//����¼�
		savePasswd.addActionListener(this);
		autoLogin.setFont(check);
		autoLogin.setText("�Զ���¼");
		autoLogin.setBounds(210, 130, 90, 30);
		loginPanel.add(autoLogin);
		
		//����¼�
		submitButton.addActionListener(this);
		submitButton.setBounds(70, 180, 90, 30);
		submitButton.setFont(loginFont);
		submitButton.setText("��½");
		loginPanel.add(submitButton);
		
		//�ڴ�����¼�
		registerButton.addActionListener(this);
		registerButton.setBounds(210, 180, 90, 30);
		registerButton.setFont(loginFont);
		registerButton.setText("ע��");
		loginPanel.add(registerButton);
		
//		���ñ���ɫ
//		Color bg = new Color(51, 153, 51);	// 006633
//		registerButton.setBorderPainted(false);
//		registerButton.setBackground(bg);
//		submitButton.setBorderPainted(false);
//		submitButton.setBackground(bg);
//		savePasswd.setBackground(bg);
//		autoLogin.setBackground(bg);
//		
//		loginPanel.setOpaque(true);
//		bg = new Color(0, 102, 51);	// 006633
//		loginPanel.setBackground(bg);
	}
	
	public void actionPerformed(ActionEvent event){

		if(event.getSource() == savePasswd){
			
		}else if(event.getSource() == autoLogin){
			
		}else if(event.getSource()==submitButton){
			this.loginEventHandle();
		}else if(event.getSource()==registerButton){
			this.registerEventHandle();
		}else {
			
		}

	}
	/**
	 * 
	 */
	private void loginEventHandle(){
		ObtainUsersInformation userInfo = new ObtainUsersInformation();
		this.username = userText.getText();
		this.passwd = new String(passwdText.getPassword());
		
		int res = userInfo.userAuthorization(this.username, this.passwd);
		if(0==res){
//			System.out.println("Login success");
			new MainFrame();
			this.dispose();
		}else if(1==res){
//			�޸���2015.04.15
//			System.out.println("password error");
//			error.setText("��������뱣֤��������������ȷ!");
//			error.setFont(check);
//			Rectangle rect = submitButton.getBounds();
//			error.setBounds(rect.x, rect.y+rect.height+20, this.getWidth(), 20);
//			loginPanel.add(error);
//			this.repaint();
			WarnMsgDialog msgDialog = new WarnMsgDialog("password error",3);
			Thread thread = new Thread(msgDialog);
			thread.start();
		}else {
//			�޸���2015.04.15
//			System.out.println("user not exits");
//			error.setText("�û�"+userText.getText()+"�����ڣ������������ע�����û�!");
//			error.setFont(check);
//			Rectangle rect = submitButton.getBounds();
//			error.setBounds(rect.x, rect.y+rect.height+20, this.getWidth(), 20);
//			loginPanel.add(error);
//			this.repaint();
			WarnMsgDialog msgDialog = new WarnMsgDialog("user not exits",3);
			Thread thread = new Thread(msgDialog);
			thread.start();
		}
	}
	private void registerEventHandle(){
		try {
			URI uri = new URI("http://www.baidu.com");
			Desktop.getDesktop().browse(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String argv[]){
		new LoginDialog();
	}
}