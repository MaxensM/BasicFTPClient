import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FichiersLocaux {

	public JPanel panel, barreTransfert;
	public JButton transfert;
	public JTextField recherche;
	
	private JTree arbre, arbre2, arbre3;
	private DefaultMutableTreeNode racine;
	private  DefaultTreeCellRenderer[] tCellRenderer = new  DefaultTreeCellRenderer[3];
	
	public FichiersLocaux(){
		this.panel = new JPanel();
		panel.setBackground(Color.white);
		this.panel.setLayout(new BorderLayout());
		this.panel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		this.barreTransfert = new JPanel();
		this.transfert = new JButton("Accéder");
		this.recherche = new JTextField(15);
		barreTransfert.add(transfert);
		barreTransfert.add(recherche);
		panel.add(barreTransfert, BorderLayout.SOUTH);
		
		creationArborescence();

	} 
	
	public void creationArborescence(){
		initRenderer();
	    listRoot();
	}
	
	  private void initRenderer(){
	    this.tCellRenderer[0] = new  DefaultTreeCellRenderer();
	    /*this.tCellRenderer[0].setClosedIcon(new ImageIcon("img/ferme.jpg"));
	    this.tCellRenderer[0].setOpenIcon(new ImageIcon("img/ouvert.jpg"));
	    this.tCellRenderer[0].setLeafIcon(new ImageIcon("img/feuille.jpg"));*/

	    this.tCellRenderer[1] = new  DefaultTreeCellRenderer();
	    this.tCellRenderer[1].setClosedIcon(null);
	    this.tCellRenderer[1].setOpenIcon(null);
	    this.tCellRenderer[1].setLeafIcon(null);
	  }

	  private void listRoot(){      
	    this.racine = new DefaultMutableTreeNode();       
	    int count = 0;
	    for(File file : File.listRoots()){
	      DefaultMutableTreeNode lecteur = new DefaultMutableTreeNode(file.getAbsolutePath());
	      try {
	        for(File nom : file.listFiles()){
	          DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName()+"\\");               
	          lecteur.add(this.listFile(nom, node));               
	        }
	      } catch (NullPointerException e) {}

	      this.racine.add(lecteur);
	    }
	    
	    arbre = new JTree(this.racine);
	    arbre.setRootVisible(false);
	    //On définit le rendu pour cet arbre
	    arbre.setCellRenderer(this.tCellRenderer[0]);

	    arbre2 = new JTree(this.racine);
	    arbre2.setRootVisible(false);
	    arbre2.setCellRenderer(this.tCellRenderer[1]);

	    arbre3 = new JTree(this.racine);
	    arbre3.setRootVisible(false);

	   JSplitPane split = new JSplitPane(   JSplitPane.HORIZONTAL_SPLIT, 
	      new JScrollPane(arbre2), 
	      new JScrollPane(arbre3));
	    split.setDividerLocation(200);
	      
	    JSplitPane split2 = new JSplitPane(   JSplitPane.HORIZONTAL_SPLIT, 
	      new JScrollPane(arbre), 
	      split);
	    split2.setDividerLocation(200);
	    
	    JScrollPane jsp = new JScrollPane(arbre);
	    jsp.setPreferredSize(new Dimension(600,240));
	    this.panel.add(jsp, BorderLayout.CENTER);
	    
	    //this.panel.add(BorderLayout.CENTER, split2);
	  }

	  private DefaultMutableTreeNode listFile(File file, DefaultMutableTreeNode node){
	    int count = 0;

	    if(file.isFile())
	      return new DefaultMutableTreeNode(file.getName());
	    else{
	      File[] list = file.listFiles();
	      if(list == null)
	        return new DefaultMutableTreeNode(file.getName());

	      for(File nom : list){
	        count++;
	        //Pas plus de 5 enfants par noeud
	        if(count < 5){
	          DefaultMutableTreeNode subNode;
	          if(nom.isDirectory()){
	            subNode = new DefaultMutableTreeNode(nom.getName()+"\\");
	            node.add(this.listFile(nom, subNode));
	          }else{
	            subNode = new DefaultMutableTreeNode(nom.getName());
	          }
	          node.add(subNode);
	        }
	      }
	      return node;
	    }
	  }
 
	}
