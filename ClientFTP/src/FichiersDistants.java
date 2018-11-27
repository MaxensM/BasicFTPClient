import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FichiersDistants {

	JPanel panel, barreRecherche;
	JTextArea text;
	JTextField recherche;
	JButton valider;
	
	public FichiersDistants(){
		this.panel = new JPanel();
		panel.setBackground(Color.white);
		this.panel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.panel.setLayout(new BorderLayout());

		this.text = new JTextArea(); 
		JScrollPane scroll = new JScrollPane(text);
		panel.add(scroll, BorderLayout.CENTER);
		this.text.setEditable(false);
		
		this.barreRecherche = new JPanel();
		this.recherche = new JTextField(15);
		this.valider = new JButton("Accéder");
		this.barreRecherche.add(recherche);
		this.barreRecherche.add(valider);
		panel.add(barreRecherche, BorderLayout.SOUTH);
		
	}
	
}
