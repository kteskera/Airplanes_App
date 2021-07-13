package org.foi.nwtis.kteskera.vjezba_03.konfiguracije;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KonfiguracijaJSON extends KonfiguracijaApstraktna {

    public KonfiguracijaJSON(String nazivDatoteke) {
        super(nazivDatoteke);
    }

    @Override
    public void ucitajKonfiguraciju(String nazivDatoteke) throws NeispravnaKonfiguracija {
        this.obrisiSvePostavke();

        if (nazivDatoteke == null || nazivDatoteke.length() == 0) {
            throw new NeispravnaKonfiguracija("Naziv datoteke nije specificiran");
        }

        File file = new File(nazivDatoteke);
        if (file.exists() && file.isFile()) {
            String contents;
            try {
                contents = new String(Files.readAllBytes(Paths.get(nazivDatoteke)));
                JSONObject jsonObject = new JSONObject(contents);
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    this.postavke.setProperty(key, (String) jsonObject.get(key));
                }
            } catch (IOException ex) {
                Logger.getLogger(KonfiguracijaJSON.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            throw new NeispravnaKonfiguracija("Datoteka pod nazivom: '" + nazivDatoteke + "' ne postoji!");
        }

    }

    @Override
    public void spremiKonfiguraciju(String datoteka) throws NeispravnaKonfiguracija {

    }

}
