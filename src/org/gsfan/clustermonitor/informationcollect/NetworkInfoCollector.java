package org.gsfan.clustermonitor.informationcollect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.gsfan.clustermonitor.datatransmission.NetworkMessage;


/**
 * �ɼ��������ʹ����
 */
public class NetworkInfoCollector{
	
	private static NetworkInfoCollector INSTANCE = new NetworkInfoCollector();
	private NetworkMessage netMessage = null;
//	private final static float TotalBandwidth = 1000;	//���ڴ���,Mbps
	private float curRate = 0.0f;//�������,Mbps
	private NetworkInfoCollector(){
		this.netMessage = new NetworkMessage();
	}
	
	public static NetworkInfoCollector getInstance(){
		return INSTANCE;
	}
	
	public NetworkMessage getNetworkMessage() {
		return this.netMessage;
	}
	/**
	 * @Purpose:�ɼ��������ʹ����
	 * @param args
	 * @return float,�������ʹ����,С��1
	 */
	public void collectNetworkBandwidth() {
		Process pro = null;
		BufferedReader input = null;
		Runtime runtime = Runtime.getRuntime();
		try {
			String command = "cat /proc/net/dev";
			//��һ�βɼ���������
			long startTime = System.currentTimeMillis();
			pro = runtime.exec(command);
			input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line = null;
			long inSize1 = 0, outSize1 = 0;
			while((line=input.readLine()) != null){	
				line = line.trim();
				if(line.startsWith("eth0")){
					String[] temp = line.split("\\s+"); 
					inSize1 = Long.parseLong(temp[1]);	//Receive bytes,��λΪByte
					outSize1 = Long.parseLong(temp[9]);				//Transmit bytes,��λΪByte
					break;
				}				
			}	
			input.close();
			pro.destroy();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
			}
			//�ڶ��βɼ���������
			long endTime = System.currentTimeMillis();
			pro = runtime.exec(command);
			input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			long inSize2 = 0 ,outSize2 = 0;
			while((line=input.readLine()) != null){	
				line = line.trim();
				if(line.startsWith("eth0")){
					String[] temp = line.split("\\s+"); 
					inSize2 = Long.parseLong(temp[1]);	//Receive bytes,��λΪByte
					outSize2 = Long.parseLong(temp[9]);
					break;
				}				
			}
			if(inSize1 != 0 && outSize1 !=0 && inSize2 != 0 && outSize2 !=0){
				float interval = (float)(endTime - startTime)/500;
				//���ڴ����ٶ�,��λΪMbps
				curRate = (float)(inSize2 - inSize1 + outSize2 - outSize1)/(1024*interval);
//				netUsage = curRate/TotalBandwidth;

			}				
			input.close();
			pro.destroy();
		} catch (IOException e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
		}
		this.netMessage.setCurRate(curRate);
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		NetworkInfoCollector collector = NetworkInfoCollector.getInstance();
		
		while(true){
			collector.collectNetworkBandwidth();
			System.out.println(collector.getNetworkMessage().getCurRate());
			Thread.sleep(1000);
		}
	}
}