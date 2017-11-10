package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.contratediarista.entity.Disponibilidade;
import br.com.contratediarista.entity.Usuario;

@SuppressWarnings("unchecked")
@RequestScoped
public class DisponibilidadeDao implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DisponibilidadeDao() {

	}

	@Inject
	EntityManager em;

	GenericDao<Disponibilidade> dao;

	@PostConstruct
	void init() {
		dao = new GenericDao<Disponibilidade>(Disponibilidade.class, em);
	}

	public Disponibilidade restoreById(int id) {
		return dao.restoreById(id);
	}

	public void salvar(Disponibilidade disponibilidade) throws Exception {
		dao.salvar(disponibilidade);
	}

	public void excluir(Disponibilidade disponibilidade) throws Exception {
		dao.excluir(disponibilidade);
	}

	public List<Disponibilidade> buscarDisponibilidadePorUsuarioEData(Usuario usuario, Date dataInicial,
			Date dataFinal) {
		try {
			StringBuilder sql = new StringBuilder(" SELECT d FROM Disponibilidade d ");
			sql.append(" WHERE d.data BETWEEN :dataInicial AND :dataFinal ").append(" AND d.prestador = :usuario ");
			sql.append(" ORDER BY d.data ");
			Query query = em.createQuery(sql.toString(), Disponibilidade.class);
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			query.setParameter("usuario", usuario);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Disponibilidade> buscarDisponibilidades(Date dataInicial, Date dataFinal, int valorInicial,
			int valorFinal) {
		try {
			StringBuilder sql = new StringBuilder(" SELECT d FROM Disponibilidade d ");
			sql.append(" WHERE d.data BETWEEN :dataInicial AND :dataFinal ")
					.append(" AND d.valorPeriodo BETWEEN :valorInicial AND :valorFinal ");
			Query query = em.createQuery(sql.toString(), Disponibilidade.class);
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			query.setParameter("valorInicial", valorInicial);
			query.setParameter("valorFinal", valorFinal);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
