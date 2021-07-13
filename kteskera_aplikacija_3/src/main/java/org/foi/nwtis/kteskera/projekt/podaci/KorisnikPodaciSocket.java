package org.foi.nwtis.kteskera.projekt.podaci;

import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.foi.nwtis.kteskera.vjezba_03.konfiguracije.Konfiguracija;

public class KorisnikPodaciSocket {

    private int port;
    private String posluzitelj;
    Konfiguracija konf;

    public KorisnikPodaciSocket(ServletContext context) {
        konf = (Konfiguracija) context.getAttribute("PostavkeApp");
        this.port = Integer.parseInt(konf.dajPostavku("port"));
        this.posluzitelj = konf.dajPostavku("posluzitelj");
    }
    
    public String add(String username, String lozinka, String prezime, String ime) {
        String komanda = "ADD " + username + " " + lozinka + " \"" + prezime + "\" \"" + ime+"\"";
        return izvrsiKomandu(komanda, posluzitelj, port);
    }
    
    public String ovlasti(String username) {
        String komanda = "OVLASTI " + username;
        return izvrsiKomandu(komanda, posluzitelj, port);
    }

    public String authen(String username, String lozinka) {
        String komanda = "AUTHEN " + username + " " + lozinka;
        return izvrsiKomandu(komanda, posluzitelj, port);
    }

    public String logout(String username, String id) {
        String komanda = "LOGOUT " + username + " " + id;
        return izvrsiKomandu(komanda, posluzitelj, port);
    }

    public String grant(String username, String id, String podrucje, String username2) {
        
        String komanda = "GRANT " + username + " " + id + " " + podrucje+ " " + username2;
        
        return izvrsiKomandu(komanda, posluzitelj, port);
    }

    public String revoke(String username, String id, String podrucje,String username2) {
        String komanda = "REVOKE " + username + " " + id + " " + podrucje+ " " + username2;
        return izvrsiKomandu(komanda, posluzitelj, port);
    }

    public String rights(String username, String id,String username2) {
        String komanda = "RIGHTS " + username + " " + id+ " " + username2;
        return izvrsiKomandu(komanda, posluzitelj, port);
    }

    public String author(String username, String id, String podrucje) {
        String komanda = "AUTHOR " + username + " " + id + " " + podrucje;
        return izvrsiKomandu(komanda, posluzitelj, port);
    }

    public String list(String username, String id, String username2) {
        String komanda = "LIST " + username + " " + id + " " + username2;
        return izvrsiKomandu(komanda, posluzitelj, port);
    }

    public String listAll(String username, String id) {
        String komanda = "LISTALL " + username + " " + id;
        return izvrsiKomandu(komanda, posluzitelj, port);
    }
    
     public String slobodnaKomanda(String komanda) {
        return izvrsiKomandu(komanda, posluzitelj, port);
    }

    private String izvrsiKomandu(String komanda, String adresa, int port) {
        try (Socket uticnica = new Socket(adresa, port);
                InputStream is = uticnica.getInputStream();
                OutputStream os = uticnica.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os)) {
            osw.write(komanda);
            osw.flush();
            uticnica.shutdownOutput();

            StringBuilder tekst = new StringBuilder();

            while (true) {
                int i = is.read();
                if (i == -1) {
                    break;
                }
                tekst.append((char) i);
            }
            uticnica.shutdownInput();
            uticnica.close();
            return tekst.toString();
        } catch (IOException ex) {
            System.out.println("Neuspjesno spajanje na posluzitelj " + ex);
        }

        return "Ne moguce je izvršiti komandu!";
    }

}
