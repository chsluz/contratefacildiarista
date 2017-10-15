package br.com.contratediarista.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import br.com.contratediarista.entity.Avaliacao;
import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.service.AvaliacaoService;
import br.com.contratediarista.service.RotinaService;
import br.com.contratediarista.utils.FacesUtil;

@Named
@ViewScoped
public class VisualizarVagasPrestadorAprovadoBean implements Serializable {
	private SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatoJson = new SimpleDateFormat("yyyy-MM-dd");
	private static final long serialVersionUID = 1L;
	private Usuario usuarioAvaliado;
	private Rotina rotinaSelecionada;
	private Usuario usuarioLogado;
	private Avaliacao avaliacao;
	@Inject
	private FacesContext facesContext;

	private ScheduleModel eventModel;
	private ScheduleEvent event = new DefaultScheduleEvent();

	@Inject
	private FacesUtil facesUtil;

	@Inject
	private AvaliacaoService avaliacaoService;

	@Inject
	private RotinaService rotinaService;

	public VisualizarVagasPrestadorAprovadoBean() {

	}

	@PostConstruct
	public void init() {
		instanciarNovo();
	}

	@SuppressWarnings("unchecked")
	public void instanciarNovo() {
		avaliacao = new Avaliacao();
		Calendar calendarInicial = Calendar.getInstance();
		calendarInicial.add(Calendar.MONTH, -2);
		Calendar calendarFinal = Calendar.getInstance();
		calendarFinal.add(Calendar.MONTH, 1);
		Date dataInicial = calendarInicial.getTime();
		Date dataFinal = calendarFinal.getTime();
		List<Rotina> rotinas = new ArrayList<>();
		usuarioLogado = facesUtil.getUsuarioLogado();
		rotinaSelecionada = (Rotina) facesContext.getExternalContext().getSessionMap().get("rotina");
		usuarioAvaliado = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuarioAvaliado");
		if (rotinaSelecionada != null) {
			rotinaSelecionada = restaurar(rotinaSelecionada);
		}

		Response response = rotinaService.buscarPorUsuarioEDataVinculada(usuarioLogado.getUid(),
				formatoJson.format(dataInicial), formatoJson.format(dataFinal));
		if (response.getStatus() == Status.OK.getStatusCode()) {
			rotinas = (List<Rotina>) response.getEntity();
		}
		eventModel = new DefaultScheduleModel();
		for (Rotina rotina : rotinas) {
			DefaultScheduleEvent vinculada = new DefaultScheduleEvent();
			vinculada.setAllDay(false);
			vinculada.setData(rotina);
			vinculada.setStartDate(rotina.getData());
			vinculada.setEndDate(rotina.getData());
			vinculada.setTitle(rotina.getPrestadorSelecionado().getNome());
			eventModel.addEvent(vinculada);
		}
	}

	public void onEventSelect(SelectEvent selectEvent) throws IOException, ParseException {
		event = (ScheduleEvent) selectEvent.getObject();
		Date data = event.getStartDate();
		String dataAtual = formatoData.format(new Date());
		rotinaSelecionada = (Rotina) event.getData();
		if (data.before(formatoData.parse(dataAtual))) {
			facesContext.getExternalContext().redirect("avaliar_prestador.jsf");
			facesContext.getExternalContext().getSessionMap().put("usuarioAvaliado",
					(Usuario) rotinaSelecionada.getPrestadorSelecionado());
			facesContext.getExternalContext().getSessionMap().put("rotina", (Rotina) event.getData());
		} else {
			facesContext.getExternalContext().redirect("mais_informacoes_vaga.jsf");
			facesContext.getExternalContext().getSessionMap().put("rotina", (Rotina) event.getData());
		}
	}

	public Rotina restaurar(Rotina rotina) {
		Response response = rotinaService.restoreById(rotina.getId());
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return (Rotina) response.getEntity();
		} else {
			return null;
		}
	}

	public void avaliar() {
		avaliacao.setAvaliador(usuarioLogado);
		avaliacao.setUsuario(usuarioAvaliado);
		avaliacao.setData(rotinaSelecionada.getData());
		Response response = avaliacaoService.buscarAvaliacaoSalvaPrestadorContratante(usuarioAvaliado.getUid(),
				usuarioAvaliado.getUid(), formatoJson.format(avaliacao.getData()));
		if (response.getStatus() == Status.OK.getStatusCode()
				|| response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
			if (response.getStatus() == Status.OK.getStatusCode()) {
				Avaliacao retorno = (Avaliacao) response.getEntity();
				avaliacao.setId(retorno.getId());
				response = avaliacaoService.alterar(avaliacao);
				if (response.getStatus() == Status.OK.getStatusCode()) {
					facesUtil.exibirMsgSucesso(facesUtil.getLabel("avaliacao.alterada.sucesso"));
				} else {
					facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar.avaliacao"));
				}
			} else {
				response = avaliacaoService.salvar(avaliacao);
				if (response.getStatus() == Status.OK.getStatusCode()) {
					facesUtil.exibirMsgSucesso(facesUtil.getLabel("avaliacao.salva.sucesso"));
				} else {
					facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar.avaliacao"));
				}

			}
		} else {
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar.avaliacao"));
		}
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public Usuario getUsuarioAvaliado() {
		return usuarioAvaliado;
	}

	public void setUsuarioAvaliado(Usuario usuarioAvaliado) {
		this.usuarioAvaliado = usuarioAvaliado;
	}

	public Rotina getRotinaSelecionada() {
		return rotinaSelecionada;
	}

	public void setRotinaSelecionada(Rotina rotinaSelecionada) {
		this.rotinaSelecionada = rotinaSelecionada;
	}

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

}
