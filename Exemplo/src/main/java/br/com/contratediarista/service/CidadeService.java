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

import br.com.contratediarista.dao.CidadeDao;
import br.com.contratediarista.entity.Cidade;

@RequestScoped
@Path("cidade")
public class CidadeService implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public CidadeService() {

	}

	@Inject
	private CidadeDao cidadeDao;

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response listByIdEstado(@PathParam("id") int id) {
		try {
			List<Cidade> cidades = cidadeDao.listByIdEstado(id);
			return Response.ok(cidades).build();
		} catch (Exception e) {
			return Response.status(INTERNAL_SERVER_ERROR)
					.type(MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response listAll() {
		try {
			List<Cidade> cidades = cidadeDao.listAll();
			return Response.ok(cidades).build();
		} catch (Exception e) {
			return Response.status(INTERNAL_SERVER_ERROR)
					.type(MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
		}
	}

	@GET
	@Path(value = "restore-nome/{nome}")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response restoreByNome(@PathParam(value = "nome") String nome) {
		try {
			Cidade cidade = cidadeDao.restoreByNome(nome);
			return Response.ok(cidade).build();
		} catch (Exception e) {
			return Response.status(INTERNAL_SERVER_ERROR)
					.type(MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
		}
	}

}
