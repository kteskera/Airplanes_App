package org.foi.nwtis.kteskera.projekt.wsep;

import jakarta.annotation.PostConstruct;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

@ClientEndpoint()
public class OglasnikKorisnika {

    private static Session session;

    @PostConstruct
    public void spajanje() {
        String uri = "ws://localhost:8380/kteskera_aplikacija_4_2/login";
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, new URI(uri));
        } catch (IOException | URISyntaxException | DeploymentException ex) {
            Logger.getLogger(OglasnikKorisnika.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public  static void posaljiPoruku(String poruka) {
        try {
            System.out.println("Aplikacija_3: " + poruka);
            session.getBasicRemote().sendText(poruka);
        } catch (IOException ex) {
            Logger.getLogger(OglasnikKorisnika.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
