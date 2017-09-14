package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.contratediarista.entity.TipoAtividade;

@RequestScoped
public class TipoAtividadeDao implements Serializable {

	private static final long serialVersionUID = 1L;

	GenericDao<TipoAtividade> dao;

	@Inject
	EntityManager em;

	public TipoAtividadeDao() {

	}

	@PostConstruct
	void init() {
		dao = new GenericDao<TipoAtividade>(TipoAtividade.class, em);
	}

	public List<TipoAtividade> listarTodos() {
		return dao.listarTodos();
	}

	public void salvar(TipoAtividade tipo) throws Exception {
		dao.salvar(tipo);
	}

	public void alterar(TipoAtividade tipo) throws Exception {
		dao.alterar(tipo);
	}

	public void excluir(TipoAtividade tipo) throws Exception {
		dao.excluir(tipo);
	}

	public TipoAtividade restoreById(int id) throws Exception {
		return dao.restoreById(id);
	}

}
