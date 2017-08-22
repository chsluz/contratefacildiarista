package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.contratediarista.entity.Estado;

@RequestScoped
public class EstadoDao implements Serializable{

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
	
	@SuppressWarnings("unchecked")
	public List<Estado> listByIdPais(int  id) {
		try {
			String sql = "SELECT e FROM Estado e WHERE e.pais.id = :id";
			Query query = em.createQuery(sql,Estado.class);
			query.setParameter("id", id);
			return (List<Estado>) query.getResultList();
		}  catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	public List<Estado> listAll() {
		try {
			return dao.listarTodos();
		}  catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	


}
