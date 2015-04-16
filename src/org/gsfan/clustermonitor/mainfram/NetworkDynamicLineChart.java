package org.gsfan.clustermonitor.mainfram;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;

import org.gsfan.clustermonitor.datatransmission.FrameMsgByDelimiter;
import org.gsfan.clustermonitor.datatransmission.Message;
import org.gsfan.clustermonitor.datatransmission.MessageCodec;
import org.gsfan.clustermonitor.datatransmission.MessageFramer;
import org.gsfan.clustermonitor.datatransmission.MessageTextCodec;
import org.gsfan.clustermonitor.datatransmission.NetworkMessage;
import org.gsfan.clustermonitor.datatransmission.TCPClient;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class NetworkDynamicLineChart extends DynamicLineChart{

	private TimeSeries netBindWidth = null;
	private ChartPanel chartPanel = null;
//	private JFreeChart chart = null;
	
	@SuppressWarnings("deprecation")
	public NetworkDynamicLineChart(int maxAge,String titleName, String hostIP){
		super(titleName, hostIP);
		
//		super(titleName, hostIP, false);
//		super(titleName, hostIP, 0);
		
		this.netBindWidth = new TimeSeries("��ǰ��ʹ�õĴ���");
		this.netBindWidth.setMaximumItemAge(maxAge);
		
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(netBindWidth);
		
		DateAxis domain = new DateAxis("ʱ��");
		NumberAxis range = new NumberAxis("����/Mbps");
		Font tickFont = new Font("SansSerif", Font.PLAIN, 10);
		Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
		domain.setTickLabelFont(tickFont);
		range.setTickLabelFont(tickFont);
		domain.setLabelFont(labelFont);
		range.setLabelFont(labelFont);
		range.setRange(0.0, 65);
//		range.setTickUnit(new NumberTickUnit(5));//���ÿ̶��߼��
		
		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
		renderer.setSeriesPaint(0, Color.red);
		renderer.setSeriesPaint(1, Color.blue);
		renderer.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
		
		XYPlot plot = new XYPlot(dataset, domain, range, renderer);
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		domain.setAutoRange(true);
		domain.setLowerMargin(0.0);
		domain.setUpperMargin(0.0);
		domain.setTickLabelsVisible(true);
		
		range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		Font titleFont = new Font("SansSerif", Font.BOLD, 14);
		JFreeChart chart = new JFreeChart(this.titleName, titleFont, plot, true);
		chart.setBackgroundPaint(Color.white);
		chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 4, 4),
				BorderFactory.createLineBorder(Color.black)));
	}
	public String getTitleName(){
		return this.titleName;
	}
	
	public ChartPanel getChartPanel(){
		return this.chartPanel;
	}
	
	/**
	* Adds an observation to the 'net bindwith' time series.
	* @param y the total memory used.
	*/
	private void addNetBindWidthObservation(float y) {
//		XYPlot plot = this.chart.getXYPlot();
//		NumberAxis range = (NumberAxis)plot.getRangeAxis();
//		if(y<1.0){
//				range.setRange(0, 1.0);
//				range.setTickUnit(new NumberTickUnit(0.1));
//		} else {
//			range.setRange(0, y+2);
//		}
		if(y<0.1)
			y=0.1f;//��y<0.1ʱ,����ͼ�޷���ʾ
		this.netBindWidth.add(new Millisecond(), y);
	}
	
	public void firstCommunication(Socket client) {
		if(client==null)
			return;
		MessageCodec codec = new MessageTextCodec();
		MessageFramer framer;
		try {
			framer = new FrameMsgByDelimiter(client.getInputStream());
			NetworkMessage netMsg = new NetworkMessage();
			
			byte[] encodeMsg = codec.messageEncode(netMsg);
			
			System.out.println("First communication:Sending net message ("+ encodeMsg.length + ") bytes:");
			System.out.println(netMsg);
			framer.frameMessage(encodeMsg, client.getOutputStream());
		} catch (IOException e) {
			System.out.println("In " + this + "firstCommunication error!");
		}
//		finally {
//			try {
//				client.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	public void subsequentCommunication(TCPClient client) {
		if(client==null)
			return;
		Message message = null;
		NetworkMessage netMsg = null;
		try{
			
			MessageCodec codec = new MessageTextCodec();
			FrameMsgByDelimiter framer = new FrameMsgByDelimiter(client.getInputStream());
			byte[] req;
			while((req=framer.nextMessage())!=null){
				System.out.println("Received net message ("+req.length+") bytes" );
				message = codec.messageDecode(req);
				if(message instanceof NetworkMessage){
					netMsg = (NetworkMessage)message;
				}
				System.out.println(netMsg);
			} 
		}catch (IOException ioe){
			System.out.println("Error handling client:"+ioe.getMessage());
		} finally {
			System.out.println("************Client closing connection!*************");
			client.disconnectToServer();
		}
		addNetBindWidthObservation(netMsg.getCurRate());
	}
}
