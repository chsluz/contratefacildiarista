package br.com.contratediarista.utils;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class FacesUtil implements Serializable {

	/**
	 *
	 */
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

	public String getLabel(String chave) {
		return bundle.getString(chave);
	}

}
