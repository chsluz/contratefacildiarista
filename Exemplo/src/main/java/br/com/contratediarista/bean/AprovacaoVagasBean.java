package br.com.contratediarista.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.enuns.TipoPeriodo;
import br.com.contratediarista.service.RotinaService;
import br.com.contratediarista.service.UsuarioService;
import br.com.contratediarista.utils.FacesUtil;

@Named
@ViewScoped
public class AprovacaoVagasBean implements Serializable {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final long serialVersionUID = 1L;
	private Date dataInicial;
	private Date dataFinal;
	private Date dataMinima;
	private Usuario usuarioLogado;
	private TipoPeriodo tipoPeriodo;
	private List<Rotina> rotinas;
	private Rotina rotina;

	@Inject
	private RotinaService rotinaService;

	@Inject
	private FacesUtil facesUtil;

	@Inject
	private FacesContext facesContext;

	@Inject
	private UsuarioService usuarioService;

	public AprovacaoVagasBean() {

	}

	@PostConstruct
	public void init() {
		dataInicial = new Date();
		dataFinal = new Date();
		dataMinima = new Date();
		Response response = usuarioService.buscarUsuarioByUid(facesUtil.getUsuarioLogado().getUid());
		if (response.getStatus() == Status.OK.getStatusCode()) {
			usuarioLogado = (Usuario) response.getEntity();
		}
		rotina = (Rotina) facesContext.getExternalContext().getSessionMap().get("rotina");
		response = rotinaService.restoreById(rotina.getId());
		if (response.getStatus() == Status.OK.getStatusCode()) {
			rotina = (Rotina) response.getEntity();
		}
	}

	@SuppressWarnings("unchecked")
	public void buscarRotinas() {
		rotinas = new ArrayList<Rotina>();
		if (usuarioLogado != null) {
			Response response = rotinaService.buscarPorUsuarioEData(usuarioLogado.getUid(), sdf.format(dataInicial),
					sdf.format(dataFinal));
			if (response.getStatus() == Status.OK.getStatusCode()) {
				rotinas = (List<Rotina>) response.getEntity();
			}
		}
	}

	public void aprovar() {
		try {
			Response response = rotinaService.salvar(rotina);
			if (response.getStatus() == Status.OK.getStatusCode()) {
				facesUtil.exibirMsgSucesso(facesUtil.getLabel("salvo.sucesso"));
			} else {
				facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
		}
	}

	public void excluir(Rotina rotina) {
		try {
			Response response = rotinaService.excluir(rotina);
			if (response.getStatus() == Status.OK.getStatusCode()) {
				facesUtil.exibirMsgSucesso(facesUtil.getLabel("excluido.sucesso"));
			} else {
				facesUtil.exibirMsgErro(facesUtil.getLabel("erro.excluir"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.excluir"));
		}
	}

	public void editar(Rotina rotinaEditada) throws IOException {
		facesContext.getExternalContext().redirect("aprovar_vaga_contratante.jsf");
		facesContext.getExternalContext().getSessionMap().put("rotina", rotinaEditada);
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Date getDataMinima() {
		return dataMinima;
	}

	public List<Rotina> getRotinas() {
		return rotinas;
	}

	public void setRotinas(List<Rotina> rotinas) {
		this.rotinas = rotinas;
	}

	public Rotina getRotina() {
		return rotina;
	}

	public void setRotina(Rotina rotina) {
		this.rotina = rotina;
	}

	public void setDataMinima(Date dataMinima) {
		this.dataMinima = dataMinima;
	}

	public TipoPeriodo getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

}
