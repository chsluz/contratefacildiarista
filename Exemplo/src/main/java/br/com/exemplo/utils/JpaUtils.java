package br.com.exemplo.utils;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class JpaUtils implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	final String PROVIDER = "testeCdiUnit";

	EntityManagerFactory emf = Persistence.createEntityManagerFactory(PROVIDER);

	@Produces
	@RequestScoped
	public EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	public void fecharEntityManager(@Disposes EntityManager entityManager) {
		if (entityManager != null) {
			entityManager.close();
		}
	}

}
