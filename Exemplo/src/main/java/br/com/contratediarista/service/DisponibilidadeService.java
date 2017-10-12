package br.com.contratediarista.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.contratediarista.dao.DisponibilidadeDao;
import br.com.contratediarista.dao.UsuarioDao;
import br.com.contratediarista.entity.Disponibilidade;
import br.com.contratediarista.entity.Usuario;

@RequestScoped
@Path("disponibilidade")
public class DisponibilidadeService implements Serializable {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Gson gson = new Gson();

	private static final long serialVersionUID = 1L;

	public DisponibilidadeService() {

	}

	@Inject
	private DisponibilidadeDao disponibilidadeDao;

	@Inject
	private UsuarioDao usuarioDao;

	@POST
	@Path("/salvar")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response salvar(Disponibilidade disponibilidade) {
		try {
			disponibilidadeDao.salvar(disponibilidade);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/excluir")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response excluir(Disponibilidade disponibilidade) {
		try {
			disponibilidadeDao.excluir(disponibilidade);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/buscar-id/{id}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response restoreById(@PathParam(value = "id") int id) {
		try {
			Disponibilidade disponibilidade = disponibilidadeDao.restoreById(id);
			if (disponibilidade != null) {
				return Response.ok(disponibilidade).build();
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
	@Path("/buscar-por-usuario-data/{uid}/{dataInicial}/{dataFinal}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response buscarPorUsuarioEData(@PathParam(value = "uid") String uid,
			@PathParam(value = "dataInicial") String dataInicial, @PathParam(value = "dataFinal") String dataFinal) {
		try {
			Usuario usuario = usuarioDao.buscarUsuarioPorChave(uid);
			Date dataIni = sdf.parse(dataInicial);
			Date dataFin = sdf.parse(dataFinal);
			List<Disponibilidade> disponibilidades = disponibilidadeDao.buscarDisponibilidadePorUsuarioEData(usuario,
					dataIni, dataFin);
			if (disponibilidades != null) {
				return Response.ok(disponibilidades).build();
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
	@Path("/buscar-disponibilidades")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response buscarDisponibilidades(String json) {
		try {
			JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
			Date dataIni = null;
			Date dataFin = null;
			int valorInicial = 0;
			int valorFinal = 0;
			if (jsonObject.get("dataInicial") != null) {
				dataIni = sdf.parse(jsonObject.get("dataInicial").getAsString());
			}
			if (jsonObject.get("dataFinal") != null) {
				dataFin = sdf.parse(jsonObject.get("dataFinal").getAsString());
			}
			if (jsonObject.get("valorInicial") != null) {
				valorInicial = jsonObject.get("valorInicial").getAsInt();
			}
			if (jsonObject.get("valorFinal") != null) {
				valorFinal = jsonObject.get("valorFinal").getAsInt();
			}
			List<Disponibilidade> disponibilidades = disponibilidadeDao.buscarDisponibilidades(dataIni, dataFin,
					valorInicial, valorFinal);
			if (disponibilidades != null) {
				return Response.ok(disponibilidades).build();
			} else {
				return Response.noContent().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

}
