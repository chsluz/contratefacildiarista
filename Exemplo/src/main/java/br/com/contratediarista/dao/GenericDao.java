package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@SuppressWarnings("unchecked")
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
			e.printStackTrace();
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new Exception(e.getCause());
		}
	}

	public List<T> listarTodos() {
		try {
			String sql = "SELECT t FROM " + classe.getName() + " t";
			Query query = em.createQuery(sql, classe);
			return query.getResultList();
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public T restoreById(int id) {
		try {
			String sql = "SELECT t FROM " + classe.getName() + " t WHERE t.id = :id";
			Query query = em.createQuery(sql, classe);
			query.setParameter("id", id);
			return (T) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public T restoreByNome(String nome) {
		try {
			String sql = "SELECT t FROM " + classe.getName() + " t WHERE t.nome = :nome";
			Query query = em.createQuery(sql, classe);
			query.setParameter("nome", nome);
			query.setMaxResults(1);
			return (T) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public T restoreByDescricao(String descricao) {
		try {
			String sql = "SELECT t FROM " + classe.getName() + " t WHERE t.descricao = :descricao";
			Query query = em.createQuery(sql, classe);
			query.setParameter("descricao", descricao);
			query.setMaxResults(1);
			return (T) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void alterar(T t) throws Exception {
		try {
			em.getTransaction().begin();
			em.merge(t);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new Exception(e.getCause());
		}
	}

	public void excluir(T t) throws Exception {
		try {
			em.getTransaction().begin();
			em.remove(t);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new Exception(e.getCause());
		}
	}

}
