package org.foi.nwtis.kteskera.projekt.ejb.sb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import java.util.List;
import org.foi.nwtis.kteskera.projekt_lib.Poruka;



@Stateful
public class KorisniciSB {
    @EJB
    BankaPoruka bp;
    
    public List<Poruka> vratiSvePoruke(){
    return bp.vratiPoruke();
   
    }
    
    
    
}
