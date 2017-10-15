package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.contratediarista.entity.Avaliacao;
import br.com.contratediarista.entity.Usuario;

@RequestScoped
public class AvaliacaoDao implements Serializable {

	private static final long serialVersionUID = 1L;

	public AvaliacaoDao() {

	}

	@Inject
	EntityManager em;

	GenericDao<Avaliacao> dao;

	@PostConstruct
	void init() {
		dao = new GenericDao<Avaliacao>(Avaliacao.class, em);
	}

	public Avaliacao restoreById(int id) {
		return dao.restoreById(id);
	}

	public void salvar(Avaliacao avaliacao) throws Exception {
		dao.salvar(avaliacao);
	}

	public void alterar(Avaliacao avaliacao) throws Exception {
		dao.alterar(avaliacao);
	}

	public void excluir(Avaliacao avaliacao) throws Exception {
		dao.excluir(avaliacao);
	}

	public Avaliacao buscarAvaliacaoSalvaPrestadorContratante(Usuario avaliador, Usuario avaliado, Date data)
			throws Exception {
		try {
			StringBuilder sql = new StringBuilder(" SELECT a FROM Avaliacao a ");
			sql.append(" WHERE a.data = :data ").append(" AND a.usuario = :avaliado ")
					.append(" AND a.avaliador = :avaliador ");
			Query query = em.createQuery(sql.toString(), Avaliacao.class);
			query.setParameter("data", data);
			query.setParameter("avaliado", avaliado);
			query.setParameter("avaliador", avaliador);
			return (Avaliacao) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

}
