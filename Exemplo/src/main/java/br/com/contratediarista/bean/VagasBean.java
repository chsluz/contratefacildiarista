package br.com.contratediarista.bean;

import java.io.Serializable;
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

import org.joda.time.LocalDate;

import br.com.contratediarista.entity.Endereco;
import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.TipoAtividade;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.entity.Vaga;
import br.com.contratediarista.enuns.DiasSemana;
import br.com.contratediarista.enuns.TipoPeriodo;
import br.com.contratediarista.service.TipoAtividadeService;
import br.com.contratediarista.service.VagaService;
import br.com.contratediarista.utils.FacesUtil;

@SuppressWarnings("unchecked")
@Named
@ViewScoped
public class VagasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Date dataInicial;
	private Date dataFinal;
	private TipoPeriodo tipoPeriodo;
	private DiasSemana[] diasSelecionados;
	private Endereco endereco;
	private Usuario usuarioLogado;

	private List<TipoAtividade> tiposAtividades;
	private List<TipoAtividade> atividadesSelecionadas;

	@Inject
	private TipoAtividadeService tipoAtividadeService;

	@Inject
	private FacesUtil facesUtil;

	@Inject
	private BuscaEnderecoBean buscaEnderecoBean;

	@Inject
	private VagaService vagaService;
	
	private Vaga vaga;

	public VagasBean() {
	}

	@PostConstruct
	public void init() {
		instanciarNovo();
	}

	public void instanciarNovo() {
		dataInicial = new Date();
		dataFinal = new Date();
		usuarioLogado = facesUtil.getUsuarioLogado();
		vaga = new Vaga();
		if (usuarioLogado != null) {
			vaga.setContratante(usuarioLogado);
			endereco = usuarioLogado.getEndereco();
			endereco.setId(null);
			buscaEnderecoBean.setEndereco(endereco);
			buscaEnderecoBean.setCidade(endereco.getBairro().getCidade());
			buscaEnderecoBean.setEstado(buscaEnderecoBean.getCidade().getEstado());
		}
		atividadesSelecionadas = new ArrayList<>();
		tipoPeriodo = null;
		diasSelecionados = null;
		carregarListaAtividades();

	}

	public void salvar() {
		if (dataFinal.before(dataInicial)) {
			facesUtil.exibirMsgErro(facesUtil.getLabel("data.final.nao.pode.ser.menor.que.data.inicial"));
			return;
		}
		try {
			vaga.setEndereco(buscaEnderecoBean.getEndereco());
			vaga.setDataCadastrada(new Date());
			vaga.setTipoPeriodo(tipoPeriodo);
			List<TipoAtividade> atividades = new ArrayList<>();
			for (TipoAtividade tipo : atividadesSelecionadas) {
				Response tipoAtividade = tipoAtividadeService.restoreById(tipo.getId());
				if (tipoAtividade.getStatus() == Status.OK.getStatusCode()) {
					atividades.add((TipoAtividade) tipoAtividade.getEntity());
				}
			}
			vaga.setTiposAtividade(atividades);
			vaga.setRotinas(new ArrayList<>());
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
				if (Arrays.asList(diasSelecionados).contains(diaSemana)) {
					Rotina rotina = new Rotina();
					rotina.setData(data);
					rotina.setDiaSemana(diaSemana);
					rotina.setVaga(vaga);
					vaga.getRotinas().add(rotina);
				}
			}
			Response response = vagaService.salvar(vaga);
			if (response.getStatus() == Status.OK.getStatusCode()) {
				instanciarNovo();
				facesUtil.exibirMsgSucesso(facesUtil.getLabel("salvo.sucesso"));
			} else {
				facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
			}
		} catch (Exception e) {
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.salvar"));
			e.printStackTrace();
		}
	}

	public void cancelar() {
		instanciarNovo();
	}

	public void carregarListaAtividades() {
		tiposAtividades = new ArrayList<>();
		Response response = tipoAtividadeService.listAll();
		if (response.getStatus() == Status.OK.getStatusCode()) {
			tiposAtividades = (List<TipoAtividade>) response.getEntity();
		}
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

	public List<TipoAtividade> getAtividadesSelecionadas() {
		return atividadesSelecionadas;
	}

	public void setAtividadesSelecionadas(List<TipoAtividade> atividadesSelecionadas) {
		this.atividadesSelecionadas = atividadesSelecionadas;
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

	public TipoPeriodo getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

	public DiasSemana[] getDiasSelecionados() {
		return diasSelecionados;
	}

	public void setDiasSelecionados(DiasSemana[] diasSelecionados) {
		this.diasSelecionados = diasSelecionados;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public BuscaEnderecoBean getBuscaEnderecoBean() {
		return buscaEnderecoBean;
	}

	public void setBuscaEnderecoBean(BuscaEnderecoBean buscaEnderecoBean) {
		this.buscaEnderecoBean = buscaEnderecoBean;
	}

	public Vaga getVaga() {
		return vaga;
	}

	public void setVaga(Vaga vaga) {
		this.vaga = vaga;
	}

}
