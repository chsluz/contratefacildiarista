package br.com.exemplo.utils;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

public class FacesContextUtils implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Produces
	@RequestScoped
	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

}
