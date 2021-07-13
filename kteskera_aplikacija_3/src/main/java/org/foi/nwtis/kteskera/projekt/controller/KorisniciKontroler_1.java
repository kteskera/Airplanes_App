package org.foi.nwtis.kteskera.projekt.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;
import org.foi.nwtis.kteskera.projekt_lib.Poruka;
import org.foi.nwtis.kteskera.projekt.wsep.OglasnikAplikacije;
import org.foi.nwtis.podaci.Korisnik;

@Path("korisnik")
@Controller
public class KorisniciKontroler_1 {

    @Inject
    private Models model;

    @Inject
    HttpSession session;

    @Inject
    ServletContext context;
    
     @Inject
    OglasnikAplikacije oglasnikAplikacije;

    /**
     * Metoda koja prikazuje jsp za prijavu
     */
    @Path("prijava")
    @GET
    @View("prijava.jsp")
    public void prijavaKorisnika() {

    }

    /**
     * Metoda koja prikazuje jsp za registraciju
     */
    @Path("registracijaKorisnika")
    @GET
    @View("registracijaKorisnika.jsp")
    public void registracijaKorisnika() {

    }

    @GET
    @Path("odjava")
    public String odjavaKorisnika() {
        Korisnik k = (Korisnik) session.getAttribute("korisnik");
        if (k != null) {
            
            
            Poruka p =  new Poruka();
            p.setKorisnik(k);
            p.setNaslov("Odjava");
            p.setSadrzaj("Aplikacija_3");
            //oglasnikAplikacije.posaljiPoruk(p);
            KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
            String odg = kps.logout(k.getKorisnik(), k.getIdSjednice());
            session.removeAttribute("korisnik");
            return "prijava.jsp";
        } else {
            return "prijava.jsp";
        }
    }
    
     @GET
    @Path("izbornik")
    public String izbornik() {
       return "izbornik.jsp";
    }
}
