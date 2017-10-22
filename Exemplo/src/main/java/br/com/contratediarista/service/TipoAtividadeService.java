package br.com.contratediarista.service;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.contratediarista.dao.TipoAtividadeDao;
import br.com.contratediarista.entity.TipoAtividade;

@RequestScoped
@Path("tipo-atividade")
public class TipoAtividadeService implements Serializable {
	Type type = new TypeToken<List<TipoAtividade>>() {
	}.getType();

	private static final long serialVersionUID = 1L;

	@Inject
	private TipoAtividadeDao tipoAtividadeDao;

	public TipoAtividadeService() {

	}

	@POST
	@Path("salvar")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response salvar(TipoAtividade tipo) {
		try {
			tipoAtividadeDao.salvar(tipo);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}
	
	@POST
	@Path("cadastrar-tipo-atividade/{descricao}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response cadastrarTipoAtividade(@PathParam("descricao") String descricao) {
		try {
			TipoAtividade tipo = new TipoAtividade();
			tipo.setDescricao(descricao);
			tipoAtividadeDao.salvar(tipo);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("alterar")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response alterar(TipoAtividade tipo) {
		try {
			tipoAtividadeDao.alterar(tipo);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("remover")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response excluir(TipoAtividade tipo) {
		try {
			tipoAtividadeDao.excluir(tipo);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}
	
	@POST
	@Path("excluir-tipo-atividade/{id}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response excluirTipoAtividade(@PathParam("id") int id) {
		try {
			TipoAtividade tipo = tipoAtividadeDao.restoreById(id);
			tipoAtividadeDao.excluir(tipo);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("buscar-id/{id}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response restoreById(@PathParam(value = "id") int id) {
		try {
			TipoAtividade tipo = tipoAtividadeDao.restoreById(id);
			if (tipo != null) {
				return Response.ok(tipo).build();
			} else {
				return Response.noContent().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@GET()
	@Path("/listar-todos")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response listAll() {
		try {
			Gson gson = new Gson();
			List<TipoAtividade> tiposAtividade = tipoAtividadeDao.listarTodos();
			String json = gson.toJson(tiposAtividade, type);
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.entity(gson.fromJson(json, type)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

}
