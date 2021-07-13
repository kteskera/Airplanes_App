package org.foi.nwtis.kteskera.projekt.podaci;

public class Ovlasti {
    
    public String korisnik;
    public String podrucjeRada;
    public boolean status;

    public Ovlasti(String korisnik, String podrucjeRada, boolean status) {
        this.korisnik = korisnik;
        this.podrucjeRada = podrucjeRada;
        this.status = status;
    }
    
    
    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getPodrucjeRada() {
        return podrucjeRada;
    }

    public void setPodrucjeRada(String podrucjeRada) {
        this.podrucjeRada = podrucjeRada;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    
    
    
}
