package org.gsfan.clustermonitor.datatransmission;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FrameMsgByLength implements MessageFramer{
	
	private DataInputStream input = null;
	private static final int MAXMESSAGELENGTH = 65535;
	private static final int BYTEMASK = 0xff;
	private static final int BYTESHIFT = 8;
	
	public FrameMsgByLength(InputStream input) {
		this.input = new DataInputStream(input);
	}
	
	public void frameMessage(byte message[], OutputStream out) throws IOException {
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
}