package org.foi.nwtis.kteskera.projekt.sb;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.Connection;
import jakarta.jms.JMSConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;
import org.foi.nwtis.kteskera.projekt_lib.Poruka;

@Stateless
public class PosiljateljPoruka {

    @Inject
    @JMSConnectionFactory("jms/NWTiS_QF_PR")
    private JMSContext context;

    @Resource(lookup = "jms/NWTiS_PR")
    Queue requestQueue;

    public void saljiPoruku(String textPoruke) {
        System.out.println("PosiljateljPoruka:" + textPoruke);
        TextMessage poruka = context.createTextMessage(textPoruke);
        context.createProducer().send(requestQueue, poruka);
    }

    public void saljiPoruk(Poruka poruka) {
        ObjectMessage message = context.createObjectMessage(poruka);
        context.createProducer().send(requestQueue, message);
    }
}
