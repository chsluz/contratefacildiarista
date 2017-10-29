package br.com.contratediarista.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.contratediarista.entity.Solicitacao;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.enuns.TipoSolicitacao;
import br.com.contratediarista.enuns.TipoUsuario;
import br.com.contratediarista.service.SolicitacaoService;
import br.com.contratediarista.service.UsuarioService;
import br.com.contratediarista.utils.FacesUtil;

@Named
@ViewScoped
public class CadastrarSolicitacaoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Usuario usuarioLogado;
	private List<Usuario> usuarios;
	private List<TipoSolicitacao> tiposSolicitacao;
	private Solicitacao solicitacao;
	
	@Inject
	private FacesUtil facesUtil;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private SolicitacaoService solicitacaoService;
	
	public CadastrarSolicitacaoBean() {
		
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		solicitacao = new Solicitacao();
		usuarioLogado = facesUtil.getUsuarioLogado();
		tiposSolicitacao = Arrays.asList(TipoSolicitacao.values());
		if(usuarioLogado.getTipoUsuario().equals(TipoUsuario.CONTRATANTE)) {
			Response response = usuarioService.buscarContatosContratante(usuarioLogado.getUid());
			if(response.getStatus() == Status.OK.getStatusCode()) {
				usuarios = (List<Usuario>) response.getEntity();
			}
		}
		else {
			Response response = usuarioService.buscarContatosPrestador(usuarioLogado.getUid());
			if(response.getStatus() == Status.OK.getStatusCode()) {
				usuarios = (List<Usuario>) response.getEntity();
			}
		}
		
	}
	
	public void salvar() {
		try {
			Response response = solicitacaoService.salvar(solicitacao);
			if (response.getStatus() == Status.OK.getStatusCode()) {
				solicitacao = new Solicitacao();
				facesUtil.exibirMsgSucesso(facesUtil.getLabel("salvo.sucesso"));
			} else {
				facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
			}
		} catch (Exception e) {
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
		}
		
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<TipoSolicitacao> getTiposSolicitacao() {
		return tiposSolicitacao;
	}

	public void setTiposSolicitacao(List<TipoSolicitacao> tiposSolicitacao) {
		this.tiposSolicitacao = tiposSolicitacao;
	}

	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}
	
	

}
