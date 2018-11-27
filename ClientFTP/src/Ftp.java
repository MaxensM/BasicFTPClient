
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class Ftp {

	private String user;
	private Socket socket = null, socketData = null;
	private boolean pHistorique = true;
	private boolean binaire = false;
	private String host;
	private int port;
	private String mdp;

	private PrintStream writer, writerData;
	private BufferedReader readerData;
	private BufferedInputStream reader;
	private String dataIP;
	private int dataPort;
	private Historique historique;
	private Progression progression;

	public Ftp(String serveur, int port2, String id, String mdp2, Historique historiqueOriginal, Progression progressionOriginale) {
		user = id;
		port = port2;
		host = serveur;
		mdp = mdp2;
		historique = historiqueOriginal;
		progression = progressionOriginale;
		dataIP = null;
	}

	//--------------------------- FONCTIONS DE CONNEXION ---------------------------------------------
	public void connect() throws IOException {

		if (socket != null)
			throw new IOException("Connexion dèja active");

		socket = new Socket(host, port);
		reader = new BufferedInputStream(socket.getInputStream());
		writer = new PrintStream((socket.getOutputStream()));
		String response = read();

		if (!response.startsWith("220")) {
			throw new IOException("Erreur de connexion au FTP : \n" + response);
		} else {
		}

		send("USER " + user);
		response = read();
		if (!response.startsWith("331"))
			throw new IOException("Erreur de connexion avec le compte utilisateur : \n" + response);

		send("PASS " + mdp);
		response = read();
		if (!response.startsWith("230")) {
			throw new IOException("Erreur de connexion avec le compte utilisateur : \n" + response);
		} else {
			historique.connecte = true;
		}

	}

	public void quit() {
		try {
			send("QUIT");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					socket = null;
				}
			}
		}
	}

	private void enterPassiveMode() throws IOException {

		send("PASV");
		String response = read();
		String ip = null;
		int port = -1;

		int debut = response.indexOf('(');
		int fin = response.indexOf(')', debut + 1);
		if (debut > 0) {
			String dataLink = response.substring(debut + 1, fin);
			StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
			try {
				ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
						+ tokenizer.nextToken();

				port = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());
				dataIP = ip;
				dataPort = port;

			} catch (Exception e) {
				throw new IOException("Mauvaises informations envoyées au FTP : " + response);
			}
		}
	}

	private void createDataSocket() throws UnknownHostException, IOException {

		this.socketData = new Socket(dataIP, dataPort);
		this.readerData = new BufferedReader(new InputStreamReader(socketData.getInputStream()));
	}
	
	
	//------------------------------------------- FONCTIONS FTP ---------------------------------------
	public String pwd() throws IOException {
		send("PWD");
		
		return read();
	}

	public String cwd(String dir) throws IOException {
		send("CWD " + dir);
		return read();
	}

	public String xcup() throws IOException {
		send("XCUP");
		return read();
	}

	public String list() throws IOException {

		if (socket == null) connect();
		enterPassiveMode();
		createDataSocket();
		send("LIST");
		quit();
		return readData();

	}

	private void send(String command) throws IOException {

		writer.println(command);
		
		if (pHistorique)
			log(command);

	}

	
	//------------------------- Fonctions annexes (Ecriture/Lecture/Print) ----------------------------------
	private String read() throws IOException {
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);

		if (pHistorique)
			log(response);
		return response;
	}

	private String readData() throws IOException {

		String response = "";
		byte[] b = new byte[1024];
		int stream;
		String s = "";
		while ((s = readerData.readLine()) != null) {
			response += s + "\n";
		}
		readerData.close();
		return response;
	}

	public void printHistorique(boolean active) {
		pHistorique = active;
	}

	private void log(String str) {
		historique.text.append(">> " + str+ "\n");
	}
	
	
	
	public synchronized boolean stor(String filename) throws IOException {
		if (socket == null) connect();
	    File file = new File(filename);
	    return stor(new FileInputStream(file), filename);
	  }

	  public synchronized boolean stor(InputStream inputStream, String filename)
	      throws IOException {
		
		progression.text.append("Chargement de "+ filename+" : PREPARATION |");
		  
	    BufferedInputStream input = new BufferedInputStream(inputStream);
	    String response;
	    
	    enterPassiveMode();
	    send("STOR " + filename);
	    createDataSocket();
	    
	    progression.text.append(" DEBUTÉ |");
	    
	    
	    
	    response = read();
	    if (!response.startsWith("150 ")) {
	      throw new IOException("SimpleFTP was not allowed to send the file: "
	          + response);
	    }

	    BufferedOutputStream output = new BufferedOutputStream(socketData
	        .getOutputStream());
	    byte[] buffer = new byte[4096];
	    int bytesRead = 0;
	    boolean debut = false;
	    
	    while ((bytesRead = input.read(buffer)) != -1) {
	      output.write(buffer, 0, bytesRead);
	      if(!debut){
	        	progression.text.append(" EN COURS |");
	        	debut = true;
	        }
	    }
	    
	    
	    progression.text.append(" TERMINÉ\n");
	    output.flush();
	    output.close();
	    input.close();

	    response = read();
	    quit();
	    return response.startsWith("226 ");
	  }
	
	  public synchronized boolean retr(String fileName) throws IOException {

		  if (socket == null) connect();
	        if(!binaire){
	        	bin();
	        }
	        
	        progression.text.append("Téléchargement de "+ fileName+" : PREPARATION |");
	        String response = null;

	        enterPassiveMode();
	        send("RETR " + fileName);
	        createDataSocket();
	        
	        response = read();
	        log(response);
	        if(!response.startsWith("150")){
	            throw new IOException("Unable to download file from the remote server");
	        }   
	        
	        BufferedInputStream input = new BufferedInputStream(socketData.getInputStream());
	        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(new File(fileName)));

	        byte[] buffer = new byte[4096];
	        int bytesRead = 0;
	        boolean debut = false;
	        progression.text.append(" DEBUTÉ |");
	        
	        while ((bytesRead = input.read(buffer)) != -1) {
	            output.write(buffer);
	            if(!debut){
	            	progression.text.append(" EN COURS |");
	            	debut = true;
	            }   
	        }
	        
	        progression.text.append(" TERMINÉ\n");
	        
	        output.close();
	        input.close();

	        if(response.startsWith("150")){
	        	quit();
	            return true;
	        }else{
	            throw new IOException("Error");
	        }
	    }
	
	
	  public synchronized boolean bin() throws IOException {
		    send("TYPE I");
		    String response = read();
		    return (response.startsWith("200 "));
		  }
	
}
