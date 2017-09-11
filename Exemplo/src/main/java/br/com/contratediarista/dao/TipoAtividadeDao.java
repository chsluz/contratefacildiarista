package br.com.contratediarista.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.contratediarista.entity.TipoAtividade;

@RequestScoped
public class TipoAtividadeDao implements Serializable{

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


}
