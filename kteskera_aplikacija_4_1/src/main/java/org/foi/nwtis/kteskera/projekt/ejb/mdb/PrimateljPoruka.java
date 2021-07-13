package org.foi.nwtis.kteskera.projekt.ejb.mdb;

import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.JMSConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.kteskera.projekt.ejb.sb.BankaPoruka;
import org.foi.nwtis.kteskera.projekt_lib.Poruka;

@MessageDriven(mappedName = "jms/NWTiS_PR")
public class PrimateljPoruka implements MessageListener {

    @Inject
    @JMSConnectionFactory("jms/NWTiS_QF_PR")
    private JMSContext context;

    @EJB
    BankaPoruka bankaPoruka;

    @Override
    public void onMessage(Message message) {
        ObjectMessage msg = null;
        try {
            msg = (ObjectMessage) message;
            Poruka p = (Poruka) msg.getObject();
            //bankaPoruka.poruke.add(p); 
            bankaPoruka.dodajPoruku(p);
            System.out.println(p.getNaslov());
        } catch (JMSException ex) {
            Logger.getLogger(PrimateljPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
