package br.com.contratediarista.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.contratediarista.dao.AvaliacaoDao;
import br.com.contratediarista.dao.UsuarioDao;
import br.com.contratediarista.entity.Avaliacao;
import br.com.contratediarista.entity.Usuario;

@RequestScoped
@Path("/avaliacao")
public class AvaliacaoService implements Serializable {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private static final long serialVersionUID = 1L;

	@Inject
	private AvaliacaoDao avaliacaoDao;

	@Inject
	private UsuarioDao usuarioDao;

	public AvaliacaoService() {

	}
	
	
	@POST
	@Path("/avaliar-usuario/{uidAvaliador}/{uidAvaliado}/{nota}/{data}/{observacao}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response avaliarUsuario(
			@PathParam("uidAvaliador") String uidAvaliador,
			@PathParam("uidAvaliado") String uidAvaliado,
			@PathParam("nota") int nota,
			@PathParam("data") String data,
			@PathParam("observacao") String observacao) {
		try {
			Usuario avaliador = usuarioDao.buscarUsuarioPorChave(uidAvaliador);
			Usuario avaliado = usuarioDao.buscarUsuarioPorChave(uidAvaliado);
			Date diaAvaliacao = sdf.parse(data);
			Avaliacao avaliacao = avaliacaoDao.buscarAvaliacaoSalvaPrestadorContratante(avaliador, avaliado, diaAvaliacao);
			if(avaliacao == null) {
				avaliacao = new Avaliacao();
				avaliacao.setData(diaAvaliacao);
				avaliacao.setAvaliador(avaliador);
				avaliacao.setUsuario(avaliado);
				avaliacao.setObservacao(observacao);
				avaliacao.setNota(nota);
				avaliacaoDao.salvar(avaliacao);
			}
			else {
				avaliacao.setNota(nota);
				avaliacao.setObservacao(observacao);
				avaliacaoDao.alterar(avaliacao);
			}
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/buscar-avaliacao-salva-prestador-contratante/{uidAvaliador}/{uidAvaliado}/{data}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response buscarAvaliacaoSalvaPrestadorContratante(@PathParam("uidAvaliador") String uidAvaliador,
			@PathParam("uidAvaliado") String uidAvaliado, @PathParam("data") String data) {
		try {
			Date dataFormatada = sdf.parse(data);
			Usuario avaliador = usuarioDao.buscarUsuarioPorChave(uidAvaliador);
			Usuario avaliado = usuarioDao.buscarUsuarioPorChave(uidAvaliado);
			Avaliacao consulta = avaliacaoDao.buscarAvaliacaoSalvaPrestadorContratante(avaliador, avaliado,
					dataFormatada);
			if (consulta != null) {
				return Response.ok(consulta).build();
			} else {
				return Response.noContent().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}
	
	@POST
	@Path("/salvar")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response salvar(Avaliacao avaliacao) {
		try {
			avaliacaoDao.salvar(avaliacao);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/alterar")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response alterar(Avaliacao avaliacao) {
		try {
			avaliacaoDao.alterar(avaliacao);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}


}
