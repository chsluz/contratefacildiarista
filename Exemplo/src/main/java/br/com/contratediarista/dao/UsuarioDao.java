package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.contratediarista.entity.Usuario;

@SuppressWarnings("unchecked")
@RequestScoped
public class UsuarioDao extends GenericDao<Usuario> implements Serializable {

	public UsuarioDao() {

	}

	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager em;

	GenericDao<Usuario> dao;

	@PostConstruct
	void init() {
		dao = new GenericDao<Usuario>(Usuario.class, em);
	}

	public Usuario buscarUsuarioPorChave(String uid) {
		try {
			String sql = "SELECT u FROM Usuario u WHERE u.uid = :uid";
			Query query = em.createQuery(sql);
			query.setParameter("uid", uid);
			return (Usuario) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Usuario> buscarContatosPrestador(String uid) {
		try {
			StringBuilder sql = new StringBuilder(" SELECT DISTINCT(v.contratante) FROM Rotina r ")
					.append(" JOIN r.vaga v ")
					.append(" WHERE r.prestadorSelecionado.uid = :uid ");
			Query query = em.createQuery(sql.toString(),Usuario.class);
			query.setParameter("uid", uid);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Usuario> buscarContatosContratante(String uid) {
		try {
			StringBuilder sql = new StringBuilder(" SELECT DISTINCT(r.prestadorSelecionado) FROM Rotina r ")
					.append(" JOIN r.vaga v ")
					.append(" WHERE r.prestadorSelecionado IS NOT NULL")
					.append(" AND v.contratante.uid = :uid ");
			Query query = em.createQuery(sql.toString(),Usuario.class);
			query.setParameter("uid", uid);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void salvar(Usuario usuario) throws Exception {
		dao.salvar(usuario);
	}

	@Override
	public void alterar(Usuario usuario) throws Exception {
		dao.alterar(usuario);
	}

}
