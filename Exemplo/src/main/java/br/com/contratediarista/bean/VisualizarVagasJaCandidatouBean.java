package br.com.contratediarista.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.LocalDate;

import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.enuns.DiasSemana;
import br.com.contratediarista.service.RotinaService;
import br.com.contratediarista.utils.FacesUtil;

@Named
@ViewScoped
public class VisualizarVagasJaCandidatouBean implements Serializable {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final long serialVersionUID = 1L;
	private List<Rotina> rotinas;
	private Usuario usuarioLogado;
	@Inject
	private FacesContext facesContext;
	@Inject
	private RotinaService rotinaService;
	@Inject
	private FacesUtil facesUtil;

	public VisualizarVagasJaCandidatouBean() {

	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		Date data = new Date();
		String dataFormatada = sdf.format(data);
		usuarioLogado = facesUtil.getUsuarioLogado();
		Response response = rotinaService.listarRotinasJaCandidatou(dataFormatada, usuarioLogado.getUid());
		if (response.getStatus() == Status.OK.getStatusCode()) {
			rotinas = (List<Rotina>) response.getEntity();
		}
		// else if(response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
		// facesUtil.exibirMsgWarning("Você não se candidatou a nenhuma vaga");
		// }
	}

	public String jaSelecionado(Rotina rotina) {
		if (rotina.getPrestadorSelecionado() != null && rotina.getPrestadorSelecionado().equals(usuarioLogado)) {
			return "Sim";
		} else {
			return "Não";
		}
	}

	public void visualizar(Rotina rotina) throws IOException {
		facesContext.getExternalContext().redirect("visualizacao_vaga_prestador.jsf");
		facesContext.getExternalContext().getSessionMap().put("rotina", rotina);
	}

	public void excluir(Rotina rotina) throws Exception {
		Response response = rotinaService.restoreById(rotina.getId());
		if (response.getStatus() == Status.OK.getStatusCode()) {
			rotina = (Rotina) response.getEntity();
			List<Usuario> usuarios = new ArrayList<>();
			if (rotina.getPrestadorSelecionado() != null && rotina.getPrestadorSelecionado().equals(usuarioLogado)) {
				rotina.setPrestadorSelecionado(null);
			}
			for (Usuario usuario : rotina.getPrestadores()) {
				usuarios.add(usuario);
			}
			usuarios.remove(usuarioLogado);
			rotina.setPrestadores(new HashSet<>(usuarios));
			response = rotinaService.alterar(rotina);
			if (response.getStatus() == Status.OK.getStatusCode()) {
				rotinas.remove(rotina);
				facesUtil.exibirMsgSucesso("Descadastrado com sucesso.");
			}
			else {
				facesUtil.exibirMsgErro("Erro ao se descadastrar da vaga");
			}
		} else {
			facesUtil.exibirMsgErro("Erro ao se descadastrar da vaga");
		}
	}

	public DiasSemana buscarDiaDaSemana(Rotina rotina) {
		LocalDate dataSelecionada = LocalDate.fromDateFields(rotina.getData());
		DiasSemana diaSemana = DiasSemana.getValor(dataSelecionada.getDayOfWeek());
		return diaSemana;
	}

	public List<Rotina> getRotinas() {
		return rotinas;
	}

	public void setRotinas(List<Rotina> rotinas) {
		this.rotinas = rotinas;
	}

}
