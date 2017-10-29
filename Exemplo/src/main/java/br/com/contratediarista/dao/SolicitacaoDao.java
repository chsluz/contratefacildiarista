package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.contratediarista.entity.Solicitacao;

@RequestScoped
public class SolicitacaoDao implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public SolicitacaoDao() {

	}

	@Inject
	EntityManager em;

	GenericDao<Solicitacao> dao;

	@PostConstruct
	void init() {
		dao = new GenericDao<Solicitacao>(Solicitacao.class, em);
	}

	public Solicitacao restoreById(int id) {
		return dao.restoreById(id);
	}
	
	public void salvar(Solicitacao solicitacao) throws Exception {
		dao.salvar(solicitacao);
	}
	
	public void excluir(Solicitacao solicitacao) throws Exception {
		dao.excluir(solicitacao);
	}

	public List<Solicitacao> listAll() {
		try {
			return dao.listarTodos();
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

}
