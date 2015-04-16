package org.gsfan.clustermonitor.mainfram;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

@SuppressWarnings("serial")
public class DiskMultiplePieChart  extends JPanel implements MultiChart {
	
	private LinkedList<PieChart> chartList = null;
	private static int chartCount = 0;
	
	private ChartDataGenerator chartDataGenerator = null;
	
	public DiskMultiplePieChart() {
		super.setLayout(null);
		this.chartList = new LinkedList<PieChart>();
//		this.setBackground(Color.WHITE);
	}
	
	
	public DiskMultiplePieChart(ChartDataGenerator chartDataGenerator) {
		super.setLayout(null);
		this.chartList = new LinkedList<PieChart>();
		this.chartDataGenerator = chartDataGenerator;
//		this.setBackground(Color.WHITE);
	}
	
	
	public ChartDataGenerator getChartDataGenerator() {
		return chartDataGenerator;
	}


	public void setChartDataGenerator(ChartDataGenerator chartDataGenerator) {
		this.chartDataGenerator = chartDataGenerator;
	}


	public LinkedList<PieChart> getChartList() {
		return chartList;
	}


	public void addPieChart(PieChart lineChart){
		chartList.add(lineChart);
		chartCount++;
	}
	
	public void clearPieChart(){
//		for(ListIterator<PieChart> iter = chartList.listIterator(); iter.hasNext();){
//			chartList.remove(iter.next());
//		}
		chartCount = 0;
		chartList.clear();
	}
	
	public void setPerCharPosition(){

		this.removeAll();
		this.repaint();
		
		int count = 0;
		for(ListIterator<PieChart> iter = chartList.listIterator(); iter.hasNext();){
			
			int temp = (int)(count%3);
			int xStartPos = (temp+1)*5 + 400*temp;
			temp = (int)(count/3);
			int yStartPos = (temp+1)*15 + 300*temp;
			PieChart pieChart = iter.next();
			ChartPanel chartPanel = pieChart.getChartPanel();
			chartPanel.setBounds(xStartPos, yStartPos, 400, 300);
			this.add(chartPanel);
			count++;
		}
		count = (int)(chartCount/3);
		this.setPreferredSize(new Dimension(1220, (count+1)*15 + 300*(count+1)));
	}

	
//	public static void main(String[] args){
//		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//		String checkInType[] = { "��ʹ��", "δʹ��"};
//		String clazzs[] = { "1�Ż�", "2�Ż�", "3�Ż�", "4�Ż�" ,"5�Ż�", "6�Ż�" ,"7�Ż�","8�Ż�"};
//		for(int i=0; i<clazzs.length; i++){
//			for(int j=0; j<checkInType.length; j++){
//				int checkInData = 1 + (int)(Math.random()*100);
//				dataset.addValue(checkInData, clazzs[i], checkInType[j]);
//			}
//		}
//		new MultiplePieChart(dataset);
//	}
}