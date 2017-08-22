package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.contratediarista.entity.Pais;

@RequestScoped
public class PaisDao implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public PaisDao() {

	}

	@Inject
	EntityManager em;

	GenericDao<Pais> dao;

	@PostConstruct
	void init() {
		dao = new GenericDao<Pais>(Pais.class, em);
	}

	public List<Pais> listAll() {
		try {
			return dao.listarTodos();
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

}
