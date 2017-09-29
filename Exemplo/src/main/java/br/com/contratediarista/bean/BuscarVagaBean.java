package br.com.contratediarista.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.contratediarista.entity.TipoAtividade;
import br.com.contratediarista.enuns.DiasSemana;
import br.com.contratediarista.enuns.TipoPeriodo;
import br.com.contratediarista.service.TipoAtividadeService;
import br.com.contratediarista.service.VagaService;

@SuppressWarnings("unchecked")
@Named
@ViewScoped
public class BuscarVagaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TipoAtividade> tiposAtividades;
	
	private Date dataInicial;
	private Date dataFinal;
	private int valorInicial;
	private int valorFinal;
	private TipoPeriodo tipoPeriodo;
	private List<TipoAtividade> atividadesSelecionadas;
	private DiasSemana[] diasSelecionados;
	private SimpleDateFormat formatoJson = new SimpleDateFormat("yyyy-MM-dd");

	@Inject
	private TipoAtividadeService tipoAtividadeService;
	
	@Inject
	private VagaService vagaService;
	
	private Gson gson;

	public BuscarVagaBean() {

	}

	@PostConstruct
	public void init() {
		gson = new Gson();
		dataInicial = new Date();
		dataFinal = new Date();
		carregarListaAtividades();
	}

	public void carregarListaAtividades() {
		tiposAtividades = new ArrayList<>();
		Response response = tipoAtividadeService.listAll();
		if (response.getStatus() == Status.OK.getStatusCode()) {
			tiposAtividades = (List<TipoAtividade>) response.getEntity();
		}
	}
	
	public void pesquisar() {
		JsonObject parametros = montarChamadaPesquisarVaga();
		String json = gson.toJson(parametros);
		vagaService.buscarVagasDisponiveisPorDataValor(json);
	}
	
	public JsonObject montarChamadaPesquisarVaga() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("dataInicial", formatoJson.format(dataInicial));
		jsonObject.addProperty("dataFinal", formatoJson.format(dataFinal));
		jsonObject.addProperty("valorInicial", valorInicial);
		jsonObject.addProperty("valorFinal", valorFinal);
		return jsonObject;
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

	public int getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(int valorInicial) {
		this.valorInicial = valorInicial;
	}

	public int getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(int valorFinal) {
		this.valorFinal = valorFinal;
	}

	public List<TipoAtividade> getAtividadesSelecionadas() {
		return atividadesSelecionadas;
	}

	public void setAtividadesSelecionadas(List<TipoAtividade> atividadesSelecionadas) {
		this.atividadesSelecionadas = atividadesSelecionadas;
	}

	public DiasSemana[] getDiasSelecionados() {
		return diasSelecionados;
	}

	public void setDiasSelecionados(DiasSemana[] diasSelecionados) {
		this.diasSelecionados = diasSelecionados;
	}

	public TipoPeriodo getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

}
