package org.foi.nwtis.kteskera.projekt.podaci;

import java.sql.Timestamp;

public class Meteo {

    public String ident;
    public float temperatura;
    public float vlaga;
    public float tlak;
    public float brzinaVjetra;
    public float smjerVjetra;
    public Timestamp datum;

    public Meteo(String ident, float temperatura, float vlaga, float tlak, float brzinaVjetra, float smjerVjetra, Timestamp datum) {
        this.ident = ident;
        this.temperatura = temperatura;
        this.vlaga = vlaga;
        this.tlak = tlak;
        this.brzinaVjetra = brzinaVjetra;
        this.smjerVjetra = smjerVjetra;
        this.datum=datum;
    }

    public Timestamp getDatum() {
        return datum;
    }

    public void setDatum(Timestamp datum) {
        this.datum = datum;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public float getVlaga() {
        return vlaga;
    }

    public void setVlaga(float vlaga) {
        this.vlaga = vlaga;
    }

    public float getTlak() {
        return tlak;
    }

    public void setTlak(float tlak) {
        this.tlak = tlak;
    }

    public float getBrzinaVjetra() {
        return brzinaVjetra;
    }

    public void setBrzinaVjetra(float brzinaVjetra) {
        this.brzinaVjetra = brzinaVjetra;
    }

    public float getSmjerVjetra() {
        return smjerVjetra;
    }

    public void setSmjerVjetra(float smjerVjetra) {
        this.smjerVjetra = smjerVjetra;
    }
    
    
}
