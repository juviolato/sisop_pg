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

  public ChatServer()
  {
    existing_sessions = new HashSet<Session>();
  }

  @OnOpen
  public void onOpen(Session session) throws IOException
  {
    LOGGER.log(Level.FINE, "Establishing connection with SESSION ID: {0}", session.getId());
    existing_sessions.add(session);
    
    LOGGER.log(Level.FINE, "Connection ESTABLISHED. Session ID: {0}", session.getId());
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
    LOGGER.log(Level.FINE, "Connection with SESSION ID {0} being CLOSED", session.getId());
    existing_sessions.remove(session);

    LOGGER.log(Level.FINE, "Connection CLOSED");
  }

  @OnError
  public void onError(Session session, Throwable throwable)
  {

  }
}