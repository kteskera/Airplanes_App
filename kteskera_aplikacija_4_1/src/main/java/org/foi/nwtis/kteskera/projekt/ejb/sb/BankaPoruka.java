/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kteskera.projekt.ejb.sb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.servlet.ServletContext;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.kteskera.projekt_lib.Poruka;

@Startup
@Singleton
public class BankaPoruka {

    @Getter
    @Setter
    public List<Poruka> poruke = new ArrayList<>();

    public List<Poruka> webSocketPoruke = new ArrayList<>();

    public void dodajPoruku(Poruka poruka) {

        this.poruke.add(poruka);
    }

    public List<Poruka> vratiPoruke() {
        
        System.out.println(poruke);
        return this.poruke;
    }

    /*
    public void zapisiUDatoteku(ServletContext context) throws IOException {
        String putanjaKonfDatoteke = context.getRealPath("WEB-INF");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String JSONObject = gson.toJson(poruke);
        FileWriter file = new FileWriter(putanjaKonfDatoteke + "/poruke.json");
        file.write(JSONObject);
      
    }
    
    public void dohvatiIzDatoteke(ServletContext context) throws IOException {
        String putanjaKonfDatoteke = context.getRealPath("WEB-INF");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String JSONObject = gson.toJson(poruke);
        FileWriter file = new FileWriter(putanjaKonfDatoteke + "/poruke.txt");
        file.write(JSONObject);
      
    }*/
}
