package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class GenericDao<T> implements Serializable {
	private Class<T> classe;
	private EntityManager em;

	/**
	 *
	 */
	
	public GenericDao() {
		
	}

	public GenericDao(Class<T> classe, EntityManager em) {
		this.classe = classe;
		this.em = em;
	}

	private static final long serialVersionUID = 1L;

	public void salvar(T t) throws Exception {
		try {
			em.getTransaction().begin();
			em.persist(t);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e.getCause());
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> listarTodos() {
		try {
			String sql = "SELECT t FROM " + classe.getName() + " t";
			Query query = em.createQuery(sql, classe);
			return (List<T>) query.getResultList();
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public T restoreById(int id) {
		try {
			String sql = "SELECT t FROM " + classe.getName() + " t WHERE t.id = :id";
			Query query = em.createQuery(sql, classe);
			query.setParameter("id", id);
			return (T) query.getSingleResult();
		}catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public void alterar(T t) {
		try {
			em.getTransaction().begin();
			em.persist(t);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
	}

}
