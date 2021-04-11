import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;
import javax.websocket.OnOpen;
import javax.websocket.OnMessage;
import javax.websocket.OnClose;
import javax.websocket.OnError;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.*;

import java.io.*;


@ServerEndpoint("/chat")
public class ChatServer
{
  private static final Logger LOGGER = Logger.getLogger(ChatServer.class.getName());
  private static Set<Session> existing_sessions;

  @OnOpen
  public void onOpen(Session session) throws IOException
  {
    LOGGER.info(String.format("Establishing connection with SESSION ID: %s", session.getId()));
    existing_sessions.add(session);
    
    LOGGER.info(String.format("Connection ESTABLISHED. Session ID: %s", session.getId()));
  }

  @OnMessage
  public void onMessage(String message, Session session) throws IOException
  {
    for (Session peer : existing_sessions)
    {
      peer.getBasicRemote().sendText(message);
    }
  }

  @OnClose
  public void onClose(Session session) throws IOException
  {
    LOGGER.info(String.format("Connection with SESSION ID %s being CLOSED", session.getId()));
    existing_sessions.remove(session);

    LOGGER.info("Connection CLOSED");
  }

  @OnError
  public void onError(Session session, Throwable throwable)
  {
    LOGGER.info(String.format("ERROR occured: %s", throwable));

    try
    {
      LOGGER.info(String.format("CLOSING connection for SESSION ID %s.", session.getId()));
      this.requestingSession.close(new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, "Exception occurred: " + throwable.toString()));
      LOGGER.info("Connection CLOSED.");

    } catch (IOException e)
    {
      Logger.getLogger(ConnectionAcceptEndpoint.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}