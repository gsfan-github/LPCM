package org.gsfan.clustermonitor.tcpconnetion;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class Client extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	Button button1,button2,button3,button4,button5,button6;
	TextArea text1,text2;
	int size,type;
	int count=0;
	JMenu jm;
	JMenuItem t_over,t;
	JMenuBar  br;
	private String [] outs;
	ServerThread thread1;
	
	public Client(){
		setLayout(null);
		size=80;
		type=Font.BOLD;		
		setTitle("Linux��Ⱥ״̬�����");
		setSize(630,450);
		setLocationRelativeTo(null);
		setVisible(true);
		
	    jm=new JMenu("�鿴") ;     //����JMenu�˵�����
	    br=new  JMenuBar() ;  //�����˵�������
	    t_over=new JMenuItem("�˳�") ;  //�˵���
		text1=new TextArea("",50,50);
		text2=new TextArea("",50,50);
		button1=new Button("ϵͳ");
		button2=new Button("��Դ");
		button3=new Button("����");
		button4=new Button("����");
		button5=new Button("����");
		button6=new Button("�û�");
		text1.setBounds(05,70,600,250);
		text2.setBounds(05,320,600,50);
		button1.setBounds(05, 20, 70, 30);
		button2.setBounds(85, 20, 70, 30);
		button3.setBounds(165, 20, 70, 30);
		button4.setBounds(245, 20, 70, 30);
		button5.setBounds(325, 20, 70, 30);
		button6.setBounds(405, 20, 70, 30);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		button5.addActionListener(this);
		button6.addActionListener(this);
		Font font1=new Font("Roman",Font.BOLD,18);
		Font font2=new Font("Times",Font.BOLD,18);
		text1.setFont(font1);
		text2.setFont(font1);
		button1.setFont(font2);
		button2.setFont(font2);
		button3.setFont(font2);
		button4.setFont(font2);
		button5.setFont(font2);
		button6.setFont(font2);
	    jm.add(t_over) ;   //���˵���Ŀ��ӵ��˵�
	    br.add(jm) ;      //���˵����ӵ��˵�������
	    this.setJMenuBar(br) ;  //Ϊ ��������  �˵�������
		add(text1);
		add(text2);
		add(button1);
		add(button2);
		add(button3);
		add(button4);
		add(button5);
		add(button6);
		t_over.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 System.exit(0);
			}
			   
		});
		
	      try{
	          text2.setText("�ȴ�����");
	          @SuppressWarnings("resource")
	   	      ServerSocket sk = new ServerSocket(4005);
	          while(true){
	        	 Socket connectToClient=sk.accept();
	             text2.append("\nlinux����"+(++count)+":"+connectToClient.getInetAddress()+"��������");
	             t=new JMenuItem("linux_"+(count)) ;//�˵���
	             jm.add(t) ;    //���˵���Ŀ��ӵ��˵�
	             thread1=new ServerThread(connectToClient);
	     		 t.addActionListener(new ActionListener(){
	   			 public void actionPerformed(ActionEvent e) {
	   				 thread1.start();
	   			}	   			   
	   		});
	          }
	         }catch (Exception e) 
	         {
	            e.printStackTrace();
	         }
		
	}
	
	public class ServerThread extends Thread
	{
	   Socket server;
	   public ServerThread(Socket socket){
		   server=socket;
	   }

	   public void run()
	   {
	      try
	      {
		   ObjectInputStream ip=new ObjectInputStream(server.getInputStream());
	       outs = (String[])ip.readObject();
	         
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }
	   }
	}

	public void actionPerformed(ActionEvent e) {

		if(e.getSource()==button1)
		{
			//String[] names = ((String)outs[0]).split("\n");
			text1.setText("linux��ϵͳ��Ϣ��\n\n"+"------------------------------------"
					+ "-----------------------------------\n\n"
			+outs[0]);
		}

		else if(e.getSource()==button2)
		{
			//String[] names = ((String)outs[1]).split("\n");
			text1.setText("linux����Դ��Ϣ��\n\n"+"--------------------------------------"
					+ "---------------------------------\n\n"
			+outs[1]);
		}

    		else if(e.getSource()==button3)
    		{
    			//String[] names = ((String)outs[2]).split("\n");
    			text1.setText("linux�ķ�����Ϣ��\n\n"+"---------------------------------"
    					+ "--------------------------------------\n\n"
    			+outs[2]);
    		}

    		else if(e.getSource()==button4)
    		{
    			//String[] names = ((String)outs[3]).split("\n");
    			text1.setText("linux��������Ϣ��\n\n"+"-----------------------------------"
    					+ "------------------------------------\n\n"
    			+outs[3]);
    		}

    		else if(e.getSource()==button5)
    		{
    			//String[] names = ((String)outs[4]).split("\n");
    			text1.setText("linux�Ľ�����Ϣ��\n\n"+"-------------------------------------"
    					+ "----------------------------------\n\n"
    			+outs[4]);
    		}

    		else if(e.getSource()==button6)
    		{
    			//String[] names = ((String)outs[5]).split("\n");
    			text1.setText("linux���û���Ϣ��\n\n"+"--------------------------------------"
    					+ "---------------------------------\n\n"
    			+outs[5]);
    		}
	
	}
	
	public  static  void  main(String  args[]){
		new  Client();

    }
}