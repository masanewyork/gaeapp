package my.example;

import com.google.appengine.api.datastore.*;
import com.google.appengine.codelab.Util;

public class VesselPosition {
	private String name;
	private String description;
	private double latitude;
	private double longitude;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Update the vesselPoistion
	 *
	 * @param bean:     VesselPosition instance to update
	 * @return updated product
	 */
	public static void createOrUpdateVesselPosition(VesselPosition bean) {
		String name = bean.getName();
		Double latitude = bean.getLatitude();
		Double longitude = bean.getLongitude();
		Entity vesselPosition = getVesselPosition(name);
		if (vesselPosition == null) {
			vesselPosition = new Entity("VesselPosition", name);
			vesselPosition.setProperty("latitude", latitude);
			vesselPosition.setProperty("longitude", longitude);
		} else {
			vesselPosition.setProperty("latitude", latitude);
			vesselPosition.setProperty("longitude", longitude);
		}
		Util.persistEntity(vesselPosition);
	}

	/**
	 * Return all the vesselPositions
	 *
	 * @param kind : of kind vesselPosition
	 * @return vesselPositions
	 */
	public static Iterable<Entity> getAllVesselPositions(String kind) {
		return Util.listEntities(kind, null, null);
	}

	/**
	 * Get product entity
	 *
	 * @param name : name of the vesselPosition
	 * @return: vesselPosition entity
	 */
	public static Entity getVesselPosition(String name) {
		Key key = KeyFactory.createKey("VesselPosition", name);
		return Util.findEntity(key);
	}

}
