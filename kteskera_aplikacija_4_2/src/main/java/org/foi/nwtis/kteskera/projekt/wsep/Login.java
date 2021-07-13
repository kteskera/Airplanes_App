package org.foi.nwtis.kteskera.projekt.wsep;

import jakarta.ejb.EJB;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.foi.nwtis.kteskera.projekt.ejb.sb.BankaPoruka;
import org.foi.nwtis.kteskera.projekt_lib.Poruka;

@ServerEndpoint("/login")
public class Login {

    @EJB
    BankaPoruka bankaPoruka;
    
    @OnMessage
    public void stiglaPoruka(Poruka poruka, Session sessions) {
        System.out.println("WebSocket poruka " + poruka.getNaslov());
        for (Session session : sessions.getOpenSessions()) {
            if (session.isOpen()) {
                bankaPoruka.webSocketPoruke.add(poruka);
            }
        }

    }

}
