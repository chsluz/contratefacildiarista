package br.com.contratediarista.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;

public class GenericDao<T> implements Serializable {
	private Class<T> classe;
	private EntityManager em;

	/**
	 *
	 */

	public GenericDao(Class<T> classe, EntityManager em) {
		this.classe = classe;
		this.em = em;
	}

	private static final long serialVersionUID = 1L;

	public void salvar(T t) {
		try {
			em.getTransaction().begin();
			em.persist(t);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
	}

	public void alterar() {
		try {
			em.getTransaction().begin();
			em.persist(classe);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
	}

}
