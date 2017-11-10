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

import org.primefaces.context.RequestContext;

import br.com.contratediarista.entity.Disponibilidade;
import br.com.contratediarista.entity.Endereco;
import br.com.contratediarista.entity.TipoAtividade;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.enuns.TipoPeriodo;
import br.com.contratediarista.service.DisponibilidadeService;
import br.com.contratediarista.service.TipoAtividadeService;
import br.com.contratediarista.service.UsuarioService;
import br.com.contratediarista.utils.FacesUtil;

@Named
@ViewScoped
public class CadastrarDisponibilidadeBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Date dataMinima;
	private Disponibilidade disponibilidade;
	private List<TipoAtividade> tiposAtividades;
	private List<TipoAtividade> atividadesSelecionadas;
	private Usuario usuarioLogado;
	private Endereco endereco;
	
	@Inject
	private FacesUtil facesUtil;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private BuscaEnderecoBean buscaEnderecoBean;
	
	@Inject 
	private TipoAtividadeService tipoAtividadeService;
	
	@Inject
	private DisponibilidadeService disponibilidadeService;
	
	public CadastrarDisponibilidadeBean() {
		
	}
	
	@PostConstruct
	public void init() {
		instanciarNovo();
	}
	
	public void instanciarNovo() {
		disponibilidade = new Disponibilidade();
		disponibilidade.setEndereco(new Endereco());
		disponibilidade.setData(new Date());
		dataMinima = new Date();
		usuarioLogado = facesUtil.getUsuarioLogado();
		
		Response response = usuarioService.buscarUsuarioByUid(usuarioLogado.getUid());
		if(response.getStatus() == Status.OK.getStatusCode()) {
			usuarioLogado = (Usuario) response.getEntity();
		}
		if (usuarioLogado != null) {
			disponibilidade.setPrestador(usuarioLogado);
			endereco = Endereco.copy(usuarioLogado.getEndereco());
			endereco.setId(null);
			buscaEnderecoBean.setEndereco(endereco);
			buscaEnderecoBean.setCidade(endereco.getBairro().getCidade());
			buscaEnderecoBean.setEstado(buscaEnderecoBean.getCidade().getEstado());
			RequestContext.getCurrentInstance().execute("carregarMapa()");
		}
		carregarListaAtividades();
	}
	
	public void cancelar() {
		instanciarNovo();
	}
	
	public void salvar() {
		disponibilidade.setEndereco(buscaEnderecoBean.getEndereco());
		disponibilidade.setTiposAtividade(new ArrayList<>());
		for (TipoAtividade tipo : atividadesSelecionadas) {
			Response tipoAtividade = tipoAtividadeService.restoreById(tipo.getId());
			if (tipoAtividade.getStatus() == Status.OK.getStatusCode()) {
				disponibilidade.getTiposAtividade().add((TipoAtividade) tipoAtividade.getEntity());
			}
		}
		Response response = disponibilidadeService.salvar(disponibilidade);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			instanciarNovo();	
			facesUtil.exibirMsgSucesso("Disponibilidade salva com sucesso.");
		} else {
			facesUtil.exibirMsgErro("Erro ao salvar disponibilidade.");
		}
	}
	
	@SuppressWarnings("unchecked")
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

	public Date getDataMinima() {
		return dataMinima;
	}

	public void setDataMinima(Date dataMinima) {
		this.dataMinima = dataMinima;
	}

	public List<TipoAtividade> getTiposAtividades() {
		return tiposAtividades;
	}

	public void setTiposAtividades(List<TipoAtividade> tiposAtividades) {
		this.tiposAtividades = tiposAtividades;
	}
	
	public Disponibilidade getDisponibilidade() {
		return disponibilidade;
	}

	public void setDisponibilidade(Disponibilidade disponibilidade) {
		this.disponibilidade = disponibilidade;
	}

	public List<TipoAtividade> getAtividadesSelecionadas() {
		return atividadesSelecionadas;
	}

	public void setAtividadesSelecionadas(List<TipoAtividade> atividadesSelecionadas) {
		this.atividadesSelecionadas = atividadesSelecionadas;
	}

}
