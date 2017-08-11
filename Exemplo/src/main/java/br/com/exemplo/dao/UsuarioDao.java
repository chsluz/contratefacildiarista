package br.com.exemplo.dao;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.exemplo.entity.Usuario;

@RequestScoped
public class UsuarioDao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager em;

	GenericDao<Usuario> dao;

	@PostConstruct
	void init() {
		dao = new GenericDao<Usuario>(Usuario.class, em);
	}

	/**
	 *
	 */

	public Usuario validarLogin(Usuario usuario) {
		try {
			String sql = "SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha";
			Query query = em.createQuery(sql);
			query.setParameter("login", usuario.getLogin());
			query.setParameter("senha", usuario.getSenha());
			return (Usuario) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public void salvar(Usuario usuario) {
		dao.salvar(usuario);
	}

}
