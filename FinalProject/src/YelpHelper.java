import java.awt.EventQueue;
import java.awt.ScrollPane;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.ButtonGroup;
import javax.swing.JTree;
import javax.swing.JComponent.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RefineryUtilities;

import com.opencsv.CSVReader;

import javax.swing.ImageIcon;
import java.awt.Dimension;
import javax.swing.JComboBox;




public class YelpHelper  {
	static String topicSelected = "";
	static int diagramSelected;
	private JFrame frame;
	private JTextField textNum1;
	private JTextArea textAreaAns;
	private JScrollPane scrollPane;
	private JLabel lblSearch;
	private JLabel lblResult;
	private JLabel lblDocNum;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnLoc, rdbtnRes, rdbtnDate, rdbtnReview;
	private JComboBox comboBoxTopic;
	private JComboBox comboBoxDiagram;
	private JButton btnGetPieChart;
	private JTextField txtResturantName;
	private JLabel lblRanking;
	// private JScrollPane scrollbar;
	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public static void main(String[] args) throws SolrServerException, IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					YelpHelper window = new YelpHelper();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public YelpHelper() throws SolrServerException, IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	private void initialize() throws SolrServerException, IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 556, 421);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		// frame.add(scrollbar);
		
		// textAreaTest.setText("The service at Pho Saigon was fast");
		JButton btnSubmit = new JButton("Search");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String searchQuery = textNum1.getText();
					// System.out.println(searchQuery);
					String str = search(searchQuery);
					System.out.println(str);
					//JTextArea textAreaAns = new JTextArea (5, 200);
					textAreaAns.setText(str);
					
				} catch (Exception searchException) {
					System.out.println(searchException.toString());
					JOptionPane.showMessageDialog(null, searchException);
				}				
			}
		});
		btnSubmit.setBounds(141, 230, 117, 29);
		frame.getContentPane().add(btnSubmit);
		
		textNum1 = new JTextField();
		textNum1.setBounds(150, 10, 374, 26);
		frame.getContentPane().add(textNum1);
		textNum1.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(150, 48, 374, 170);
		frame.getContentPane().add(scrollPane);
		
		textAreaAns = new JTextArea();
		scrollPane.setViewportView(textAreaAns);
		textAreaAns.setLineWrap(true);
		textAreaAns.setLineWrap(true);
		
		lblSearch = new JLabel("Search Query");
		lblSearch.setBounds(32, 15, 83, 16);
		frame.getContentPane().add(lblSearch);
		
		lblResult = new JLabel("Result");
		lblResult.setBounds(54, 50, 61, 16);
		frame.getContentPane().add(lblResult);
		
		lblDocNum = new JLabel("Plz Search");
		lblDocNum.setForeground(Color.RED);
		lblDocNum.setBounds(292, 235, 169, 16);
		frame.getContentPane().add(lblDocNum);
		
		rdbtnLoc = new JRadioButton("Res & Price");
		rdbtnLoc.setSelected(true);
		buttonGroup.add(rdbtnLoc);
		rdbtnLoc.setBounds(6, 76, 132, 23);
		frame.getContentPane().add(rdbtnLoc);
		
		rdbtnRes = new JRadioButton("Res & Rating");
		buttonGroup.add(rdbtnRes);
		rdbtnRes.setBounds(6, 111, 132, 23);
		frame.getContentPane().add(rdbtnRes);
		
		rdbtnDate = new JRadioButton("Date");
		buttonGroup.add(rdbtnDate);
		rdbtnDate.setBounds(6, 146, 89, 23);
		frame.getContentPane().add(rdbtnDate);
		
	    rdbtnReview = new JRadioButton("Review");
		buttonGroup.add(rdbtnReview);
		rdbtnReview.setBounds(6, 181, 99, 23);
		frame.getContentPane().add(rdbtnReview);
		
		btnGetPieChart = new JButton("Get Pie Chart");
		btnGetPieChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				topicSelected = Integer.toString(comboBoxTopic.getSelectedIndex() + 1);
				PieChart_AWT demo = new PieChart_AWT("Topic" + topicSelected);				
				try {
					demo.run();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnGetPieChart.setBounds(231, 349, 117, 29);
		frame.getContentPane().add(btnGetPieChart);
		
		

		//Create the combo box, select item at index 4.
		//Indices start at 0, so 4 specifies the pig.
		String[] pieChartTopic = { "Topic 1", "Topic 2", "Topic 3", "Topic 4", "Topic 5","Topic 6", "Topic 7", "Topic 8", "Topic 9", "Topic 10" };
		comboBoxTopic = new JComboBox(pieChartTopic);
		// comboBoxTopic.setSelectedIndex(0);
		comboBoxTopic.setBounds(113, 350, 106, 27);
		frame.getContentPane().add(comboBoxTopic);
		
		String[] diagram = {"Star vs Location", "Star vs Date", "Star vs Price", "Star vs Type", "Sentiment vs Star"};
		comboBoxDiagram = new JComboBox(diagram);
		comboBoxDiagram.setBounds(59, 311, 160, 27);
		frame.getContentPane().add(comboBoxDiagram);
		
		JButton btnGetDiagram = new JButton("Get Diagram");
		btnGetDiagram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				diagramSelected = comboBoxDiagram.getSelectedIndex();
				ShowImage diag = new ShowImage();				
				diag.run();
			}
		});
		btnGetDiagram.setBounds(231, 310, 117, 29);
		frame.getContentPane().add(btnGetDiagram);
		
		txtResturantName = new JTextField();
		txtResturantName.setText("Resturant Name - Location");
		txtResturantName.setBounds(32, 272, 182, 26);
		frame.getContentPane().add(txtResturantName);
		txtResturantName.setColumns(10);
		
		JButton btnGetRanking = new JButton("Get Ranking");
		btnGetRanking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String resturant = txtResturantName.getText();
				try {
					String resturantRanking = new DecimalFormat("#0.00").format(getRanking(resturant));
					lblRanking.setText(resturantRanking + "%");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnGetRanking.setBounds(231, 272, 117, 29);
		frame.getContentPane().add(btnGetRanking);
		
		lblRanking = new JLabel("Ranking");
		lblRanking.setForeground(Color.RED);
		lblRanking.setBounds(370, 277, 61, 16);
		frame.getContentPane().add(lblRanking);
		
		
		// scrollbar = new JScrollPane();
		// scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		// scrollbar.setSize(464, 131);
		// scrollbar.setVisible(true);
	}
	
	public String[] subStrFormat (int rdbtnID) {
		String [] res = new String [3];
		switch (rdbtnID) {
			case 1:
				res[0] = "price=[";
				res[1] = "7";
				res[2] = ", res_name=[";
				break;
			case 2:
				res[0] = "res_name=[";
				res[1] = "10";
				res[2] = ", review=[";
				break;
			case 3:
				res[0] = "rating=[";
				res[1] = "8";
				res[2] = ", price=[";
				break;
			case 4:
				res[0] = "date=[";
				res[1] = "6";
				res[2] = ", rating=[";
				break;
			case 5:
				res[0] = "review=[";
				res[1] = "8";
				res[2] = ", _version";
				break;
			default:
				// random string to make sure it cannot find it so return empty string
				res[0] = "pxs[fsrsj]{jjksjf";
				res[1] = "1000000";
				res[2] = "sifosi8rse'[=]fs";
				break;
		}
		return res;
	}
	
	public int getrdbtnSelected() {
		int i = 0;
		i = rdbtnLoc.isSelected() ? 0 : i;
		i = rdbtnRes.isSelected() ? 1 : i;
		i = rdbtnDate.isSelected() ? 2 : i;
		i = rdbtnReview.isSelected() ? 3 : i;
		return i;
	}
	
	// split the rdbtn option, return array[2].
	// for ResturantName and Price, array[0] = 1, array[1] = 0. Refer to function subStrFormat
	public int[] rdbtnSplit(int rdbtnID) {
		int[] res = new int[2];
		
		switch (rdbtnID) {
		case 0:
			res[0] = 2;
			res[1] = 1;
			break;
		case 1:
			res[0] = 2;
			res[1] = 3;
			break;
		case 2:
			res[0] = 4;
			break;
		case 3:
			res[0] = 5;
			break;
		default:
			break;
		}
		
		return res;
	}
	
	public String search(String searchText) throws SolrServerException, IOException {
		   HttpSolrServer solr = new HttpSolrServer("http://localhost:8983/solr/yelpHelper");

		   SolrQuery query = new SolrQuery();
		   query.setRows(500);
		   query.setQuery(searchText);
		   //query.addFilterQuery("cat:electronics","store:amazon.com");
		   //query.setFields("id","price","merchant","cat","store");
		   query.setStart(0);    
		   //query.set("defType", "edismax");

		   QueryResponse response = solr.query(query);
		   SolrDocumentList results = response.getResults();
		   String [] strArray = new String [results.size() + 1];
		   System.out.println(results.size());
		   strArray[0] = "===================================" + System.getProperty("line.separator");
		   String str1 = "";
		   String str2 = "";
		   String strTotal = "";
		   String id, separator;
		   
		   boolean isCombinedRdbtn = false;   // ex. resturant name AND Location
		   int rdbtnID = getrdbtnSelected();
		   int[] rdbtnSplitArray = rdbtnSplit(rdbtnID);
		   String[] sub1 = subStrFormat(rdbtnSplitArray[0]);
		   String[] sub2 = new String [3];
		   if (rdbtnSplitArray[1] == 1 || rdbtnSplitArray[1] == 3 ) {
			   sub2 = subStrFormat(rdbtnSplitArray[1]);
			   isCombinedRdbtn = true;
		   }
		   
		   lblDocNum.setText("Num Found: " + Integer.toString(results.size()));
		   for (int i = 0; i < results.size(); i++) {
			   id = "Doc " + Integer.toString(i + 1);
			   String str = results.get(i).toString();
			   str1 = "";
			   str2 = "";
			   str1 += str;
			   System.out.print(str1);
			   str1 = str1.substring(str1.indexOf(sub1[0]) + Integer.parseInt(sub1[1]), str1.indexOf(sub1[2]) - 1);
			   
			   // CombinedRdbtn case
			   if (isCombinedRdbtn) {
				   str2 += str;
				   str2 = str2.substring(str2.indexOf(sub2[0]) + Integer.parseInt(sub2[1]), str2.indexOf(sub2[2]) - 1);
				   if (sub2[0] == "price=[") {
					   str2 = ", Price: " + str2;
					   
				   } else {
					   str2 = ", Rating: " + str2;
				   }
				   
			   }
			   // str = str.substring(str.indexOf("review=[") + 8, str.indexOf(", _version") - 1);
			   separator = System.getProperty("line.separator");  // display next result in next line
			   separator += "===================================";
			   strTotal = "  |  " + id + " |  " + str1 + str2 + separator;
			   strArray[i + 1] = strTotal;
			   
		   }
		   
		   strTotal = "";
		   for (int i = 0; i < strArray.length; i++) { // strArray.length
			   strTotal += strArray[i];
		   }
		   //System.out.println(str);
		   return strTotal;
		 }
	
	public static double getRanking(String name) throws IOException{
		CSVReader reader = new CSVReader(new FileReader("sorted.csv"));
		String[] nextLine;
		double answer = 0.0;
		int rank=0;
		while ((nextLine = reader.readNext()) != null){
			rank += 1.0;
			if (nextLine[0].equals(name)){
				//answer=Double.valueOf(nextLine[1]);
				System.out.println(rank);
				answer = rank / 1043.0;
				break;
			}
		}
		answer *= 100.0;
		reader.close();
		return answer;

	}
}
