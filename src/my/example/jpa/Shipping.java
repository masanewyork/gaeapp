package my.example.jpa;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Shipping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String vesselName;
	private String itemName;
	private int quantity;

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public static Shipping createOrUpdateShipping(Shipping shipping) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.persist(shipping);
		} finally {
			em.close();
		}
		return shipping;
	}

	public static Collection<Shipping> getShippingByItem(String itemName) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			Query query = em.createQuery("SELECT e FROM Shipping e where e.itemName = ?1");
			query.setParameter(1, itemName);
			Collection<Shipping> result = query.getResultList();
			result.size();
			return result;
		} finally {
			em.close();
		}
	}

	public static Collection<Shipping> getAllShipping() {
		EntityManager em = EMF.get().createEntityManager();
		try {
			Query query = em.createQuery("SELECT e FROM Shipping e");
			Collection<Shipping> result = query.getResultList();
			result.size();
			return result;
		} finally {
			em.close();
		}
	}
}
