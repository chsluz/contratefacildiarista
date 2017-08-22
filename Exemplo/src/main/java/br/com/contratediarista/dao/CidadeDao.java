package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.contratediarista.entity.Cidade;

@RequestScoped
public class CidadeDao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CidadeDao() {
		
	}
	

	@Inject
	EntityManager em;

	GenericDao<Cidade> dao;
	
	@PostConstruct
	void init() {
		dao = new GenericDao<Cidade>(Cidade.class, em);
	}
	
	@SuppressWarnings("unchecked")
	public List<Cidade> listByIdEstado(int id) {
		try {
			String sql = "SELECT c FROM Cidade c WHERE c.estado.id = :id";
			Query query = em.createQuery(sql,Cidade.class);
			query.setParameter("id", id);
			return (List<Cidade>) query.getResultList();
		}  catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public List<Cidade> listAll() {
		try {
			return dao.listarTodos();
		}  catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}


}
