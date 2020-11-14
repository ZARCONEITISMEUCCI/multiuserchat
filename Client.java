package Client;

import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static java.lang.System.out;

public class  Client extends JFrame implements ActionListener {
    String stringa;
    PrintWriter stampa;
    BufferedReader lettura;
    JTextArea  Messaggio;
    JTextField inserimento;
    JButton tastoInvia,tastoEsci;
    Socket client;
    
    public Client(String stringa,String servername) throws Exception {
        super(stringa); 
        this.stringa = stringa;
        client  = new Socket(servername,8080);
        lettura = new BufferedReader( new InputStreamReader( client.getInputStream()) ) ;
        stampa = new PrintWriter(client.getOutputStream(),true);
        stampa.println(stringa);  // invia il nome del client al server
        buildInterface();
        new MessagesThread().start();  // thread di attesa dei messaggi in arrivo
    }
    
    public void buildInterface() {
        tastoInvia = new JButton("Invia messaggio");
        tastoEsci = new JButton("Esci dalla chat");
        Messaggio = new JTextArea();
        Messaggio.setRows(10);
        Messaggio.setColumns(50);
        Messaggio.setEditable(false);
        inserimento  = new JTextField(50);
        JScrollPane sp = new JScrollPane(Messaggio, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp,"Center");
        JPanel bp = new JPanel( new FlowLayout());
        bp.add(inserimento);
        bp.add(tastoInvia);
        bp.add(tastoEsci);
        add(bp,"South");
        tastoInvia.addActionListener(this);
        tastoEsci.addActionListener(this);
        setSize(500,300);
        setVisible(true);
        pack();
    }
    
    public void actionPerformed(ActionEvent evt) {
        if ( evt.getSource() == tastoEsci ) {
            stampa.println("Chiudi");  // bottone di chiusura della connessione col server
            System.exit(0);
        } else {
            // invia messaggio al server
            stampa.println(inserimento.getText());
        }
    }
    
    public static void main(String ... args) {
    
        // inserisci il nome utente
        String name = JOptionPane.showInputDialog(null,"Inserisci il tuo nome :", "Nome utente",
             JOptionPane.PLAIN_MESSAGE);
        String servername = "localhost";  
        try {
            new Client( name ,servername);
        } catch(Exception ex) {
            out.println( "Errore --> " + ex.getMessage());
        }
        
    } 
    
    
    class  MessagesThread extends Thread {
        public void run() {
            String line;
            try {
                while(true) {
                    line = lettura.readLine();
                    Messaggio.append(line + "\n");
                } 
            } catch(Exception ex) {}
        }
    }
} 

