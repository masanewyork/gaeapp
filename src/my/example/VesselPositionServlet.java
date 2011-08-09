package my.example;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.codelab.BaseServlet;
import com.google.appengine.codelab.Util;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VesselPositionServlet extends BaseServlet {
	private static final Logger logger = Logger.getLogger(VesselPositionServlet.class.getCanonicalName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String command = req.getParameter("command");
		try {
			PrintWriter out = resp.getWriter();
			if ("show".equals(command)) {
				Iterable<Entity> entities = VesselPosition.getAllVesselPositions("VesselPosition");
				out.println(Util.writeJSON(entities));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String command = req.getParameter("command");
		if ("load".equals(command)) {
			try {
//			URL url = new URL("http://www.google.com");
				URL url = new URL("http://marinetraffic2.aegean.gr/ais/getkml.aspx");
				URLConnection conn = url.openConnection();
				conn.setConnectTimeout(100000);
				ByteArrayInputStream kmzAsStream = (ByteArrayInputStream) conn.getContent();
				List<VesselPosition> vesselPositions = (new KMLParser()).parse(kmzAsStream);
				for (VesselPosition vesselPosition : vesselPositions) {
					logger.log(Level.INFO, "Updating.." + vesselPosition.getName());
					VesselPosition.createOrUpdateVesselPosition(vesselPosition);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
