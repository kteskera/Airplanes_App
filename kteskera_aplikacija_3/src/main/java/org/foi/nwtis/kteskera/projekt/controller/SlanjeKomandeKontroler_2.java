package org.foi.nwtis.kteskera.projekt.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;
import org.foi.nwtis.podaci.Korisnik;

@Path("komanda")
@Controller
public class SlanjeKomandeKontroler_2 {

    @Inject
    private Models model;

    @Inject
    HttpSession session;
    @Inject
    ServletContext context;

    @FormParam("komanda")
    String komanda;

    @Path("slanjeKomande")
    @POST
    @View("slanjeKomande.jsp")
    public String slanjeKomande() {
        if (session.getAttribute("korisnik") != null) {
            Korisnik k = (Korisnik) session.getAttribute("korisnik");
            KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);           
            String odgovor=kps.slobodnaKomanda(komanda);
            model.put("poruka", odgovor);
        }else {
            return "../../index.jsp";
        }
        return null;
    }

    
    
    
    
}
