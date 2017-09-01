package br.com.contratediarista.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.primefaces.context.RequestContext;

import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.enuns.TipoUsuario;
import br.com.contratediarista.service.UsuarioService;
import br.com.contratediarista.utils.FacesUtil;

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

	@Inject
	private BuscaEnderecoBean buscaEnderecoBean;

	@Inject
	private FacesUtil facesUtil;

	private Usuario usuario;

	private String idUsuario;

	private String msgErro;

	@PostConstruct
	public void init() {
		instanciarNovo();
	}

	public void instanciarNovo() {
		usuario = new Usuario();

	}

	public void logar() throws IOException {
		Response response = usuarioService.buscarUsuarioByUid(idUsuario);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			usuario = (Usuario) response.getEntity();
			if (usuario != null) {
				facesContext.getExternalContext().getSessionMap().put("usuario", usuario);
				facesContext.getExternalContext().redirect("index.jsf");
			}
		}
	}

	public void deslogar() throws IOException {
		facesContext.getExternalContext().getSessionMap().put("usuario", null);
		facesContext.getExternalContext().redirect("../");
	}

	public void exibirMensagemErro() {
		facesUtil.exibirMsgErro(msgErro);
	}

	public void salvar() throws Exception {
		try {
			usuario.setEndereco(buscaEnderecoBean.getEndereco());
			usuario.setUid(idUsuario);
			usuarioService.salvar(usuario);
			instanciarNovo();
			facesUtil.exibirMsgSucesso(facesUtil.getLabel("salvo.sucesso"));
			facesContext.getExternalContext().redirect("../");
		} catch (Exception e) {
			RequestContext.getCurrentInstance().execute("excluirLogin();");
			e.printStackTrace();
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
		}
	}

	public void cadastrarNovo() throws IOException {
		facesContext.getExternalContext().redirect("cadastro_usuario.jsf");
	}

	public void cancelar() throws IOException {
		facesContext.getExternalContext().redirect("../");
	}

	public List<TipoUsuario> getTiposUsuario() {
		List<TipoUsuario> tipos = Arrays.asList(TipoUsuario.values());
		return tipos;
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
