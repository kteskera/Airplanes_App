package org.foi.nwtis.kteskera.projekt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.foi.nwtis.kteskera.projekt.podaci.KorisniciKlijent_1;
import org.foi.nwtis.kteskera.projekt.podaci.KorisniciKlijent_2;
import org.foi.nwtis.podaci.Korisnik;
import jakarta.ws.rs.core.Response;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;
import org.foi.nwtis.kteskera.projekt_lib.Poruka;
import org.foi.nwtis.kteskera.projekt.wsep.OglasnikAplikacije;

@Path("korisnik")
@Controller
public class KorisniciKontroler_2 {

    @Inject
    private Models model;

    @Inject
    ServletContext context;

    @FormParam("korisnik")
    String korisnik;
    @FormParam("lozinka")
    String lozinka;

    @FormParam("imeReg")
    String imeReg;

    @FormParam("prezimeReg")
    String prezimeReg;

    @FormParam("lozinkaReg")
    String lozinkaReg;

    @FormParam("email")
    String email;

    @FormParam("korime")
    String korime;

    @Inject
    HttpSession session;

    @Inject
    OglasnikAplikacije oglasnikApk;

    /**
     * Post metoda za prijavu korisnika
     *
     * @return view
     * @throws JsonProcessingException
     */
    @POST
    @Path("prijava")
    public String prijavaKorisnika() throws JsonProcessingException {

        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (odg.split(" ")[0].equals("OK")) {

            KorisniciKlijent_2 kk2 = new KorisniciKlijent_2(korisnik);
            Response rez2 = kk2.dajKorisnika(Response.class, korisnik, lozinka);
            if (rez2.getStatus() == 200) {
                Object response = rez2.readEntity(Object.class);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String kor = gson.toJson(response);
                final ObjectMapper objectMapper = new ObjectMapper();
                Korisnik k = null;
                k = objectMapper.readValue(kor, Korisnik.class);
                k.setLozinka(lozinka);
                k.setIdSjednice(odg.split(" ")[1]);
                session.setAttribute("korisnik", k);
                
                Poruka p = new Poruka();
                p.setKorisnik(k);
                p.setNaslov("Prijava");
                p.setSadrzaj("Aplikacija_3");
                //oglasnikApk.posaljiPoruk(p);
                return "izbornik.jsp";
            } else {
                model.put("greska", "Netočni podaci za prijavu!");
                return "prijava.jsp";
            }
        }

        return "../../index.jsp";
    }

    /**
     * Post metoda za registraciju korisnika
     *
     * @return view
     */
    @POST
    @Path("registracija")
    public String registracijaKorisnika() {

        Korisnik k = new Korisnik(korime, lozinkaReg, prezimeReg, imeReg, email, 0);
        KorisniciKlijent_1 kk = new KorisniciKlijent_1();
        Response rez2 = kk.dodajKorisnika(k, Response.class);

        if (rez2.getStatus() == 200) {
            model.put("greska", "Uspješna registracija");
            return "prijava.jsp";
        } else {
            model.put("greska", "Neuspješna registracija");
            return "registracijaKorisnika.jsp";
        }
    }

}
