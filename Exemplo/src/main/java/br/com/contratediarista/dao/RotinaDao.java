package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.Usuario;

@SuppressWarnings("unchecked")
@RequestScoped
public class RotinaDao implements Serializable {

	private static final long serialVersionUID = 1L;

	public RotinaDao() {
	}

	@Inject
	EntityManager em;

	GenericDao<Rotina> dao;

	@PostConstruct
	void init() {
		dao = new GenericDao<Rotina>(Rotina.class, em);
	}

	public Rotina restoreById(int id) {
		return dao.restoreById(id);
	}

	public void salvar(Rotina rotina) throws Exception {
		dao.salvar(rotina);
	}

	public void alterar(Rotina rotina) throws Exception {
		dao.alterar(rotina);
	}

	public void excluir(Rotina rotina) throws Exception {
		dao.excluir(rotina);
	}

	public List<Rotina> buscarVagasPorUsuario(Usuario usuario, Date dataInicial, Date dataFinal) {
		try {
			StringBuilder sql = new StringBuilder(" SELECT r FROM Rotina r ");
			sql.append(" WHERE r.data BETWEEN :dataInicial AND :dataFinal ")
					.append(" AND r.prestadorSelecionado = :usuario ");
			Query query = em.createQuery(sql.toString(), Rotina.class);
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			query.setParameter("usuario", usuario);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Rotina> listarRotinasPorDataEUsuario(Date dataInicial, Date dataFinal, Usuario usuario) {
		try {
			StringBuilder sql = new StringBuilder(" SELECT r FROM  Rotina r ");
			sql.append(" JOIN r.vaga v ").append(" WHERE r.data BETWEEN :dataInicial AND :dataFinal ")
					.append(" AND v.contratante = :usuario ");
			Query query = em.createQuery(sql.toString(), Rotina.class);
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			query.setParameter("usuario", usuario);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Rotina> listarRotinasVinculadasPorDataEUsuario(Date dataIni, Date dataFin, Usuario usuario) {
		try {
			StringBuilder sql = new StringBuilder(" SELECT r FROM  Rotina r ");
			sql.append(" JOIN r.vaga v ").append(" WHERE r.data BETWEEN :dataInicial AND :dataFinal ")
					.append(" AND v.contratante = :usuario ")
					.append(" AND r.prestadorSelecionado IS NOT NULL ");
			Query query = em.createQuery(sql.toString(), Rotina.class);
			query.setParameter("dataInicial", dataIni);
			query.setParameter("dataFinal", dataFin);
			query.setParameter("usuario", usuario);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
