package br.com.contratediarista.dao;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.contratediarista.entity.Usuario;

@RequestScoped
public class UsuarioDao extends GenericDao<Usuario> implements Serializable {

	public UsuarioDao() {

	}

	private static final long serialVersionUID = 1L;

	@Inject
	static EntityManager em;

	GenericDao<Usuario> dao;

	@PostConstruct
	void init() {
		dao = new GenericDao<Usuario>(Usuario.class, em);
	}

	/**
	 *
	 */

//	public Usuario validarLogin(Usuario usuario) {
//		try {
//			String sql = "SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha";
//			Query query = em.createQuery(sql);
//			query.setParameter("email", usuario.getEmail());
//			query.setParameter("senha", usuario.getSenha());
//			return (Usuario) query.getSingleResult();
//		} catch (NoResultException e) {
//			return null;
//		} catch (Exception e) {
//			System.out.println(e);
//			return null;
//		}
//	}

	@Override
	public void salvar(Usuario usuario) throws Exception {
		dao.salvar(usuario);
	}

	@Override
	public void alterar(Usuario usuario) {
		dao.alterar(usuario);
	}

}
