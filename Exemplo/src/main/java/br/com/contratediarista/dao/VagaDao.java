package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.contratediarista.entity.TipoAtividade;
import br.com.contratediarista.entity.Vaga;
import br.com.contratediarista.enuns.DiasSemana;
import br.com.contratediarista.enuns.TipoPeriodo;

@RequestScoped
public class VagaDao implements Serializable {

	private static final long serialVersionUID = 1L;

	GenericDao<Vaga> dao;

	@Inject
	EntityManager em;

	public VagaDao() {

	}

	@PostConstruct
	void init() {
		dao = new GenericDao<Vaga>(Vaga.class, em);
	}

	public void salvar(Vaga vaga) throws Exception {
		dao.salvar(vaga);
	}

	public void alterar(Vaga vaga) throws Exception {
		dao.alterar(vaga);
	}

	public void excluir(Vaga vaga) throws Exception {
		dao.excluir(vaga);
	}

	public Vaga restoreById(int id) throws Exception {
		return dao.restoreById(id);
	}

	@SuppressWarnings("unchecked")
	public List<Vaga> buscarVagasPorDataEValor(Date dataInicial, Date dataFinal, int valorInicial, int valorFinal,
			TipoPeriodo periodo, List<TipoAtividade> tiposAtividade, List<DiasSemana> diasSemana) {
		try {
			StringBuilder sql = new StringBuilder(" SELECT v FROM Vaga v ");
			sql.append(" JOIN v.rotinas r ").append(" JOIN v.tiposAtividade at ")
					.append(" WHERE v.valorPeriodo BETWEEN :valorInicial AND :valorFinal ")
					.append(" AND r.data BETWEEN :dataInicial AND :dataFinal ");

			if (periodo != null) {
				sql.append(" AND r.tipoPeriodo = :tipoPeriodo ");
			}
			if (diasSemana != null) {
				sql.append(" AND r.diaSemana IN :diasSemana ");
			}
			if (tiposAtividade != null) {
				sql.append(" AND at IN :tiposAtividade ");
			}
			Query query = em.createQuery(sql.toString(), Vaga.class);
			query.setParameter("valorInicial", valorInicial);
			query.setParameter("valorFinal", valorFinal);
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			if (periodo != null) {
				query.setParameter("tipoPeriodo", periodo);
			}
			if (diasSemana != null) {
				query.setParameter("diasSemana", diasSemana);
			}
			if (tiposAtividade != null) {
				query.setParameter("tiposAtividade", tiposAtividade);
			}
			return (List<Vaga>) query.getResultList();
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

}
