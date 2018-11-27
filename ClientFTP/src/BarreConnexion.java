import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BarreConnexion {
	
	JPanel panel;
	Integer id;
	JTextField hoteT;
	JTextField identifiantT;
	JTextField mdpT;
	JTextField portT;
	JButton connexion;
	
	public BarreConnexion(){
	
	this.panel = new JPanel();
	
	JLabel hoteL = new JLabel("Hôte :");
	this.hoteT = new JTextField(15);
	
	JLabel identifiantL = new JLabel("Identifiant :");
	this.identifiantT = new JTextField(15);
	JLabel mdpL = new JLabel("Mot de Passe :");
	this.mdpT = new JTextField(15);
	JLabel portL = new JLabel("Port :");
	this.portT = new JTextField(15);
	portT.setText("21");
	this.connexion = new JButton("Connexion");
	
	this.panel.add(hoteL);
	this.panel.add(hoteT);
	this.panel.add(identifiantL);
	this.panel.add(identifiantT);
	this.panel.add(mdpL);
	this.panel.add(mdpT);
	this.panel.add(portL);
	this.panel.add(portT);
	this.panel.add(connexion);
	
	}
}
