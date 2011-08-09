package my.example;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class KMLParser {
	private static final Logger logger = Logger.getLogger(KMLParser.class.getCanonicalName());

	public static void main(String argv[]) {
		File file = new File("C:\\projects\\GAETestApp\\MarineTraffic.kmz");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			List<VesselPosition> vesselPositions = (new KMLParser()).parse(fis);
			for (VesselPosition vessel : vesselPositions) {
				System.out.println(vessel.getName() + ":" + vessel.getLatitude() + ":" + vessel.getLongitude());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private List<VesselPosition> vesselPositions = new ArrayList();

	public List<VesselPosition> parse(InputStream kmzAsStream) {

		try {
			byte[] xmlDocument = new byte[0];
			byte[] buf = new byte[1024];
			ZipInputStream zipinputstream = new ZipInputStream(kmzAsStream);
			ZipEntry zipentry;
			zipentry = zipinputstream.getNextEntry();
			while (zipentry != null) {
				String entryName = zipentry.getName();
				logger.log(Level.INFO, entryName);
				if (!"doc.kml".equals(entryName)) {
					zipentry = zipinputstream.getNextEntry();
					continue;
				}
				// fetch doc.kml
				int n;
				ByteArrayOutputStream out = new ByteArrayOutputStream((int) zipentry.getSize());
				while ((n = zipinputstream.read(buf, 0, 1024)) > -1)
					out.write(buf, 0, n);

				xmlDocument = out.toByteArray();
				out.close();
				zipinputstream.closeEntry();
				break;
			}
			zipinputstream.close();

			InputSource kmlDocument = new InputSource(new ByteArrayInputStream(xmlDocument));
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(kmlDocument);
			doc.getDocumentElement().normalize();
//			System.out.println("Root element " + doc.getDocumentElement().getNodeName());
			/*
			<Placemark>
		<name>Eiffel Tower</name>
		<description>
			Located in Paris, France.

			This description balloon opens
			when the Placemark is loaded.
		</description>
		<gx:balloonVisibility>1</gx:balloonVisibility>
		<Point>
		  <coordinates>2.294785,48.858093,0</coordinates>
		</Point>
	  </Placemark>
	  */
			NodeList nodeLst = doc.getDocumentElement().getChildNodes();
			collectPlacemarks(nodeLst);
//			System.out.println(vesselPositions.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vesselPositions;
	}

	private void collectPlacemarks(NodeList nodeLst) {
		if (nodeLst == null) {
			return;
		}
		for (int s = 0; s < nodeLst.getLength(); s++) {
			Node aNode = nodeLst.item(s);
			if ("Placemark".equals(aNode.getNodeName())) {
				NodeList children = aNode.getChildNodes();
				VesselPosition vessel = new VesselPosition();
				for (int n = 0; n < children.getLength(); n++) {
					Node childNode = children.item(n);
					if ("name".equals(childNode.getNodeName())) {
						vessel.setName(childNode.getFirstChild().getNodeValue());
					}
					if ("description".equals(childNode.getNodeName())) {
						vessel.setDescription(childNode.getFirstChild().getNodeValue());
					}
					if ("Point".equals(childNode.getNodeName())) {
						Node coordinates = childNode.getFirstChild();
						if (coordinates != null) {
							String[] coordinatesText = coordinates.getFirstChild().getNodeValue().split(",");
							vessel.setLatitude(Double.valueOf(coordinatesText[0]));
							vessel.setLongitude(Double.valueOf(coordinatesText[1]));
						}
					}
				}
				vesselPositions.add(vessel);
			} else if (aNode.getChildNodes() != null) {
				collectPlacemarks(aNode.getChildNodes());
			}
		}
	}

	private void traverseAndShowNode(NodeList nodeLst) {
		if (nodeLst == null) {
			return;
		}
		for (int s = 0; s < nodeLst.getLength(); s++) {
			Node fstNode = nodeLst.item(s);
			System.out.println(fstNode.getNodeName());
			if (fstNode.getChildNodes() != null) {
				traverseAndShowNode(fstNode.getChildNodes());
			}
		}
	}
}
