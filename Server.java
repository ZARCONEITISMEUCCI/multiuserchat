package Server;

import java.io.*;
import java.util.*;
import java.net.*;
import static java.lang.System.out;

public class  Server {
Vector<String> users = new Vector<String>();
Vector<HandleClient> clients = new Vector<HandleClient>();

public void process() throws Exception  {
   ServerSocket server = new ServerSocket(8080);
   out.println("Server avviato... ...Adesso puoi inviare messaggi ai tuoi amici!");
   while( true) {
		 Socket client = server.accept();
		 HandleClient c = new HandleClient(client);
		 clients.add(c);
  }  // fine del ciclo di inserimento dei client connessi 
}
public static void main(String ... args) throws Exception {
   new Server().process();
} 

public void boradcast(String user, String message)  {
	    // invia ai client connessi il messaggio
	    for ( HandleClient c : clients )
	       if ( ! c.getUserName().equals(user) )
	          c.sendMessage(user,message);
}

class  HandleClient extends Thread {
     String nome = "";
	BufferedReader input;
	PrintWriter output;

	public HandleClient(Socket  client) throws Exception {
      // ricevi input e output
	 input = new BufferedReader( new InputStreamReader( client.getInputStream())) ;
	 output = new PrintWriter ( client.getOutputStream(),true);
	 // leggi nome utente
	 nome  = input.readLine();
	 users.add(nome); // inserisci nel vettore
	 start();
     }

     public void sendMessage(String stringa,String  msg)  {
	    output.println( stringa + ":" + msg);
	}
		
     public String getUserName() {  
         return nome; 
     }
     public void run()  {
 	     String line;
	     try    {
             while(true)   {
		 line = input.readLine();
		 if ( line.equals("end") ) {
		   clients.remove(this);
		   users.remove(nome);
		   break;
              }
		 boradcast(nome,line); // invia in broadcast a tutti gli utenti
	       } 
	     } 
	     catch(Exception ex) {
	       System.out.println(ex.getMessage());
	     }
     } 
} 

} // end of Server