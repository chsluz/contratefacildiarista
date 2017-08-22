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

import br.com.contratediarista.entity.Bairro;
import br.com.contratediarista.entity.Cidade;
import br.com.contratediarista.entity.Endereco;
import br.com.contratediarista.entity.Estado;
import br.com.contratediarista.entity.Pais;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.enuns.TipoUsuario;
import br.com.contratediarista.service.BairroService;
import br.com.contratediarista.service.CidadeService;
import br.com.contratediarista.service.EstadoService;
import br.com.contratediarista.service.PaisService;
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
	private EstadoService estadoService;

	@Inject
	private CidadeService cidadeService;

	@Inject
	private PaisService paisService;

	@Inject
	private BairroService bairroService;

	@Inject
	private FacesContext facesContext;

	private Usuario usuario;

	private String idUsuario;

	private String msgErro;

	private Pais pais;

	private Estado estado;

	private Cidade cidade;

	private Bairro bairro;

	@PostConstruct
	public void init() {
		usuario = new Usuario();
		usuario.setEndereco(new Endereco());
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
		facesContext.getExternalContext().redirect("paginas/login.jsf");
	}

	public void exibirMensagemErro() {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, msgErro, msgErro);
		facesContext.addMessage(null, facesMessage);
	}

	@SuppressWarnings("unchecked")
	public List<Pais> getListarPais() {
		Response response = null;
		try {
			response = paisService.listAll();
			List<Pais> paises = (List<Pais>) response.getEntity();
			return paises;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Estado> getListarEstados() {
		Response response = null;
		try {
			if (pais != null) {
				response = estadoService.listByIdPais(pais.getId());
			} else {
				response = estadoService.listAll();
			}
			List<Estado> estados = (List<Estado>) response.getEntity();
			return estados;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cidade> getListarCidades() {
		Response response = null;
		try {
			if (estado != null) {
				response = cidadeService.listByIdEstado(estado.getId());
			} else {
				response = cidadeService.listAll();
			}
			List<Cidade> cidades = (List<Cidade>) response.getEntity();
			return cidades;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Bairro> getListarBairros() {
		Response response = null;
		try {
			if (cidade != null) {
				response = bairroService.listByIdCidade(cidade.getId());
			} else {
				response = bairroService.listAll();
			}
			List<Bairro> bairros = (List<Bairro>) response.getEntity();
			return bairros;
		} catch (Exception e) {
			return null;
		}
	}

	public void salvar() {
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

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
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

}
