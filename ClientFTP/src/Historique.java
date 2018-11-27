import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Historique {

	JPanel panel;
	JTextArea text;
	Boolean connecte;
	
	public Historique(){
		this.panel = new JPanel();
		this.panel.setBackground(Color.white);
		this.panel.setBorder(BorderFactory.createLineBorder(Color.black));
	
		this.panel.setLayout(new BorderLayout());

		this.text = new JTextArea(); 
		JScrollPane scroll = new JScrollPane(text);
		panel.add(scroll, BorderLayout.CENTER);
		this.text.setEditable(false);
		this.connecte = false;
	
	}
	
	
}
