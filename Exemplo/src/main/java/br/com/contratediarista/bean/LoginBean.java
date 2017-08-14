package br.com.contratediarista.bean;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.contratediarista.dao.UsuarioDao;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.service.UsuarioService;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioDao usuarioDao;

	@Inject
	private UsuarioService usuarioService;

	private Usuario usuario;

	@Inject
	FacesContext facesContext;

	private String idUsuario;

	@PostConstruct
	public void init() {
		usuario = new Usuario();
	}

	public void logar() throws IOException {
		usuario = usuarioDao.retornarUsuarioByUid(idUsuario);
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

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

}
