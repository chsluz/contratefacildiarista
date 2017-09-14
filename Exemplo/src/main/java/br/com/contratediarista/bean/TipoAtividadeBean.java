package br.com.contratediarista.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.primefaces.model.LazyDataModel;

import br.com.contratediarista.entity.TipoAtividade;
import br.com.contratediarista.lazy.AbstractLazyDataModel;
import br.com.contratediarista.service.TipoAtividadeService;
import br.com.contratediarista.utils.FacesUtil;

@Named
@ViewScoped
public class TipoAtividadeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private LazyDataModel<TipoAtividade> tipoAtividadeLazy;

	private TipoAtividade tipoAtividade;

	@Inject
	private FacesUtil facesUtil;

	@Inject
	private TipoAtividadeService tipoAtividadeService;

	@PostConstruct
	public void init() {
		atualizarDados();
	}

	@SuppressWarnings("unchecked")
	private void atualizarDados() {
		instanciarNovo();
		Response response = tipoAtividadeService.listAll();
		if (response.getStatus() == Status.OK.getStatusCode()) {
			List<TipoAtividade> lista = (List<TipoAtividade>) response.getEntity();
			tipoAtividadeLazy = new AbstractLazyDataModel<TipoAtividade>(lista);
		}
	}

	public void instanciarNovo() {
		tipoAtividade = new TipoAtividade();
	}

	public void salvar() {
		Response response;
		if (tipoAtividade.getId() == 0) {
			response = tipoAtividadeService.salvar(tipoAtividade);
		} else {
			response = tipoAtividadeService.alterar(tipoAtividade);
		}
		if (response.getStatus() == Status.OK.getStatusCode()) {
			facesUtil.exibirMsgSucesso(facesUtil.getLabel("salvo.sucesso"));
			atualizarDados();
		} else {
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
		}
	}

	public void excluir(TipoAtividade tipo) {
		Response restoreById = tipoAtividadeService.restoreById(tipo.getId());
		if (restoreById.getStatus() == Status.OK.getStatusCode()) {
			tipo = (TipoAtividade) restoreById.getEntity();
			Response response = tipoAtividadeService.excluir(tipo);
			if (response.getStatus() == Status.OK.getStatusCode()) {
				facesUtil.exibirMsgSucesso(facesUtil.getLabel("excluido.sucesso"));
				atualizarDados();
			} else {
				facesUtil.exibirMsgErro(facesUtil.getLabel("erro.excluir"));
			}
		} else {
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.excluir"));
		}
	}

	public void cancelar() {
		instanciarNovo();
	}

	public LazyDataModel<TipoAtividade> getTipoAtividadeLazy() {
		return tipoAtividadeLazy;
	}

	public void setTipoAtividadeLazy(LazyDataModel<TipoAtividade> tipoAtividadeLazy) {
		this.tipoAtividadeLazy = tipoAtividadeLazy;
	}

	public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

}
