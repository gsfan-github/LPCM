package org.gsfan.clustermonitor.mainfram;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.gsfan.clustermonitor.dbconnector.ClusterInfoFromMysql;
import org.jfree.data.general.DefaultPieDataset;

@SuppressWarnings("serial")
public class MainFrameSplitPane extends JSplitPane {
	private JPanel leftPanel = null;
	private JTabbedPane tabPane = null;
	
	private Hashtable<String, String> hosts = new Hashtable<String, String>();//������Ⱥӵ�е�����
	private Hashtable<String, Hashtable<String, String>> clusterAndHost = new Hashtable<String, Hashtable<String, String>>();//���еļ�Ⱥ
	private Hashtable<String, String> clusters = new Hashtable<String, String>();//���еļ�Ⱥ
//	private String currentCluster = null;//��ǰ��Ⱥ
	private ClusterInfoFromMysql clusterInfo = null;
	private Object[] comboBoxItems = null; //�����б���
	
	public static MultiDynamicLineChart cpuChart = new MultiDynamicLineChart();
	public static MultiDynamicLineChart memoryChart = new MultiDynamicLineChart();
	public static MultiDynamicLineChart networkChart = new MultiDynamicLineChart();
	public static DiskMultiplePieChart diskMultiPieChart = new DiskMultiplePieChart();

	
	public MainFrameSplitPane(LeftPanel leftPanel, JTabbedPane tabPane){
		super(JSplitPane.HORIZONTAL_SPLIT, leftPanel, tabPane);
		this.leftPanel = leftPanel;
		leftPanel.setLayout(null);
		
		this.tabPane = tabPane;

		this.setDividerSize(1);
		this.setDividerLocation(200);
		this.setEnabled(false);

		//��mysql��ȡ��Ⱥ��Ϣ
		clusterInfo = new ClusterInfoFromMysql();
//		clusterInfo = new ClusterInfoFromMysql(MainFrame.currentCluster);
		clusters = clusterInfo.getClusters();
		Iterator<String> iter = clusters.keySet().iterator();
		
		//��ȡ��ǰ��Ⱥ��������Ϣ
//		MainFrame.hostsOfCurCluster = clusterInfo.getClusterHosts(MainFrame.currentCluster);
		
		int count = clusterInfo.getClusterCount();
		comboBoxItems = new Object[count];
		int i = 0;
		while(iter.hasNext()){
			String clusterName = (String)iter.next();
			comboBoxItems[i] = clusterName;
			hosts = clusterInfo.getClusterHosts(clusterName);
			if(hosts!=null){//���õ�ǰ��Ⱥ
//				this.currentCluster = clusterName;
			}
			clusterAndHost.put(clusterName, hosts);
			i++;
		}

		leftComponentInitialization();
		rightComponentInitialization();		
//		this.tabPane.setBackground(Color.GRAY);
	}

	public JPanel getLeftPanel() {
		return leftPanel;
	}
	public void setLeftPanel(JPanel panel) {
		this.leftPanel = panel;
	}
	
	public void leftComponentInitialization(){
//		int blankAreaHeight = 20;
//		int blankAreaCount = 0;
//		int widgetHeight = 60;
//		int widgetCount = 0;	
		Font font = new Font("����", Font.PLAIN, 16);//instantiation Font
		
		ClusterDropDownList dropDownList = new ClusterDropDownList(comboBoxItems, font);
//		int yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight;
		int yStartPos = 0;
		Rectangle rect = new Rectangle(0, yStartPos, 200, dropDownList.height);
		this.addComponentAtLeftSide(dropDownList.getPanel(), rect);
	
	}
	
	public void rightComponentInitialization(){		
//		cpuChart.setPerCharPosition();
		addTabsAtRightSide("CPU", cpuChart);		
//		memoryChart.setPerCharPosition();
		addTabsAtRightSide("�ڴ�",memoryChart);		
//		diskMultiPieChart.setPerCharPosition();
		addTabsAtRightSide("����", diskMultiPieChart);		
//		networkChart.setPerCharPosition();
		addTabsAtRightSide("����", networkChart);
	}
	
	public void addTabsAtRightSide(String tabsName,JComponent component){
		
		TabsContent option = new TabsContent(tabsName, component);
		tabPane.setFont(new Font("����", Font.PLAIN, 14));
		tabPane.addTab(option.getTabsName(), option.getScrollPane());
		
	}
	

	
	public void addComponentAtLeftSide(JComponent component,Rectangle rect){
		if(rect!=null)
			component.setBounds(rect);
		this.getLeftPanel().add(component);
	}

	public static void showChart(){
		
		cpuChart.clearDynamicLineChart();
		memoryChart.clearDynamicLineChart();
		networkChart.clearDynamicLineChart();
		diskMultiPieChart.clearPieChart();

		Iterator<String> iter = MainFrame.hostsOfShowList.keySet().iterator();
		while(iter.hasNext()){
			String chartTitle = iter.next();
			String hostIP = MainFrame.hostsOfShowList.get(chartTitle);//get hostIP according to chartTitle
			DefaultPieDataset dataset = new DefaultPieDataset();
			
			MemoryDynamicLineChart memoryLineChart = new MemoryDynamicLineChart(3000, chartTitle+"�ڵ��ڴ�ʹ�����", hostIP);
			CpuDynamicLineChart cpuLineChart = new CpuDynamicLineChart(30000, chartTitle+"�ڵ�CPUʹ����", hostIP);
			NetworkDynamicLineChart networkLineChart = new NetworkDynamicLineChart(60000, chartTitle+"�ڵ���ʹ�ô���", hostIP);
			PieChart pieChart = new PieChart(dataset, chartTitle+"�ڵ����ʹ�����", hostIP);
	
			cpuChart.addDynamicLineChart(cpuLineChart);
			ChartDataGenerator dataGenerator = new ChartDataGenerator(100, cpuChart);
			cpuChart.setChartDataGenerator(dataGenerator);
			
			memoryChart.addDynamicLineChart(memoryLineChart);
			dataGenerator = new ChartDataGenerator(100, memoryChart);
			memoryChart.setChartDataGenerator(dataGenerator);
			
			networkChart.addDynamicLineChart(networkLineChart);
			dataGenerator = new ChartDataGenerator(100, networkChart);
			networkChart.setChartDataGenerator(dataGenerator);
			
			
			diskMultiPieChart.addPieChart(pieChart);
			dataGenerator = new ChartDataGenerator(100, diskMultiPieChart);
			diskMultiPieChart.setChartDataGenerator(dataGenerator);
		}
		
		cpuChart.setPerCharPosition();
		memoryChart.setPerCharPosition();
		diskMultiPieChart.setPerCharPosition();
		networkChart.setPerCharPosition();
	}
	
	class ClusterDropDownList extends JComboBox<Object> implements ItemListener {
		private JLabel label = null;
		private JPanel panel = null;
		private Object[] comboBoxItems = null;
		private HostTable hostTable = null;
		private JScrollPane jsp = null;
		private int height = 0;
		public ClusterDropDownList(Object[] comboBoxItems, Font font) {
			super(comboBoxItems);
			label = new JLabel("Current Cluster!");
			label.setText("��ǰ��Ⱥ");
			label.setFont(font);
			label.setBounds(new Rectangle(0, 0, 200, 30));
			
			this.comboBoxItems = comboBoxItems;
			this.setSelectedItem(MainFrame.currentCluster);
			this.setFont(font);
			this.setBounds(0, 30, 200, 30);
			
			hostTable = new HostTable(new HostTableModel());
			jsp = new JScrollPane(hostTable);
			jsp.setBounds(new Rectangle(0, 60, 200, 297));
			
			panel = new JPanel();
			panel.setLayout(null);
			panel.add(label);
			panel.add(this);
			panel.add(jsp);
			
			height = 30+30+297;
			this.addItemListener(this);
		}
		
		public JPanel getPanel() {
			return panel;
		}

		public void itemStateChanged(ItemEvent event) {

			if(event.getStateChange()==ItemEvent.SELECTED) {
				int index = this.getSelectedIndex();
				MainFrame.currentCluster = (String)this.comboBoxItems[index];
//				System.out.println(MainFrame.currentCluster);
				this.setSelectedItem(MainFrame.currentCluster);
				
				panel.remove(jsp);
				hostTable = new HostTable(new HostTableModel());
				jsp = new JScrollPane(hostTable);
				jsp.setBounds(new Rectangle(0, 60, 200, 295));
				panel.add(jsp);
			}
		}

	}
}

@SuppressWarnings("serial")
class LeftPanel extends JPanel {
	public LeftPanel(){
		
	}
//	@Override
//	public void paint(Graphics g){
//		super.paint(g);
//		Graphics2D g2D = (Graphics2D)g;
//		g2D.setColor(Color.GRAY);
//		g2D.setStroke(new BasicStroke(2.0f));
//		g2D.drawLine(0, 70, 207, 70);
//		g2D.drawLine(0, 185, 207, 185);
//		g2D.drawLine(0, 300, 207, 300);
//	}
}



/*
class SelectClusterComboBox extends JPanel implements ActionListener {
	private JLabel label = null;
//	private JPanel panel = null;
	private Object[] comboBoxItems = null;
	private JComboBox<Object> dropDownList = null;
	public SelectClusterComboBox(Object[] comboBoxItems, Font font) {
//		super(comboBoxItems);
		label = new JLabel("Current Cluster!");
		label.setText("��ǰ��Ⱥ");
		label.setFont(font);
		label.setBounds(new Rectangle(0, 0, 200, 30));
		this.setLayout(null);
		
		dropDownList = new JComboBox(comboBoxItems);
		dropDownList.setSelectedItem(MainFrame.currentCluster);
		this.comboBoxItems = comboBoxItems;

		this.setFont(font);
		this.setBounds(0, 0, 200, 60);
		
		this.add(label);
		this.add(dropDownList);
	}
	
//	public void itemStateChanged(ItemEvent event) {
////		Object opt = event.getSource();
////		if(event.getStateChange()==ItemEvent.SELECTED) {
////			MainFrame.currentCluster = 
////		}
//		int index = this.getSelectedIndex();
//		MainFrame.currentCluster = (String)this.comboBoxItems[index];
//		this.setSelectedItem(MainFrame.currentCluster);
//		System.out.println(MainFrame.currentCluster);
////		if(this.getSelectedIndex())
//	}
	public void actionPerformed(ActionEvent event){
		
	}
}
*/


/*		
	JLabel ipLabel = new JLabel("ip");
ipLabel.setText("�ڵ�IP");
ipLabel.setFont(font);
yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
rect.setRect(5, yStartPos, 50, widgetHeight);
this.addComponentAtLeftSide(ipLabel, rect);
JTextField ipField = new JTextField();
rect.setRect(55, yStartPos, 135, widgetHeight);
this.addComponentAtLeftSide(ipField, rect);
widgetCount++;
extraBlankCount++;

JLabel hostLabel = new JLabel("�ڵ�����");
hostLabel.setText("�ڵ���");
hostLabel.setFont(font);
yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
rect.setRect(5, yStartPos, 50, widgetHeight);
this.addComponentAtLeftSide(hostLabel, rect);
JTextField hostField = new JTextField();
rect.setRect(55, yStartPos, 135, widgetHeight);
this.addComponentAtLeftSide(hostField, rect);
widgetCount++;
extraBlankCount++;

JButton addNodeButton = new JButton("��ӽڵ�");
addNodeButton.setFont(buttonFont);
yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
rect.setRect(5, yStartPos, 90, widgetHeight);
this.addComponentAtLeftSide(addNodeButton, rect);
JButton updateButton = new JButton("�޸Ľڵ�");
updateButton.setFont(buttonFont);
yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
rect.setRect(100, yStartPos, 90, widgetHeight);
this.addComponentAtLeftSide(updateButton, rect);
widgetCount++;
blankAreaCount++;

JLabel userLabel = new JLabel("�û�");
userLabel.setText("�� ��");
userLabel.setFont(font);
yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
rect.setRect(5, yStartPos, 40, widgetHeight);
this.addComponentAtLeftSide(userLabel, rect);
JTextField userField = new JTextField();
rect.setRect(55, yStartPos, 135, widgetHeight);
this.addComponentAtLeftSide(userField, rect);
widgetCount++;
extraBlankCount++;

JLabel passLabel = new JLabel("����");
passLabel.setText("�� ��");
passLabel.setFont(font);
yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
rect.setRect(5, yStartPos, 40, widgetHeight);
this.addComponentAtLeftSide(passLabel, rect);
JPasswordField passField = new JPasswordField();
rect.setRect(55, yStartPos, 135, widgetHeight);
this.addComponentAtLeftSide(passField, rect);
widgetCount++;
extraBlankCount++;

//����������ӿؼ�
JButton modifyButton = new JButton("�޸��û���Ϣ");
modifyButton.setFont(buttonFont);
yStartPos = widgetCount*widgetHeight + blankAreaCount*blankAreaHeight+extraBlankCount*extraBlankHeight;
rect.setRect(5, yStartPos, 185, widgetHeight);
this.addComponentAtLeftSide(modifyButton, rect);
widgetCount++;
*/	
