package br.com.contratediarista.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.LocalDate;
import org.primefaces.event.SelectEvent;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.TipoAtividade;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.enuns.DiasSemana;
import br.com.contratediarista.enuns.TipoPeriodo;
import br.com.contratediarista.service.TipoAtividadeService;
import br.com.contratediarista.service.VagaService;
import br.com.contratediarista.utils.FacesUtil;

@SuppressWarnings("unchecked")
@Named
@ViewScoped
public class BuscarVagaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TipoAtividade> tiposAtividades;
	private Usuario usuarioLogado;

	private Date dataInicial;
	private Date dataFinal;
	private Date dataMinima;
	private Integer valorInicial;
	private Integer valorFinal;
	private TipoPeriodo tipoPeriodo;
	private List<Rotina> rotinas;
	private Rotina rotinaSelecionada;
	private List<TipoAtividade> atividadesSelecionadas;
	private List<String> diasSelecionados;
	private Boolean desabilitaDomingo;
	private Boolean desabilitaSegunda;
	private Boolean desabilitaTerca;
	private Boolean desabilitaQuarta;
	private Boolean desabilitaQuinta;
	private Boolean desabilitaSexta;
	private Boolean desabilitaSabado;
	private SimpleDateFormat formatoJson = new SimpleDateFormat("yyyy-MM-dd");

	@Inject
	private TipoAtividadeService tipoAtividadeService;

	@Inject
	private VagaService vagaService;

	@Inject
	private FacesUtil facesUtil;

	@Inject
	private FacesContext facesContext;

	private Gson gson;

	public BuscarVagaBean() {

	}

	@PostConstruct
	public void init() {
		gson = new Gson();
		dataInicial = new Date();
		dataFinal = new Date();
		dataMinima = new Date();
		rotinas = new ArrayList<>();
		tipoPeriodo = null;
		diasSelecionados = null;
		carregarListaAtividades();
		usuarioLogado = facesUtil.getUsuarioLogado();
		desabilitarDiasSemana();
	}

	public void onSelectDataInicial(SelectEvent event) {
		dataInicial = (Date) event.getObject();
		desabilitarDiasSemana();
	}

	public void onSelectDataFinal(SelectEvent event) {
		dataFinal = (Date) event.getObject();
		desabilitarDiasSemana();
	}

	public void desabilitarDiasSemana() {
		boolean contemDomingo = false;
		boolean contemSegunda = false;
		boolean contemTerca = false;
		boolean contemQuarta = false;
		boolean contemQuinta = false;
		boolean contemSexta = false;
		boolean contemSabado = false;
		diasSelecionados = new ArrayList<>();
		LocalDate dataIni = LocalDate.fromDateFields(dataInicial);
		LocalDate dataFin = LocalDate.fromDateFields(dataFinal);
		List<Date> dias = new ArrayList<>();
		for (LocalDate data = dataIni; data.isBefore(dataFin); data = data.plusDays(1)) {
			dias.add(data.toDate());
		}
		dias.add(dataFin.toDate());
		for (Date data : dias) {
			LocalDate dataSelecionada = LocalDate.fromDateFields(data);
			DiasSemana diaSemana = DiasSemana.getValor(dataSelecionada.getDayOfWeek());
			if (diaSemana.equals(DiasSemana.DOMINGO)) {
				contemDomingo = true;
			} else if (diaSemana.equals(DiasSemana.SEGUNDA)) {
				contemSegunda = true;
			} else if (diaSemana.equals(DiasSemana.TERCA)) {
				contemTerca = true;
			} else if (diaSemana.equals(DiasSemana.QUARTA)) {
				contemQuarta = true;
			} else if (diaSemana.equals(DiasSemana.QUINTA)) {
				contemQuinta = true;
			} else if (diaSemana.equals(DiasSemana.SEXTA)) {
				contemSexta = true;
			} else if (diaSemana.equals(DiasSemana.SABADO)) {
				contemSabado = true;
			}
		}
		desabilitaDomingo = contemDomingo ? false : true;
		desabilitaSegunda = contemSegunda ? false : true;
		desabilitaTerca = contemTerca ? false : true;
		desabilitaQuarta = contemQuarta ? false : true;
		desabilitaQuinta = contemQuinta ? false : true;
		desabilitaSexta = contemSexta ? false : true;
		desabilitaSabado = contemSabado ? false : true;
	}

	public void carregarListaAtividades() {
		tiposAtividades = new ArrayList<>();
		Response response = tipoAtividadeService.listAll();
		if (response.getStatus() == Status.OK.getStatusCode()) {
			tiposAtividades = (List<TipoAtividade>) response.getEntity();
		}
	}

	public void pesquisar() {
		rotinas = new ArrayList<>();
		JsonObject parametros = montarChamadaPesquisarVaga();
		String json = gson.toJson(parametros);
		Response response = vagaService.buscarVagasDisponiveisPorDataValor(json);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			rotinas = (List<Rotina>) response.getEntity();
		} else if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
			facesUtil.exibirMsgWarning(facesUtil.getLabel("nenhum.resultado.encontrado"));
		} else {
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.ao.pesquisar"));
		}
	}

	public JsonObject montarChamadaPesquisarVaga() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("uid", usuarioLogado.getUid());
		jsonObject.addProperty("dataInicial", formatoJson.format(dataInicial));
		jsonObject.addProperty("dataFinal", formatoJson.format(dataFinal));
		jsonObject.addProperty("valorInicial", valorInicial);
		jsonObject.addProperty("valorFinal", valorFinal);

		List<DiasSemana> listaDiasSemana = new ArrayList<>();
		if (!diasSelecionados.isEmpty()) {
			for (String s : diasSelecionados) {
				listaDiasSemana.add(DiasSemana.getValor(Integer.parseInt(s)));
			}
		}

		if (!listaDiasSemana.isEmpty()) {
			JsonArray arraDias = new JsonArray();
			for (DiasSemana dia : listaDiasSemana) {
				JsonObject jsonDia = new JsonObject();
				jsonDia.addProperty("diaSemana", dia.ordinal());
				arraDias.add(jsonDia);
			}
			jsonObject.add("diasSelecionados", arraDias);
		}
		if (tipoPeriodo != null) {
			jsonObject.addProperty("periodo", tipoPeriodo.ordinal());
		}
		if (tiposAtividades != null && !tiposAtividades.isEmpty()) {
			JsonArray arrayTipoAtividades = new JsonArray();
			for (TipoAtividade tipo : tiposAtividades) {
				JsonObject objectTipoAtividade = new JsonObject();
				objectTipoAtividade.addProperty("id", tipo.getId());
				arrayTipoAtividades.add(objectTipoAtividade);
			}
			jsonObject.add("", arrayTipoAtividades);
		}
		return jsonObject;
	}

	public void visualizar(Rotina rotina) throws IOException {
		facesContext.getExternalContext().redirect("visualizacao_vaga_prestador.jsf");
		facesContext.getExternalContext().getSessionMap().put("rotina", rotina);
	}

	public DiasSemana buscarDiaDaSemana(Rotina rotina) {
		LocalDate dataSelecionada = LocalDate.fromDateFields(rotina.getData());
		DiasSemana diaSemana = DiasSemana.getValor(dataSelecionada.getDayOfWeek());
		return diaSemana;
	}

	public List<TipoPeriodo> getTiposPeriodo() {
		return Arrays.asList(TipoPeriodo.values());
	}

	public List<DiasSemana> getDiasSemana() {
		return Arrays.asList(DiasSemana.values());
	}

	public List<TipoAtividade> getTiposAtividades() {
		return tiposAtividades;
	}

	public void setTiposAtividades(List<TipoAtividade> tiposAtividades) {
		this.tiposAtividades = tiposAtividades;
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

	public Integer getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(Integer valorInicial) {
		this.valorInicial = valorInicial;
	}

	public Integer getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(Integer valorFinal) {
		this.valorFinal = valorFinal;
	}

	public List<TipoAtividade> getAtividadesSelecionadas() {
		return atividadesSelecionadas;
	}

	public void setAtividadesSelecionadas(List<TipoAtividade> atividadesSelecionadas) {
		this.atividadesSelecionadas = atividadesSelecionadas;
	}

	public List<String> getDiasSelecionados() {
		return diasSelecionados;
	}

	public void setDiasSelecionados(List<String> diasSelecionados) {
		this.diasSelecionados = diasSelecionados;
	}

	public TipoPeriodo getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

	public List<Rotina> getRotinas() {
		return rotinas;
	}

	public void setRotinas(List<Rotina> rotinas) {
		this.rotinas = rotinas;
	}

	public Rotina getRotinaSelecionada() {
		return rotinaSelecionada;
	}

	public void setRotinaSelecionada(Rotina rotinaSelecionada) {
		this.rotinaSelecionada = rotinaSelecionada;
	}

	public Date getDataMinima() {
		return dataMinima;
	}

	public void setDataMinima(Date dataMinima) {
		this.dataMinima = dataMinima;
	}

	public Boolean getDesabilitaDomingo() {
		return desabilitaDomingo;
	}

	public void setDesabilitaDomingo(Boolean desabilitaDomingo) {
		this.desabilitaDomingo = desabilitaDomingo;
	}

	public Boolean getDesabilitaSegunda() {
		return desabilitaSegunda;
	}

	public void setDesabilitaSegunda(Boolean desabilitaSegunda) {
		this.desabilitaSegunda = desabilitaSegunda;
	}

	public Boolean getDesabilitaTerca() {
		return desabilitaTerca;
	}

	public void setDesabilitaTerca(Boolean desabilitaTerca) {
		this.desabilitaTerca = desabilitaTerca;
	}

	public Boolean getDesabilitaQuarta() {
		return desabilitaQuarta;
	}

	public void setDesabilitaQuarta(Boolean desabilitaQuarta) {
		this.desabilitaQuarta = desabilitaQuarta;
	}

	public Boolean getDesabilitaQuinta() {
		return desabilitaQuinta;
	}

	public void setDesabilitaQuinta(Boolean desabilitaQuinta) {
		this.desabilitaQuinta = desabilitaQuinta;
	}

	public Boolean getDesabilitaSexta() {
		return desabilitaSexta;
	}

	public void setDesabilitaSexta(Boolean desabilitaSexta) {
		this.desabilitaSexta = desabilitaSexta;
	}

	public Boolean getDesabilitaSabado() {
		return desabilitaSabado;
	}

	public void setDesabilitaSabado(Boolean desabilitaSabado) {
		this.desabilitaSabado = desabilitaSabado;
	}

}
