/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kteskera.projekt.podaci;

import java.sql.Timestamp;
import java.sql.Date;

/**
 *
 * @author NWTiS_1
 */
public class MyAirportLog {
    private String ident;
    private Date flightDate;
    private Timestamp stored;

    public MyAirportLog(String ident, Date flightDate, Timestamp stored) {
        this.ident = ident;
        this.flightDate = flightDate;
        this.stored = stored;
    }

    public String getIdent() {
        return ident;
    }

    public Date getFlightDate() {
        return flightDate;
    }

    public Timestamp getStored() {
        return stored;
    }

    
}
