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

@Named
@ViewScoped
public class TipoAtividadeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private LazyDataModel<TipoAtividade> tipoAtividadeLazy;

	@Inject
	private TipoAtividadeService tipoAtividadeService;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		Response response = tipoAtividadeService.listAll();
		if (response.getStatus() == Status.OK.getStatusCode()) {
			List<TipoAtividade> lista = (List<TipoAtividade>) response.getEntity();
			tipoAtividadeLazy = new AbstractLazyDataModel<TipoAtividade>(lista);
		}
	}

	public LazyDataModel<TipoAtividade> getTipoAtividadeLazy() {
		return tipoAtividadeLazy;
	}

	public void setTipoAtividadeLazy(LazyDataModel<TipoAtividade> tipoAtividadeLazy) {
		this.tipoAtividadeLazy = tipoAtividadeLazy;
	}

}
