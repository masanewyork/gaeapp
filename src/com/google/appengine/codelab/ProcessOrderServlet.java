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
package com.google.appengine.codelab;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.codelab.mail.SendMail;

/**
 * This servlet responds to the request corresponding to orders made from task queue and processes them.
 * It also sends a mail to the customer who placed the order.
 * 
 * @author
 */
@SuppressWarnings("serial")
public class ProcessOrderServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    processOrder(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    processOrder(req, resp);
  }

  /**
   * The method processes the order and also triggers a mail confirmation for the same.
   * 
   * @param req : HTTP request from the task queue
   * @param resp : HTTP response
   * @throws IOException
   */
  private void processOrder(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    resp.setContentType("application/json; charset=utf-8");
    resp.setHeader("Cache-Control", "no-cache");
    String customerName = req.getParameter("customerName");
    String orderId = req.getParameter("orderKey");
    Key parentKey = KeyFactory.createKey("Customer", customerName);
    Key orderKey = KeyFactory.createKey(parentKey, "Order", Integer.parseInt(orderId));

    Entity customer = Util.findEntity(parentKey);
    Entity order = Util.findEntity(orderKey);
    Iterable<Entity> lineItem = Util.listChildren("LineItem", orderKey);
    String summary = "";
    for(Entity e : lineItem){
      String name = e.getProperty("itemName").toString();
      String price = e.getProperty("price").toString();
      summary+= "Item : "+name+" | Price : "+price+"\n";
    }
    // Set the Status as Processed
    order.setProperty("status", "Processed");
    Util.persistEntity(order);

    // Send Confirmation mail
    sendConfirmationMail(summary, customer);
  }

  /**
   * Sends a mail confirmation with details about the item
   * 
   * @param itemName : name of the item in order
   * @param itemPrice : price of item
   * @param customer : customer who placed the order
   * @throws IOException
   */
  private void sendConfirmationMail(String total, Entity customer) throws IOException {
    String emailId = customer.getProperty("eMail").toString();
    String customerName = customer.getProperty("name").toString();
    String subject = "Confirmation of order";
    String msgBody = " Hi " + customerName
        + " Following order is processed and please review the order list.\n" + total;
    SendMail sendmail = new SendMail();
    sendmail.send(emailId, subject, msgBody);
  }
}
