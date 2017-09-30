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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.TipoAtividade;
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
	
	private Date dataInicial;
	private Date dataFinal;
	private Integer valorInicial;
	private Integer valorFinal;
	private TipoPeriodo tipoPeriodo;
	private List<Rotina> rotinas;
	private List<TipoAtividade> atividadesSelecionadas;
	private DiasSemana[] diasSelecionados;
	private SimpleDateFormat formatoJson = new SimpleDateFormat("yyyy-MM-dd");

	@Inject
	private TipoAtividadeService tipoAtividadeService;
	
	@Inject
	private VagaService vagaService;
	
	@Inject
	private FacesUtil facesUtil;
	
	private Gson gson;

	public BuscarVagaBean() {

	}

	@PostConstruct
	public void init() {
		gson = new Gson();
		dataInicial = new Date();
		dataFinal = new Date();
		rotinas = new ArrayList<>();
		tipoPeriodo = null;
		diasSelecionados = null;
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
		rotinas = new ArrayList<>();
		JsonObject parametros = montarChamadaPesquisarVaga();
		String json = gson.toJson(parametros);
		Response response = vagaService.buscarVagasDisponiveisPorDataValor(json);
		if(response.getStatus() == Status.OK.getStatusCode()) {
			rotinas = (List<Rotina>) response.getEntity();
		}
		else if(response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
			facesUtil.exibirMsgWarning(facesUtil.getLabel("nenhum.resultado.encontrado"));
		}
		else {
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.ao.pesquisar"));
		}
	}
	
	public JsonObject montarChamadaPesquisarVaga() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("dataInicial", formatoJson.format(dataInicial));
		jsonObject.addProperty("dataFinal", formatoJson.format(dataFinal));
		jsonObject.addProperty("valorInicial", valorInicial);
		jsonObject.addProperty("valorFinal", valorFinal);
		if(!Arrays.asList(diasSelecionados).isEmpty()) {
			JsonArray arraDias = new JsonArray();
			for(DiasSemana dia : Arrays.asList(diasSelecionados)) {
				JsonObject jsonDia = new JsonObject();
				jsonDia.addProperty("diaSemana", dia.ordinal());
				arraDias.add(jsonDia);
			}
			jsonObject.add("diasSelecionados", arraDias);
		}
		if(tipoPeriodo != null) {
			jsonObject.addProperty("periodo", tipoPeriodo.getDescricao());
		}
		if(tiposAtividades != null && !tiposAtividades.isEmpty()) {
			JsonArray arrayTipoAtividades = new JsonArray();
			for(TipoAtividade tipo : tiposAtividades) {
				JsonObject objectTipoAtividade = new JsonObject();
				objectTipoAtividade.addProperty("id", tipo.getId());
				arrayTipoAtividades.add(objectTipoAtividade);
			}
			jsonObject.add("", arrayTipoAtividades);
		}
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

	public List<Rotina> getRotinas() {
		return rotinas;
	}

	public void setRotinas(List<Rotina> rotinas) {
		this.rotinas = rotinas;
	}

}
