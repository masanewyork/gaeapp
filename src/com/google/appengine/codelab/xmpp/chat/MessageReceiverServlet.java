/**
 * Copyright 2011 Google
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.appengine.codelab.xmpp.chat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.PresenceShow;
import com.google.appengine.api.xmpp.PresenceType;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
/**
 * This servlet responds to the request corresponding to a chat message sent through xmpp chat.
 * 
 * @author
 */
@SuppressWarnings("serial")
public class MessageReceiverServlet extends HttpServlet {
  private static final Logger log = Logger.getLogger(MessageReceiverServlet.class.getCanonicalName());

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    messageReceive(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
  	messageReceive(req, resp);
  }

  /**
   * The method is called when a message is received through chat. 
   * It has a hard coded reply and is sent to the user as reply using xmpp.
   * 
   * @param req : request contains the message sent through chat
   * @param resp : response sent to the user with the reply
   * @throws IOException
   */
  private void messageReceive(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    // Get the details of the sender.
    XMPPService xmpp = XMPPServiceFactory.getXMPPService();
    Message message = xmpp.parseMessage(req);
    JID fromJid = message.getFromJid();
    // Reply to the sender
    String msgBody = "We will offer a 10% discount";
    Message msg = new MessageBuilder().withRecipientJids(fromJid)
        .withBody(msgBody).build();
    SendResponse status = xmpp.sendMessage(msg);
    xmpp.sendPresence(fromJid, PresenceType.AVAILABLE, PresenceShow.NONE,
        "Sales representative for CodeLabEx3");
    boolean messageSent = (status.getStatusMap().get(fromJid) == SendResponse.Status.SUCCESS);
    if (!messageSent) {
      log.log(Level.INFO, "Could not reply to message");
    }
  }
}
