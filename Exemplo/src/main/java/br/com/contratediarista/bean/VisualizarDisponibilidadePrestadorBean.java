package br.com.contratediarista.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.LocalDate;

import com.google.gson.JsonObject;

import br.com.contratediarista.entity.Disponibilidade;
import br.com.contratediarista.entity.Endereco;
import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.TipoAtividade;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.entity.Vaga;
import br.com.contratediarista.enuns.DiasSemana;
import br.com.contratediarista.service.DisponibilidadeService;
import br.com.contratediarista.service.RotinaService;
import br.com.contratediarista.service.VagaService;
import br.com.contratediarista.utils.FacesUtil;

@Named
@ViewScoped
public class VisualizarDisponibilidadePrestadorBean implements Serializable {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private static final long serialVersionUID = 1L;
	private Date dataInicial;
	private Date dataFinal;
	private Date dataMinima;
	private int valorInicial;
	private int valorFinal;
	private List<Disponibilidade> disponibilidades;
	private Usuario usuarioLogado;

	@Inject
	private FacesUtil facesUtil;

	@Inject
	private DisponibilidadeService disponibilidadeService;

	@Inject
	private RotinaService rotinaService;

	@Inject
	private VagaService vagaService;

	public VisualizarDisponibilidadePrestadorBean() {

	}

	@PostConstruct
	public void init() {
		instanciarNovo();
	}

	public void instanciarNovo() {
		dataInicial = new Date();
		dataFinal = new Date();
		dataMinima = new Date();
		usuarioLogado = facesUtil.getUsuarioLogado();
	}

	@SuppressWarnings("unchecked")
	public void buscar() {
		disponibilidades = new ArrayList<Disponibilidade>();
		if (dataFinal.before(dataInicial)) {
			facesUtil.exibirMsgErro(facesUtil.getLabel("data.final.nao.pode.ser.menor.que.data.inicial"));
			return;
		}
		Response response = disponibilidadeService.buscarDisponibilidades(montarChamada());
		if (response.getStatus() == Status.OK.getStatusCode()) {
			disponibilidades = (List<Disponibilidade>) response.getEntity();
		}
	}

	public void contratar(Disponibilidade disponibilidade) {
		disponibilidade = restaurar(disponibilidade);
		if (disponibilidade != null) {
			List<TipoAtividade> atividades = new ArrayList<>();
			for (TipoAtividade atividade : disponibilidade.getTiposAtividade()) {
				atividades.add(atividade);
			}
			Vaga vaga = new Vaga();
			vaga.setContratante(usuarioLogado);
			vaga.setDataCadastrada(new Date());
			vaga.setEndereco(Endereco.copy(usuarioLogado.getEndereco()));
			vaga.setTipoPeriodo(disponibilidade.getTipoPeriodo());
			vaga.setValorPeriodo(disponibilidade.getValorPeriodo());
			vaga.setTiposAtividade(new HashSet<>(atividades));
			vaga.setRotinas(new HashSet<>());
			
			Rotina rotina = new Rotina();
			rotina.setData(disponibilidade.getData());
			rotina.setPrestadores(new HashSet<>(Arrays.asList(disponibilidade.getPrestador())));
			rotina.setPrestadorSelecionado(disponibilidade.getPrestador());
			rotina.setVaga(vaga);
			LocalDate dataSelecionada = LocalDate.fromDateFields(disponibilidade.getData());
			DiasSemana diaSemana = DiasSemana.getValor(dataSelecionada.getDayOfWeek());
			rotina.setDiaSemana(diaSemana);
			HashSet<Rotina> rotinas = new HashSet<>();
			rotinas.add(rotina);
			vaga.setRotinas(rotinas);
			Response response = vagaService.salvar(vaga);
			if (response.getStatus() == Status.OK.getStatusCode()) {
				facesUtil.exibirMsgSucesso(facesUtil.getLabel("contratado.sucesso"));
			} else {
				facesUtil.exibirMsgErro(facesUtil.getLabel("erro.contratar"));
			}
		} else {
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.contratar"));
		}
	}

	public String montarChamada() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("dataInicial", sdf.format(dataInicial));
		jsonObject.addProperty("dataFinal", sdf.format(dataFinal));
		jsonObject.addProperty("valorInicial", valorInicial);
		jsonObject.addProperty("valorFinal", valorFinal);
		return jsonObject.toString();
	}

	public Disponibilidade restaurar(Disponibilidade disponibilidade) {
		Response response = disponibilidadeService.restoreById(disponibilidade.getId());
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return (Disponibilidade) response.getEntity();
		} else {
			return null;
		}
	}

	public boolean removerDisponibilidade(Disponibilidade disponibilidade) {
		Response response = disponibilidadeService.excluir(disponibilidade);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return true;
		} else {
			return false;
		}
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

	public void setDataMinima(Date dataMinima) {
		this.dataMinima = dataMinima;
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

	public void setValorInicial(Integer valorInicial) {
		this.valorInicial = valorInicial;
	}

	public List<Disponibilidade> getDisponibilidades() {
		return disponibilidades;
	}

	public void setDisponibilidades(List<Disponibilidade> disponibilidades) {
		this.disponibilidades = disponibilidades;
	}

}
