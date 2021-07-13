package org.foi.nwtis.kteskera.projekt.wsep;

import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/vrijeme")
public class InfoVrijeme {

    static List<Session> sesije = new ArrayList<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {
        synchronized (sesije) {
            sesije.add(session);
        }
    }
       
    @OnClose
    public void onCLose(Session session, EndpointConfig conf) {
        synchronized (sesije) {
            sesije.remove(session);
        }
    }
    
    
    public static void posaljiPoruku(String poruka) {
        //System.out.println("Trenutno vrijeme:" + poruka);
        for (Session session : sesije) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(poruka);
                } catch (IOException ex) {
                    Logger.getLogger(InfoVrijeme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}
