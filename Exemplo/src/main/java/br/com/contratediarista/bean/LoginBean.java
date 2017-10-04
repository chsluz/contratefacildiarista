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
import br.com.contratediarista.utils.Utils;

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

	private Usuario usuarioLogin;

	private String idUsuario;

	private String msgErro;

	@PostConstruct
	public void init() {
		instanciarNovo();
	}

	public void instanciarNovo() {
		usuario = new Usuario();
		usuarioLogin = new Usuario();

	}

	public void logar() throws IOException {
		Response response = usuarioService.buscarUsuarioByUid(idUsuario);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			usuarioLogin = (Usuario) response.getEntity();
			if (usuarioLogin != null) {
				facesContext.getExternalContext().getSessionMap().put("usuario", usuarioLogin);
				facesContext.getExternalContext().redirect("pagina_inicial.jsf");
			}
		}
	}

	public void deslogar() throws IOException {
		facesContext.getExternalContext().getSessionMap().put("usuario", null);
		facesContext.getExternalContext().invalidateSession();
		facesContext.getExternalContext().redirect("../publico/login.jsf");
	}

	public void exibirMensagemErro() {
		facesUtil.exibirMsgErro(msgErro);
	}

	public void salvar() throws Exception {
		try {
			String cpf = new String(usuario.getCpf());
			cpf = Utils.removerAcentos(cpf);
			if (!Utils.validarCpf(cpf)) {
				RequestContext.getCurrentInstance().execute("excluirLogin();");
				facesUtil.exibirMsgErro(facesUtil.getLabel("cpf.invalido"));
				return;
			}
			usuario.setEndereco(buscaEnderecoBean.getEndereco());
			usuario.setUid(idUsuario);
			usuarioService.salvar(usuario);
			instanciarNovo();
			facesUtil.exibirMsgSucesso(facesUtil.getLabel("salvo.sucesso"));
			facesContext.getExternalContext().redirect("login.jsf");
		} catch (Exception e) {
			RequestContext.getCurrentInstance().execute("excluirLogin();");
			e.printStackTrace();
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
		}
	}

	public void cadastrarNovo() throws IOException {
		usuario = new Usuario();
		facesContext.getExternalContext().redirect("cadastro_usuario.jsf");
	}

	public void cancelar() throws IOException {
		usuario = new Usuario();
		facesContext.getExternalContext().redirect("login.jsf");
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

	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}

	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}

}
