import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
 
public class PieChart_AWT extends ApplicationFrame 
{
   public PieChart_AWT( String title ) 
   {
      super( title ); 
   }
   
   public static void run() throws IOException
   {
      PieChart_AWT demo = new PieChart_AWT( "Topic" + YelpHelper.topicSelected );
      demo.setContentPane(createDemoPanel( ));
      demo.setSize( 560 , 367 );    
      RefineryUtilities.centerFrameOnScreen( demo );    
      demo.setVisible( true ); 
      
   }
   
   public static PieDataset createDataset(HashMap<String,Double> res) 
   {
	  DefaultPieDataset dataset = new DefaultPieDataset( );
	  for (String key: res.keySet()) {
		   dataset.setValue(key, res.get(key));
      }
      return dataset;
   }
   
   
   public static JFreeChart createChart( PieDataset dataset )
   {
      JFreeChart chart = ChartFactory.createPieChart(      
         "Topic" + YelpHelper.topicSelected,  // chart title 
         dataset,        // data    
         true,           // include legend   
         true, 
         false);     
	  
	  PiePlot plot = (PiePlot) chart.getPlot();
      plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2}"));  // {0}: key, {1}: key.value, {2}: percentage
      return chart;
   }
   public static JPanel createDemoPanel() throws IOException
   {
	  String txt = "topic" + YelpHelper.topicSelected + ".txt";
	  HashMap<String,Double> res = readText(txt);
	  JFreeChart chart = createChart(createDataset(res));  
      
      return new ChartPanel( chart ); 
   }
   
   public static HashMap<String, Double> readText (String txt) throws IOException {
	   // ArrayList<String> res = new ArrayList<String>();
	   HashMap <String, Double> res = new HashMap<String, Double> ();
	   Scanner inFile = new Scanner(new File(txt)).useDelimiter(System.getProperty("line.separator"));
	   while (inFile.hasNext()) {
		   String temp = inFile.nextLine();
		   String[] token = temp.split(",");
		   if (res.containsKey(token[0])) {
			   res.put(token[0], res.get(token[0]) + Double.parseDouble(token[1]));
		   } else {
			   res.put(token[0], Double.parseDouble(token[1]));
		   }		   
	   }
	   inFile.close();
	   return res;
   }
}