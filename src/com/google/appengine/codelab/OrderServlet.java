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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

/**
 * This servlet responds to the request corresponding to orders. The Class
 * places the order.
 * 
 * @author
 */
@SuppressWarnings("serial")
public class OrderServlet extends BaseServlet {

  private static final Logger logger = Logger.getLogger(OrderServlet.class.getCanonicalName());
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  /**
   * Redirect the call to doDelete or doPut method.
   */
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String action = req.getParameter("action");
    if (action.equalsIgnoreCase("delete")) {
      doDelete(req, resp);
      return;
    } else if (action.equalsIgnoreCase("put")) {
      doPut(req, resp);

      return;
    }
  }

  /**
   * Insert the order and corresponding line item in a single transaction
   */
  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    logger.log(Level.INFO, "Inserting order into listing");
	String itemName = req.getParameter("itemName");
	String customerName = req.getParameter("customerName");
	String shipTo = req.getParameter("shipto");
	String city = req.getParameter("city");
	String state = req.getParameter("state");
	String zip = req.getParameter("zip");
	String quantity = req.getParameter("quantity");
	String price = req.getParameter("price");

    Entity order = Order.createOrUpdateOrder(customerName, itemName, quantity, price, shipTo, city, state, zip);
    
    // Create Task and push it into Task Queue
    Queue queue = QueueFactory.getQueue("OrderProcessingQueue");
    TaskOptions taskOptions = TaskOptions.Builder.withUrl("/processOrder")
        .param("customerName", customerName)
        .param("orderKey", String.valueOf(order.getKey().getId())).method(Method.POST);
    queue.add(taskOptions);
  }

  /**
   * Delete the order and respective line items also in a single transaction
   */
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    logger.log(Level.INFO, "Deleting order from the listing");
    String orderId = req.getParameter("id");
    String customerName = req.getParameter("parentid");
    
    Transaction txn = datastore.beginTransaction();
    try {
      Key parentKey = KeyFactory.createKey("Customer", customerName);
      Key key = KeyFactory.createKey(parentKey, "Order", Integer.parseInt(orderId));

      // CASCADE_ON_DELETE
      Iterable<Entity> entities = Util.listChildKeys("LineItem", key);
      final List<Key> keys = new ArrayList<Key>();
      for (Entity e : entities) {
        keys.add(e.getKey());
      }
      Util.deleteEntity(keys);
      datastore.delete(key);
      txn.commit();
    } finally {
      if (txn.isActive()) {
        txn.rollback();
      }
    }
  }

  /**
   * Get the requested orders and the line items in JSON format
   */
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	super.doGet(req, resp);
    logger.log(Level.INFO, "Getting orders from listing");
    String searchFor = req.getParameter("q");
    Iterable<Entity> entities = null;
    PrintWriter out = resp.getWriter();
    if (searchFor == null || searchFor.equals("")) {
      entities = Order.getAllOrders();
      out.println(Util.writeJSON(entities, "LineItem", "orderID"));
    } else {
      entities = Order.getAllOrdersForCustomer(searchFor);
      out.println(Util.writeJSON(entities, "LineItem", "orderID"));
    }
  }
}
