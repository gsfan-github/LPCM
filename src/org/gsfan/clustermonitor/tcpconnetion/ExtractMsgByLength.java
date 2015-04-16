package org.gsfan.clustermonitor.tcpconnetion;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ExtractMsgByLength implements MessageExtractor{

	private static final int MAXMESSAGELENGTH = 65535;
	
	private static final int BYTEMASK = 0xff;
//	private static final int SHORTMASK = 0xffff;
	
	private static final int BYTESHIFT = 8;
	
	private DataInputStream input = null;
	
	public ExtractMsgByLength(DataInputStream input) {
		this.input = input;
	}
	
	public void constructMessage(byte message[], OutputStream out) throws IOException {
		if( message.length > MAXMESSAGELENGTH) {
			throw new IOException("Message is to long!");
		}
		//void write(int b) ������ b �İ˸���λд��������� 
		out.write( (message.length >> BYTESHIFT) & BYTEMASK);	//��д��8λ
		out.write( message.length & BYTEMASK );			//��д��8λ
		//
		out.write(message);
		out.flush();
	}
	
	public byte[] nextMessage() throws IOException {	
		int msgLength = 0;
		try {
			msgLength = input.readUnsignedShort();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		byte[] msg = new byte[msgLength];
		input.readFully(msg);
		return msg;
	}
//	public static void main(String[] args) {
//		
//	}
}
