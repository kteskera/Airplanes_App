package org.foi.nwtis.kteskera.projekt.zrna;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.kteskera.projekt.ejb.sb.BankaPoruka;
import org.foi.nwtis.kteskera.projekt.ejb.sb.KorisniciSB;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;
import org.foi.nwtis.kteskera.projekt_lib.Poruka;

import org.foi.nwtis.podaci.Korisnik;

@Named(value = "pregledJmsPoruka")
@SessionScoped

public class PregledJmsPoruka implements Serializable {

    @Inject
    ServletContext context;

    @Getter
    @Setter
    private String poruka = "";

    @Getter
    @Setter
    List<Poruka> lista = new ArrayList<>();

    @Inject
    HttpSession session;

    @EJB
    KorisniciSB ksb;

    public PregledJmsPoruka() {
    }

    public String dajPoruke() {
        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        Korisnik k = (Korisnik) session.getAttribute("korisnik");
        String odgovor = kps.author(k.getKorisnik(), k.getIdSjednice(), "pregledJMS");
        if (odgovor.equals("OK")) {          
            lista = ksb.vratiSvePoruke();
            poruka ="";
            return "pregledJMS.xhtml";
        } else {
            poruka = "Korisnik nema pravo!";
        }
        return null;
    }
}
