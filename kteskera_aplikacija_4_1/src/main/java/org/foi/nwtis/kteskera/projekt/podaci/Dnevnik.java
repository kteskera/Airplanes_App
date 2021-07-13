/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kteskera.projekt.podaci;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author NWTiS_1
 */
public class Dnevnik implements Serializable{
    public String spremljeno;
    public String sadrzaj;
    public String status;
    public String korisnik;

    public Dnevnik() {
    }
    
    

    public Dnevnik(String spremljeno, String sadrzaj, String status, String korisnik) {
        this.spremljeno = spremljeno;
        this.sadrzaj = sadrzaj;
        this.status = status;
        this.korisnik = korisnik;
    }

    public String getSpremljeno() {
        return spremljeno;
    }

    public void setSpremljeno(String spremljeno) {
        this.spremljeno = spremljeno;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

  
}
