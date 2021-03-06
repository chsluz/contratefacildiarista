package br.com.contratediarista.utils;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.service.UsuarioService;

@Named
@ViewScoped
public class FacesUtil implements Serializable {

	/**
	 *
	 */
	@Inject
	private UsuarioService usuarioService;
	
	private static final long serialVersionUID = 1L;

	protected ResourceBundle bundle = ResourceBundle.getBundle("mensagem");

	@Inject
	private FacesContext facesContext;

	public void exibirMsgSucesso(String msg) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
		facesContext.addMessage(null, facesMessage);
	}

	public void exibirMsgErro(String msg) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
		facesContext.addMessage(null, facesMessage);
	}
	
	public void exibirMsgWarning(String msg) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
		facesContext.addMessage(null, facesMessage);
	}

	public String getLabel(String chave) {
		return bundle.getString(chave);
	}
	
	public Usuario getUsuarioLogado() {
		Usuario usuario = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuario");
		if(usuario != null) {
			Response response = usuarioService.buscarUsuarioByUid(usuario.getUid());
			if(response.getStatus() == Status.OK.getStatusCode()) {
				return (Usuario) response.getEntity();
			}
		}
		return null;
	}

}
