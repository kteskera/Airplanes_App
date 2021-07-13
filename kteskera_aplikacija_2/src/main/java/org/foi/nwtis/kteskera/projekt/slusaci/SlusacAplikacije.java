package org.foi.nwtis.kteskera.projekt.slusaci;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.kteskera.konfiguracije.bazaPodataka.KonfiguracijaBP;
import org.foi.nwtis.kteskera.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.kteskera.projekt.dretve.PreuzimanjeLetovaAviona;
import org.foi.nwtis.kteskera.projekt.dretve.PreuzimanjeMeteoPodataka;
import org.foi.nwtis.kteskera.vjezba_03.konfiguracije.Konfiguracija;
import org.foi.nwtis.kteskera.vjezba_03.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.kteskera.vjezba_03.konfiguracije.NeispravnaKonfiguracija;

@WebListener
public class SlusacAplikacije implements ServletContextListener {

    private PreuzimanjeLetovaAviona pla;
    private PreuzimanjeMeteoPodataka pmp;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        pla.stopp();
        ServletContext servletContext = sce.getServletContext();
        servletContext.removeAttribute("Postavke");
        servletContext.removeAttribute("PostavkeApp");
        
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        String putanjaKonfDatoteke = servletContext.getRealPath("WEB-INF") + File.separator + servletContext.getInitParameter("konfiguracija");
        String putanjaKonfDatoteke2 = servletContext.getRealPath("WEB-INF") + File.separator + servletContext.getInitParameter("postavke");
        KonfiguracijaBP konfBP = new PostavkeBazaPodataka(putanjaKonfDatoteke);
        Konfiguracija konf;
        try {
            konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanjaKonfDatoteke2);
            konfBP.ucitajKonfiguraciju();

            servletContext.setAttribute("Postavke", konfBP);
            servletContext.setAttribute("PostavkeApp", konf);

            pla = new PreuzimanjeLetovaAviona((PostavkeBazaPodataka) konfBP);
            pla.start();
            pmp = new PreuzimanjeMeteoPodataka((PostavkeBazaPodataka) konfBP);
            pmp.start();
            
        } catch (NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
