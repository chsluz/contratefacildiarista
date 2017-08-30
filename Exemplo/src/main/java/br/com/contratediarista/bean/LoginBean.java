package br.com.contratediarista.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
import org.primefaces.event.SelectEvent;

import br.com.contratediarista.entity.Bairro;
import br.com.contratediarista.entity.Cidade;
import br.com.contratediarista.entity.Endereco;
import br.com.contratediarista.entity.Estado;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.enuns.TipoUsuario;
import br.com.contratediarista.service.BairroService;
import br.com.contratediarista.service.CidadeService;
import br.com.contratediarista.service.EstadoService;
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
	private EstadoService estadoService;

	@Inject
	private CidadeService cidadeService;

	@Inject
	private BairroService bairroService;

	@Inject
	private FacesContext facesContext;

	@Inject
	private FacesUtil facesUtil;

	private Usuario usuario;

	private String idUsuario;

	private String msgErro;

	private Estado estado;

	private Cidade cidade;

	private Bairro bairro;

	@PostConstruct
	public void init() {
		instanciarNovo();
	}

	public void instanciarNovo() {
		usuario = new Usuario();
		usuario.setEndereco(new Endereco());
		estado = new Estado();
		cidade = new Cidade();
		bairro = new Bairro();
	}

	
	public void selectEstado(SelectEvent event) {
		estado = (Estado) event.getObject();
	}
	
	public void selectCidade(SelectEvent event) {
		cidade = (Cidade) event.getObject();
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
			usuario.setUid(idUsuario);
			usuarioService.salvar(usuario);
			instanciarNovo();
			facesUtil.exibirMsgSucesso("Usuário salvo com sucesso.");
			facesContext.getExternalContext().redirect("login_alterado.jsf");
		} catch (Exception e) {
			RequestContext.getCurrentInstance().execute("excluirLogin();");
			e.printStackTrace();
			facesUtil.exibirMsgErro("erro ao salvar.");
		}
	}
	
	public void cadastrarNovo() throws IOException {
		facesContext.getExternalContext().redirect("cadastro_usuario.jsf");
	}
	
	public void cancelar() throws IOException {
		facesContext.getExternalContext().redirect("../");
	}

	@SuppressWarnings("unchecked")
	public List<Estado> getListarEstados() {
		try {
			Response response = null;
			List<Estado> estados = new ArrayList<>();
			response = estadoService.listAll();
			if (response != null && response.getEntity() != null) {
				estados = (List<Estado>) response.getEntity();
			}
			return estados;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cidade> getListarCidades() {
		try {
			Response response = null;
			List<Cidade> cidades = new ArrayList<>();
			if (estado != null) {
				response = cidadeService.listByIdEstado(estado.getId());
				if (response != null && response.getEntity() != null) {
					cidades = (List<Cidade>) response.getEntity();
				}
			}
			return cidades;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Bairro> getListarBairros() {

		try {
			Response response = null;
			List<Bairro> bairros = new ArrayList<>();
			if (cidade != null) {
				response = bairroService.listByIdCidade(cidade.getId());
				if (response != null && response.getEntity() != null) {
					bairros = (List<Bairro>) response.getEntity();
				}
			}
			return bairros;
		} catch (Exception e) {
			return null;
		}
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

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}

	public String getMsgErro() {
		return msgErro;
	}

	public void setMsgErro(String msgErro) {
		this.msgErro = msgErro;
	}

}
