package br.com.contratediarista.service;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.contratediarista.dao.VagaDao;
import br.com.contratediarista.entity.Vaga;

@RequestScoped
@Path("vaga")
public class VagaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private VagaDao vagaDao;
	
	public VagaService() {
		
	}

	
	@POST
	@Path("salvar")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response salvar(Vaga vaga) {
		try {
			vagaDao.salvar(vaga);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

}
