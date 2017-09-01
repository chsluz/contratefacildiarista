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

import br.com.contratediarista.dao.EstadoDao;
import br.com.contratediarista.entity.Estado;

@RequestScoped
@Path("estado")
public class EstadoService implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EstadoDao estadoDao;

	public EstadoService() {

	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response listAll() {
		try {
			List<Estado> estados = estadoDao.listAll();
			return Response.ok(estados).build();
		} catch (Exception e) {
			return Response.status(INTERNAL_SERVER_ERROR)
					.type(MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
		}
	}

	@GET
	@Path(value = "restore-sigla/{sigla}")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response restoreBySigla(@PathParam(value = "sigla") String sigla) {
		try {
			Estado estado = estadoDao.restoreBySigla(sigla);
			return Response.ok(estado).build();
		} catch (Exception e) {
			return Response.status(INTERNAL_SERVER_ERROR)
					.type(MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
		}
	}

}
