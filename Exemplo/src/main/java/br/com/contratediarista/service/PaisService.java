package br.com.contratediarista.service;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.contratediarista.dao.PaisDao;
import br.com.contratediarista.entity.Pais;

@RequestScoped
@Path("pais")
public class PaisService implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public PaisService() {

	}

	@Inject
	private PaisDao paisDao;

	@GET
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response listAll() {
		try {
			List<Pais> paises = paisDao.listAll();
			return Response.ok(paises).build();
		} catch (Exception e) {
			return Response.status(INTERNAL_SERVER_ERROR)
					.type(MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
		}
	}

}
