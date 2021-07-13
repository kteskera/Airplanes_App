package org.foi.nwtis.kteskera.projekt.rest;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.foi.nwtis.kteskera.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.kteskera.projekt.podaci.Dnevnik;
import org.foi.nwtis.kteskera.projekt.podaci.DnevnikDAO;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;

@Path("dnevnik")
public class DnevnikResource {

    @Inject
    ServletContext context;

    @POST
    @Consumes("application/json")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajZapis(Dnevnik d) {
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        
        DnevnikDAO ddao = new DnevnikDAO();
        boolean provjera = ddao.dodajZapis(d, pbp);

        if (!provjera) {
            return Response
                    .status(Response.Status.NOT_MODIFIED)
                    .entity("Neuspjesno dodavanje!")
                    .build();

        }
        return Response
                .status(Response.Status.OK)
                .entity("Uspješno dodavanje!")
                .build();
    }

    @GET
    @Path("/{korisnik}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dohvatiZapise(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,@PathParam("korisnik") String userName, @QueryParam("odKad") String od, @QueryParam("doKad") String doKad, @QueryParam("stranica") String stranica, @QueryParam("pomak") String pomak) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        
        
        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }
        
        
        DnevnikDAO ddao = new DnevnikDAO();
        List<Dnevnik> dnevnikLista = ddao.dohvatiZapise(userName, pbp);
        List<Dnevnik> vratiMe = new ArrayList<>();

        if (od != null && doKad != null) {
            long vrijeme = 0;
            long od1 = 0;
            long doKad1 = 0;
            for (Dnevnik d : dnevnikLista) {
                try {
                    vrijeme = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(d.getSpremljeno()).getTime() / 1000;
                    od1 = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(od).getTime() / 1000;
                    doKad1 = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(doKad).getTime() / 1000;
                } catch (ParseException ex) {
                    System.out.println(ex.getMessage());
                }
                if (vrijeme >= od1 && vrijeme <= doKad1) {
                    vratiMe.add(d);
                }
            }
            if (pomak == null && stranica == null) {
                return Response
                        .status(Response.Status.OK)
                        .entity(vratiMe)
                        .build();
            }
        }
        if (pomak != null && stranica == null) {
            if (od != null && doKad != null) {
                return Response
                        .status(Response.Status.OK)
                        .entity(vratiMe.stream().skip(Long.parseLong(pomak)).collect(Collectors.toList()))
                        .build();
            } else {
                return Response
                        .status(Response.Status.OK)
                        .entity(dnevnikLista.stream().skip(Long.parseLong(pomak)).collect(Collectors.toList()))
                        .build();
            }
        }
        if (pomak == null && stranica != null) {
            if (od != null && doKad != null) {

                return Response
                        .status(Response.Status.OK)
                        .entity(vratiMe.stream().limit(Long.parseLong(stranica)).collect(Collectors.toList()))
                        .build();
            } else {
                return Response
                        .status(Response.Status.OK)
                        .entity(dnevnikLista.stream().limit(Long.parseLong(stranica)).collect(Collectors.toList()))
                        .build();
            }
        }
        if (pomak != null && stranica != null) {
            if (od != null && doKad != null) {

                return Response
                        .status(Response.Status.OK)
                        .entity(vratiMe.stream().skip(Long.parseLong(pomak)).limit(Long.parseLong(stranica)).collect(Collectors.toList()))
                        .build();
            } else {

                return Response
                        .status(Response.Status.OK)
                        .entity(dnevnikLista.stream().skip(Long.parseLong(pomak)).limit(Long.parseLong(stranica)).collect(Collectors.toList()))
                        .build();
            }
        }
        return Response
                .status(Response.Status.OK)
                .entity(dnevnikLista)
                .build();
    }

    @GET
    @Path("/{korisnik}/broj")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dohvatiBrojZapisa(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,@PathParam("korisnik") String userName, @QueryParam("odKad") String od, @QueryParam("doKadssss") String doKad) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        
         
        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }
        
       
        DnevnikDAO ddao = new DnevnikDAO();
        List<Dnevnik> dnevnikLista = ddao.dohvatiZapise(userName, pbp);
        List<Dnevnik> vratiMe = new ArrayList<>();

        if (od != null && doKad != null) {
            long vrijeme = 0;
            long od1 = 0;
            long doKad1 = 0;
            for (Dnevnik d : dnevnikLista) {
                try {
                    vrijeme = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(d.getSpremljeno()).getTime() / 1000;
                    od1 = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(od).getTime() / 1000;
                    doKad1 = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(doKad).getTime() / 1000;
                } catch (ParseException ex) {
                    System.out.println(ex.getMessage());
                }
                if (vrijeme >= od1 && vrijeme <= doKad1) {
                    vratiMe.add(d);
                }
            }
            return Response
                    .status(Response.Status.OK)
                    .entity(vratiMe.size())
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(dnevnikLista.size())
                .build();
    }
}
