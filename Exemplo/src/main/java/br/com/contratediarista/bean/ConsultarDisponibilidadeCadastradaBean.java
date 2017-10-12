package br.com.contratediarista.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.contratediarista.entity.Disponibilidade;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.service.DisponibilidadeService;
import br.com.contratediarista.utils.FacesUtil;

@Named
@ViewScoped
public class ConsultarDisponibilidadeCadastradaBean implements Serializable {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private static final long serialVersionUID = 1L;
	private Date dataInicial;
	private Date dataFinal;
	private Date dataMinima;
	private Disponibilidade disponibilidade;
	private List<Disponibilidade> disponibilidades;
	private Usuario usuarioLogado;

	@Inject
	private DisponibilidadeService disponibilidadeService;

	@Inject
	private FacesUtil facesUtil;

	public ConsultarDisponibilidadeCadastradaBean() {

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
		Response response = disponibilidadeService.buscarPorUsuarioEData(usuarioLogado.getUid(),
				sdf.format(dataInicial), sdf.format(dataFinal));
		if (response.getStatus() == Status.OK.getStatusCode()) {
			disponibilidades = (List<Disponibilidade>) response.getEntity();
		}
	}

	public void excluir() {
		try {
			Response response = disponibilidadeService.restoreById(disponibilidade.getId());
			if(response.getStatus() == Status.OK.getStatusCode()) {
				disponibilidade = (Disponibilidade) response.getEntity();
				response = disponibilidadeService.excluir(disponibilidade);
				if (response.getStatus() == Status.OK.getStatusCode()) {
					disponibilidades.remove(disponibilidade);
					facesUtil.exibirMsgSucesso(facesUtil.getLabel("excluido.sucesso"));
				} else {
					facesUtil.exibirMsgErro(facesUtil.getLabel("erro.excluir"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.exibirMsgErro(facesUtil.getLabel("erro.excluir"));
		}
	}
	
	public void teste() {
		System.out.println("teste");
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

	public List<Disponibilidade> getDisponibilidades() {
		return disponibilidades;
	}

	public void setDisponibilidades(List<Disponibilidade> disponibilidades) {
		this.disponibilidades = disponibilidades;
	}

	public Disponibilidade getDisponibilidade() {
		return disponibilidade;
	}

	public void setDisponibilidade(Disponibilidade disponibilidade) {
		this.disponibilidade = disponibilidade;
	}
}
