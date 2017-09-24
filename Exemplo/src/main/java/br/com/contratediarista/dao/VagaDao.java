package br.com.contratediarista.dao;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.contratediarista.entity.Vaga;

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

}
