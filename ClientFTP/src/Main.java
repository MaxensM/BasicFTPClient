import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	static Ftp ftp;
	
	public static Ftp connexion(BarreConnexion barre, Historique historique, FichiersDistants fichiersD, FichiersLocaux fichiersL, Progression progression) throws IOException{
		
		 String serveur = barre.hoteT.getText();
		 String id = barre.identifiantT.getText();
		 String mdp = barre.mdpT.getText();
		 int port = Integer.parseInt(barre.portT.getText());
		 
		 Scanner sc = new Scanner(System.in);
         Ftp ftp = new Ftp(serveur, 21, id, mdp, historique, progression);

         ftp.connect();
         ftp.printHistorique(true);
         historique.text.append("-------------------------------------------------------\n");
         historique.text.append("Connecté au FTP\n");
         historique.text.append("-------------------------------------------------------\n\n");
         
         String list = ftp.list();
         fichiersD.text.append(list);         
         return ftp;
         
	}
	
	public static void upload(FichiersLocaux fichiersL) throws IOException{
		String filename = fichiersL.recherche.getText();
		ftp.stor(filename);
	}

	public static void download(FichiersDistants fichiersD) throws IOException{
		String filename = fichiersD.recherche.getText();
		ftp.retr(filename);
		
	}
	
	public static void main(String[] args) {
		
		BarreConnexion barre = new BarreConnexion();
	    Historique historique = new Historique();
	    FichiersDistants fichiersD = new FichiersDistants();
	    FichiersLocaux fichiersL = new FichiersLocaux();
	    Progression progression = new Progression(); 
	    Boolean connecte = false;
		Fenetre fenetre = new Fenetre(barre, historique, fichiersL, fichiersD, progression);
	    
		barre.connexion.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (!(barre.hoteT.getText() == "" || barre.identifiantT.getText() == "" || barre.mdpT.getText() == ""))
					ftp = connexion(barre, historique, fichiersD, fichiersL, progression);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		fichiersD.valider.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					download(fichiersD);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});
		
		fichiersL.transfert.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					upload(fichiersL);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});
		
		
					
		

	}

}
