package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.contratediarista.entity.Estado;

@RequestScoped
public class EstadoDao implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public EstadoDao() {

	}

	@Inject
	EntityManager em;

	GenericDao<Estado> dao;

	@PostConstruct
	void init() {
		dao = new GenericDao<Estado>(Estado.class, em);
	}

	public Estado restoreBySigla(String sigla) {
		try {
			String sql = "SELECT e FROM Estado e WHERE e.sigla = :sigla";
			Query query = em.createQuery(sql, Estado.class);
			query.setParameter("sigla", sigla);
			query.setMaxResults(1);
			return (Estado) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Estado> listAll() {
		try {
			return dao.listarTodos();
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

}
