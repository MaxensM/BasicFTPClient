import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Fenetre extends JFrame {
	
	public Fenetre(BarreConnexion barre, Historique historique, FichiersLocaux fichiersL, FichiersDistants fichiersD, Progression progression){
		
	    this.setSize(1200, 800);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    this.setLayout(new BorderLayout());
	    
	    //Construction de tout sauf la barre
	    JPanel panelPrincipal = new JPanel();
	    panelPrincipal.setLayout(new GridLayout(3,1));
	    
	    JPanel panelDouble = new JPanel();
	    panelDouble.setLayout(new GridLayout(1,2));

	    panelPrincipal.add(historique.panel);
	    panelPrincipal.add(panelDouble);
	    panelPrincipal.add(progression.panel);
	    
	    panelDouble.add(fichiersL.panel);
	    panelDouble.add(fichiersD.panel);
	    
	    //Assemblage des parties
	    this.getContentPane().add(barre.panel, BorderLayout.NORTH);
	    this.getContentPane().add(panelPrincipal, BorderLayout.CENTER);
	    
	    this.setVisible(true);
	}


}

