package br.com.contratediarista.bean;

import java.io.IOException;
import java.io.Serializable;
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

import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.service.RotinaService;
import br.com.contratediarista.utils.FacesUtil;

@Named
@ViewScoped
public class VisualizarVagasVinculadasBean implements Serializable {
	private SimpleDateFormat formatoJson = new SimpleDateFormat("yyyy-MM-dd");

	private static final long serialVersionUID = 1L;

	@Inject
	private RotinaService rotinaService;

	@Inject
	private FacesUtil facesUtil;
	
	@Inject
	private FacesContext facesContext;

	private ScheduleModel eventModel;
	private ScheduleEvent event = new DefaultScheduleEvent();
	private Usuario usuarioLogado;

	public VisualizarVagasVinculadasBean() {

	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		Date dataInicial = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		Date dataFinal = calendar.getTime();
		List<Rotina> rotinas = new ArrayList<>();
		usuarioLogado = facesUtil.getUsuarioLogado();
		Response response = rotinaService.buscarVagasUsuario(usuarioLogado.getUid(), formatoJson.format(dataInicial),
				formatoJson.format(dataFinal));
		if(response.getStatus() == Status.OK.getStatusCode()) {
			rotinas = (List<Rotina>) response.getEntity();
		}
		eventModel = new DefaultScheduleModel();
		for(Rotina rotina : rotinas) {
			DefaultScheduleEvent vinculada = new DefaultScheduleEvent();
			vinculada.setAllDay(false);
			vinculada.setData(rotina);
			vinculada.setStartDate(rotina.getData());
			vinculada.setEndDate(rotina.getData());
			vinculada.setTitle(rotina.getVaga().getContratante().getNome());
			eventModel.addEvent(vinculada);
		}
	}

	public void onEventSelect(SelectEvent selectEvent) throws IOException {
		event = (ScheduleEvent) selectEvent.getObject();
		facesContext.getExternalContext().redirect("visualizacao_vaga_prestador.jsf");
		facesContext.getExternalContext().getSessionMap().put("rotina",(Rotina) event.getData());
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

}
