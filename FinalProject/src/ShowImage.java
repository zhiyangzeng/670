import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

//class image extends JFrame{
//	ImageIcon image;
//	String file = "star_v_type.png";
//	image (String filename) {
//		setLayout(new FlowLayout());
//		this.image = new ImageIcon(filename);
//		//label = new JLabel(image);
//		//add(label);
//	}
//	
//}
public class ShowImage extends JFrame {
//    private BufferedImage img;
//	
//	public ShowImage() {
//		try {
//			img = ImageIO.read(new File("star_v_type.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void paint(Graphics g) {
//		g.drawImage(img, 0, 0, null);
//	}
	
	private ImageIcon image;
	private JLabel label;
	private int diagIndexSelected;
	private String[] diagramTitle = {"Star vs Location", "Star vs Date", "Star vs Price", "Star vs Type", "Sentiment vs Star"};
	private String fileName;
	ShowImage() {
		setLayout(new FlowLayout());
		diagIndexSelected = YelpHelper.diagramSelected;
		// diagIndexSelected = 4;
		fileName = diagramTitle[diagIndexSelected] ;
		image = new ImageIcon(getClass().getResource(fileName + ".png"));
		// image = new ImageIcon(getClass().getResource("Location.png"));
		//ImageIcon image = new ImageIcon(getClass().getResource(filename));
		label = new JLabel(image);
		add(label);
	}
	
	public static void run() {
		ShowImage image = new ShowImage();
		image.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		image.setVisible(true);
		// image.displayImage("star_v_type.png");
		image.pack();
		image.setTitle(image.fileName);
	}
	
	
//	public static void displayImage (String fileName) {
//		ShowImage diagram = new ShowImage();
//		diagram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		diagram.setVisible(true);
//		diagram.pack();
//		diagram.setTitle("star_v_type.png");
//	}
}
