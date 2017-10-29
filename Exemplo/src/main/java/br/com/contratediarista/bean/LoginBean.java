package br.com.contratediarista.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.primefaces.context.RequestContext;

import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.enuns.Ativo;
import br.com.contratediarista.enuns.TipoUsuario;
import br.com.contratediarista.service.UsuarioService;
import br.com.contratediarista.utils.FacesUtil;
import br.com.contratediarista.utils.Utils;

@Named
@ViewScoped
public class LoginBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioService usuarioService;

	private boolean isFacebook;

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

	public LoginBean() {
	}

	@PostConstruct
	public void init() {
		Boolean login = (Boolean) facesContext.getExternalContext().getSessionMap().get("isFacebook");
		if (login != null) {
			isFacebook = login;
			idUsuario = (String) facesContext.getExternalContext().getSessionMap().get("uidUsuario");
		}
		instanciarNovo();
	}

	public void instanciarNovo() {
		usuario = new Usuario();
		usuarioLogin = new Usuario();
	}

	public void logarFacebook() throws IOException {
		Usuario userFacebook = null;
		isFacebook = true;
		Response response = usuarioService.buscarUsuarioByUid(idUsuario);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			userFacebook = (Usuario) response.getEntity();
		}
		if (userFacebook == null) {
			facesContext.getExternalContext().redirect("cadastro_usuario.jsf");
			facesContext.getExternalContext().getSessionMap().put("isFacebook", true);
			facesContext.getExternalContext().getSessionMap().put("uidUsuario", idUsuario);
		} else {
			logar();
		}
	}

	public String getUsuarioLogado() {
		Usuario usuario = facesUtil.getUsuarioLogado();
		if (usuario != null) {
			return "Usuário: " + usuario.getNome();
		}
		return "";
	}

	public void logar() throws IOException {
		facesContext.getExternalContext().getSessionMap().put("isFacebook", false);
		Response response = usuarioService.buscarUsuarioByUid(idUsuario);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			usuarioLogin = (Usuario) response.getEntity();

			if (usuarioLogin != null) {
				if (usuarioLogin.getAtivo() == Ativo.NAO) {
					RequestContext.getCurrentInstance().execute("deslogarUsuario()");
					facesContext.getExternalContext().redirect("usuario_bloqueado.jsf");
					facesContext.getExternalContext().getSessionMap().put("usuario", usuarioLogin);
				} else {
					facesContext.getExternalContext().redirect("");
					facesContext.getExternalContext().getSessionMap().put("usuario", usuarioLogin);
				}

			} else {
				facesUtil.exibirMsgErro(facesUtil.getLabel("usuario.nao.cadastrado"));
				RequestContext.getCurrentInstance().execute("excluirUsuarioFirebaseLogin()");
			}
		}
	}

	public void deslogar() throws IOException {
		facesContext.getExternalContext().getSessionMap().put("usuario", null);
		facesContext.getExternalContext().invalidateSession();
		facesContext.getExternalContext().redirect("../publico/login.jsf");
		instanciarNovo();
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
			if (idUsuario.equals("")) {
				throw new Exception("Id usuário vazio");
			}
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

	public Usuario verificarUsuarioJaExistente(String uid) {
		Response response = usuarioService.buscarUsuarioByUid(uid);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return (Usuario) response.getEntity();
		} else {
			return null;
		}
	}

	public void cadastrarNovo() throws IOException {
		instanciarNovo();
		facesContext.getExternalContext().getSessionMap().put("isFacebook", false);
		facesContext.getExternalContext().redirect("cadastro_usuario.jsf");
	}

	public void cancelar() throws IOException {
		instanciarNovo();
		facesContext.getExternalContext().redirect("login.jsf");
	}

	public List<TipoUsuario> getTiposUsuario() {
		List<TipoUsuario> tipos = new ArrayList<>();
		tipos.add(TipoUsuario.CONTRATANTE);
		tipos.add(TipoUsuario.PRESTADOR);
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

	public boolean isFacebook() {
		return isFacebook;
	}

	public void setFacebook(boolean isFacebook) {
		this.isFacebook = isFacebook;
	}

}
