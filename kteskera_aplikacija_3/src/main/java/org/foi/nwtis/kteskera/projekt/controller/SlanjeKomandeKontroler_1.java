package org.foi.nwtis.kteskera.projekt.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("komanda")
@Controller
public class SlanjeKomandeKontroler_1 {

    @Inject
    private Models model;

    @Inject
    HttpSession session;
    @Inject
    ServletContext context;

    @Path("slanjeKomande")
    @GET
    @View("slanjeKomande.jsp")
    public String slanjeKomande() {
     
        if (session.getAttribute("korisnik") != null) {

            return "slanjeKomande.jsp";
        } else {
            return "../../index.jsp";
        }
         

    }
}
