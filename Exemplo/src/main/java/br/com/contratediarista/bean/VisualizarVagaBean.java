package br.com.contratediarista.bean;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
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
@RequestScoped
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
			if (usuarioLogado == null) {
				facesUtil.exibirMsgErro(facesUtil.getLabel("erro.ao.vincular.usuario.vaga"));
			}
			if (rotina.getPrestadores() == null) {
				rotina.setPrestadores(new ArrayList<>());
			}
			if (!rotina.getPrestadores().contains(usuarioLogado)) {
				rotina.getPrestadores().add(usuarioLogado);
				Response response = rotinaService.salvar(rotina);
				if (response.getStatus() == Status.OK.getStatusCode()) {
					facesUtil.exibirMsgSucesso(facesUtil.getLabel("salvo.sucesso"));
				} else {
					facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
		}
	}
	
	public boolean renderizarBotaoCandidatar() {
		if(rotina.getPrestadores() != null && rotina.getPrestadores().contains(usuarioLogado)) {
			return false;
		}
		else {
			return true;
		}
	}

	public Rotina getRotina() {
		return rotina;
	}

	public void setRotina(Rotina rotina) {
		this.rotina = rotina;
	}

}
