package br.com.exemplo.bean;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.exemplo.dao.UsuarioDao;
import br.com.exemplo.entity.Usuario;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioDao usuarioDao;

	private Usuario usuario;

	@Inject
	FacesContext facesContext;

	@PostConstruct
	public void init() {
		usuario = new Usuario();
	}

	public void logar() throws IOException {
		usuario = usuarioDao.validarLogin(usuario);
		if (usuario != null) {
			facesContext.getExternalContext().getSessionMap().put("usuario", usuario);
			facesContext.getExternalContext().redirect("paginas/index.jsf");
		}
	}

	public void deslogar() throws IOException {
		facesContext.getExternalContext().getSessionMap().put("usuario", null);
		facesContext.getExternalContext().redirect("login.jsf");
	}

	public void salvar() {
		usuarioDao.salvar(usuario);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
