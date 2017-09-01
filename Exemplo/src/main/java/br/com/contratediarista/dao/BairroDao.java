package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.contratediarista.entity.Bairro;

@RequestScoped
public class BairroDao implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public BairroDao() {

	}

	@Inject
	EntityManager em;

	GenericDao<Bairro> dao;

	@PostConstruct
	void init() {
		dao = new GenericDao<Bairro>(Bairro.class, em);
	}

	@SuppressWarnings("unchecked")
	public List<Bairro> listByIdCidade(int id) {
		try {
			String sql = "SELECT b FROM Bairro b WHERE b.cidade.id = :id";
			Query query = em.createQuery(sql, Bairro.class);
			query.setParameter("id", id);
			return query.getResultList();
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public Bairro restoreById(int id) {
		return dao.restoreById(id);
	}

	public Bairro restoreByDescricao(String descricao) {
		return dao.restoreByDescricao(descricao);
	}

	public List<Bairro> listAll() {
		try {
			return dao.listarTodos();
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

}
