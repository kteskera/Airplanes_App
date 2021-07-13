package org.foi.nwtis.kteskera.projekt.dretve;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.kteskera.projekt.wsep.InfoVrijeme;

public class Vrijeme extends Thread {

    private boolean kraj = false;
    private int ciklus;
    public Vrijeme(int ciklus) {
        this.ciklus=ciklus;
    }

    @Override
    public void interrupt() {
        kraj = true;
        super.interrupt();
    }

    @Override
    public void run() {
        while (!kraj) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date date = new Date();
                InfoVrijeme.posaljiPoruku(formatter.format(date));
                Thread.sleep(ciklus*1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Vrijeme.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
