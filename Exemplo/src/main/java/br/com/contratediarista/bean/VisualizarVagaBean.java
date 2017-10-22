package br.com.contratediarista.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.primefaces.context.RequestContext;

import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.service.RotinaService;
import br.com.contratediarista.service.UsuarioService;
import br.com.contratediarista.utils.FacesUtil;

@Named
@ViewScoped
public class VisualizarVagaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	FacesContext facesContext;

	@Inject
	private RotinaService rotinaService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private FacesUtil facesUtil;

	private Rotina rotina;
	private Usuario usuarioLogado;

	public VisualizarVagaBean() {

	}

	@PostConstruct
	public void init() {
		rotina = (Rotina) facesContext.getExternalContext().getSessionMap().get("rotina");
		if (rotina != null) {
			Response response = rotinaService.restoreById(rotina.getId());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				rotina = (Rotina) response.getEntity();
				RequestContext.getCurrentInstance().execute("iniciarMapaVisualizacao()");
			}
		}
		Response response = usuarioService.buscarUsuarioByUid(facesUtil.getUsuarioLogado().getUid());
		if (response.getStatus() == Status.OK.getStatusCode()) {
			usuarioLogado = (Usuario) response.getEntity();
		}
	}

	public void canditatar() {
		try {
			List<Usuario> usuarios = rotina.getPrestadores();
			if (usuarioLogado == null) {
				facesUtil.exibirMsgErro(facesUtil.getLabel("erro.ao.vincular.usuario.vaga"));
			}
			if (usuarios == null) {
				usuarios = new ArrayList<>();
			}
			if (rotina.getPrestadores().size() > 10) {
				facesUtil.exibirMsgErro(facesUtil.getLabel("numero.maximo.candidatos.ja.preenchido"));
				return;
			}
			rotina = recuperarEntidade(rotina);
			if (!usuarios.contains(usuarioLogado)) {
				usuarios.add(usuarioLogado);
				rotina.setPrestadores(new HashSet<>(usuarios));
				Response response = rotinaService.alterar(rotina);
				if (response.getStatus() == Status.OK.getStatusCode()) {
					facesUtil.exibirMsgSucesso(facesUtil.getLabel("salvo.sucesso"));
				} else {
					facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
				}
				RequestContext.getCurrentInstance().execute("iniciarMapaVisualizacao()");
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
		}
	}

	public boolean renderizarBotaoCandidatar() {
		if ((rotina.getPrestadores() != null && rotina.getPrestadores().contains(usuarioLogado))
				|| (rotina.getPrestadorSelecionado() != null
						&& rotina.getPrestadorSelecionado().equals(usuarioLogado))) {
			return false;
		} else {
			return true;
		}
	}

	
	public Rotina recuperarEntidade(Rotina rotina) {
		Response response = rotinaService.restoreById(rotina.getId());
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return (Rotina) response.getEntity();
		} else {
			return null;
		}
	}
	
	public Rotina getRotina() {
		return rotina;
	}

	public void setRotina(Rotina rotina) {
		this.rotina = rotina;
	}

}
