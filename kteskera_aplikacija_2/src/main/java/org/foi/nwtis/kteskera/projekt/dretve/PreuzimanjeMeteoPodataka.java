package org.foi.nwtis.kteskera.projekt.dretve;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.kteskera.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.kteskera.projekt.podaci.AerodromDAO;
import org.foi.nwtis.kteskera.projekt.podaci.Meteo;
import org.foi.nwtis.kteskera.projekt.podaci.MeteoDAO;
import org.foi.nwtis.kteskera.projekt.podaci.MyAirportsDAO;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.MyAirport;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.MeteoOriginal;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

public class PreuzimanjeMeteoPodataka extends Thread {

    private PostavkeBazaPodataka pbp;

    private boolean kraj = false;
    private boolean preuzimanjeStatus;
    private int preuzimanjeCiklus;
    OWMKlijent omwKlijent;
    private String openWeatherApiKey;

    public PreuzimanjeMeteoPodataka(PostavkeBazaPodataka pbp) {
        this.pbp = pbp;
    }

    @Override
    public void interrupt() {
        this.kraj = true;
        super.interrupt();
    }

    @Override
    public void run() {
        System.out.println("Počinje preuzimanje meteo podataka!");
        omwKlijent = new OWMKlijent(openWeatherApiKey);
        AerodromDAO adao = new AerodromDAO();
        MeteoDAO mdao = new MeteoDAO();
        MyAirportsDAO madao = new MyAirportsDAO();
        List<MyAirport> myAirport = madao.dohvatiSveMyAirport(pbp);
        Aerodrom a;
        MeteoPodaci mp;
        MeteoOriginal p;

        while (!kraj) {
            System.out.println("Preuzimamo meteo podatke!");
            for (MyAirport my : myAirport) {
                a = adao.dohvatiAerodrom(pbp, my.getIdent());
                mp = omwKlijent.getRealTimeWeather(a.getLokacija().getLatitude(), a.getLokacija().getLongitude());
                p = omwKlijent.getRealTimeWeatherOriginal(a.getLokacija().getLatitude(), a.getLokacija().getLongitude());
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Meteo m = new Meteo(my.getIdent(), p.getMainTemp(), p.getMainHumidity(), p.getMainPressure(), p.getWindSpeed(), p.getWindDeg(), timestamp);
                mdao.dodajMeteo(m, pbp);
            }
            try {
                Thread.sleep(preuzimanjeCiklus*60000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzimanjeMeteoPodataka.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Preuzimanje podataka zaustavljeno!");
        }
    }

    @Override
    public synchronized void start() {
        this.preuzimanjeStatus = Boolean.parseBoolean(pbp.dajPostavku("preuzimanje.status"));
        if (!preuzimanjeStatus) {
            System.out.println("Ne preuzimamo ništa!");
            return;
        }
        this.openWeatherApiKey = pbp.dajPostavku("OpenWeatherMap.apikey");
        this.preuzimanjeCiklus = Integer.parseInt(pbp.dajPostavku("preuzimanje.ciklus.meteo"));
        super.start();
    }
}
