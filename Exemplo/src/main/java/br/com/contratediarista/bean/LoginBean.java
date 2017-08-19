package br.com.contratediarista.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.enuns.TipoUsuario;
import br.com.contratediarista.service.UsuarioService;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private FacesContext facesContext;

	private Usuario usuario;

	private String idUsuario;

	private String msgErro;

	@PostConstruct
	public void init() {
		usuario = new Usuario();
	}

	public void logar() throws IOException {
		Response response = usuarioService.retornarUsuarioByUid(idUsuario);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			usuario = (Usuario) response.getEntity();
			if (usuario != null) {
				facesContext.getExternalContext().getSessionMap().put("usuario", usuario);
				facesContext.getExternalContext().redirect("paginas/index.jsf");
			}
		}
	}

	public void deslogar() throws IOException {
		facesContext.getExternalContext().getSessionMap().put("usuario", null);
		facesContext.getExternalContext().redirect("login.jsf");
	}

	public void exibirMensagemErro() {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, msgErro, msgErro);
		facesContext.addMessage(null, facesMessage);
	}

	public void salvar() {
		System.out.println("salvou");
		usuario.setUid(idUsuario);
		usuarioService.salvar(usuario);
	}

	public List<TipoUsuario> getTiposUsuario() {
		List<TipoUsuario> tipos = Arrays.asList(TipoUsuario.values());
		return tipos;
	}

	public void cadastrarNovo() {

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

	public String getMsgErro() {
		return msgErro;
	}

	public void setMsgErro(String msgErro) {
		this.msgErro = msgErro;
	}

}
