package br.com.contratediarista.service;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.contratediarista.dao.BairroDao;
import br.com.contratediarista.entity.Bairro;

@RequestScoped
@Path("bairro")
public class BairroService implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public BairroService() {

	}

	@Inject
	private BairroDao bairroDao;

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response listByIdCidade(@PathParam("id") int id) {
		try {
			List<Bairro> bairros = bairroDao.listByIdCidade(id);
			return Response.ok(bairros).build();
		} catch (Exception e) {
			return Response.status(INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response listAll() {
		try {
			List<Bairro> bairros = bairroDao.listAll();
			return Response.ok(bairros).build();
		} catch (Exception e) {
			return Response.status(INTERNAL_SERVER_ERROR)
					.type(MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
		}
	}

	@GET
	@Path(value = "restore-descricao/{descricao}")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response restoreByDescricao(@PathParam(value = "descricao") String descricao) {
		try {
			Bairro bairro = bairroDao.restoreByDescricao(descricao);
			return Response.ok(bairro).build();
		} catch (Exception e) {
			return Response.status(INTERNAL_SERVER_ERROR)
					.type(MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
		}
	}

}
