package br.com.contratediarista.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.contratediarista.dao.SolicitacaoDao;
import br.com.contratediarista.dao.UsuarioDao;
import br.com.contratediarista.entity.Solicitacao;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.enuns.Ativo;
import br.com.contratediarista.service.SolicitacaoService;
import br.com.contratediarista.utils.FacesUtil;

@Named
@ViewScoped
public class ConsultarSolicitacaoBean implements Serializable{
	private List<Solicitacao> solicitacoes;

	private static final long serialVersionUID = 1L;
	
	@Inject
	private SolicitacaoService solicitacaoService;
	
	@Inject 
	private SolicitacaoDao solicitacaoDao;
	
	@Inject
	private UsuarioDao UsuarioDao;
	
	@Inject
	private FacesUtil facesUtil;
	
	public ConsultarSolicitacaoBean() {
		
	}
	
	@PostConstruct
	public void init() {
		instanciarNovo();
	}
	
	@SuppressWarnings("unchecked")
	public void instanciarNovo() {
		Response response = solicitacaoService.listAll();
		if(response.getStatus() == Status.OK.getStatusCode()) {
			solicitacoes = (List<Solicitacao>) response.getEntity();
		}
	}

	public List<Solicitacao> getSolicitacoes() {
		return solicitacoes;
	}

	public void setSolicitacoes(List<Solicitacao> solicitacoes) {
		this.solicitacoes = solicitacoes;
	}

	
	public void bloquearUsuario(Solicitacao solicitacao) {
		try {
			solicitacao = solicitacaoDao.restoreById(solicitacao.getId());
			Usuario usuario = UsuarioDao.buscarUsuarioPorChave(solicitacao.getUsuario().getUid());
			usuario.setAtivo(Ativo.NAO);
			UsuarioDao.alterar(usuario);
			facesUtil.exibirMsgSucesso("Usuário bloqueado com sucesso.");
			instanciarNovo();
		} catch (Exception e) {
			facesUtil.exibirMsgErro("Erro ao bloquear usuário");
		}
		
	}
	
	public void excluir(Solicitacao solicitacao) {
		try {
			solicitacao = solicitacaoDao.restoreById(solicitacao.getId());
			Response response = solicitacaoService.excluir(solicitacao);
			if (response.getStatus() == Status.OK.getStatusCode()) {
				facesUtil.exibirMsgSucesso(facesUtil.getLabel("salvo.sucesso"));
			} else {
				facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
			}
			instanciarNovo();
		} catch (Exception e) {
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
		}
	}

}
