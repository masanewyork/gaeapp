package my.example.jpa;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.codelab.BaseServlet;
import com.google.appengine.codelab.Util;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class ShippingServlet extends BaseServlet {

	private static final Logger logger = Logger.getLogger(ShippingServlet.class.getCanonicalName());

	/**
	 * Searches for the entity based on the search criteria and returns result in
	 * JSON format
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
		logger.log(Level.INFO, "Obtaining Shipping listing");
		String searchBy = req.getParameter("shipping-searchby");
		String searchFor = req.getParameter("q");
		PrintWriter out = resp.getWriter();
		if (searchFor == null || searchFor.equals("")) {
			Collection<Shipping> entities = Shipping.getAllShipping();
			out.println(writeJSON(entities));
		} else if (searchBy == null || searchBy.equals("itemName")) {
			Collection<Shipping> entities = Shipping.getShippingByItem(searchFor);
			out.println(writeJSON(entities));
		}
	}

	public static String writeJSON(Collection<Shipping> entities) {
		logger.log(Level.INFO, "creating JSON format object");
		StringBuilder sb = new StringBuilder();
		int i = 0;
		sb.append("{\"data\": [");
		for (Shipping result : entities) {
			JSONObject jo = new JSONObject(result, new String[]{"id", "itemName", "vesselName", "quantity"});
			sb.append(jo.toString());
			sb.append(",");
			i++;
		}
		if (i > 0) {
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		sb.append("]}");
		return sb.toString();
	}

	/**
	 * Creates entity and persists the same
	 */
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.log(Level.INFO, "Creating Shipping");
		String itemName = req.getParameter("itemName");
		String vesselName = req.getParameter("vesselName");
		int quantity = Integer.valueOf(req.getParameter("quantity"));
		Shipping bean = new Shipping();
		bean.setItemName(itemName);
		bean.setQuantity(quantity);
		bean.setVesselName(vesselName);
		Shipping.createOrUpdateShipping(bean);
		PrintWriter out = resp.getWriter();
		out.print("Shipping created successfully.");
	}

	/**
	 * Delete the entity from the datastore. Throws an exception if there are any
	 * orders associated with the item and ignores the delete action for it.
	 */
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		String itemKey = req.getParameter("id");
//		PrintWriter out = resp.getWriter();
//		Iterable<Entity> entities = Util.listEntities("LineItem", "itemName", itemKey);
//		try {
//			for (Entity e : entities) {
//				if (e != null)
//					out.print("Cannot delete item as there are orders associated with this item.");
//				return;
//			}
//			Entity e = Item.getSingleItem(itemKey);
//			Util.deleteEntity(e.getKey());
//			out.print("Item deleted successfully.");
//		} catch (Exception e) {
//			String msg = Util.getErrorResponse(e);
//			out.print(msg);
//		}
	}

	/**
	 * Redirects to delete or insert entity based on the action in the HTTP
	 * request.
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
}
