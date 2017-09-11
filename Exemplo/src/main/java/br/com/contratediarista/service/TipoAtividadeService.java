package br.com.contratediarista.service;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.contratediarista.dao.TipoAtividadeDao;
import br.com.contratediarista.entity.TipoAtividade;

@RequestScoped
@Path("tipo-atividade")
public class TipoAtividadeService implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private TipoAtividadeDao tipoAtividadeDao;
	
	public TipoAtividadeService() {
		
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response listAll() {
		try {
			List<TipoAtividade> tiposAtividade = tipoAtividadeDao.listarTodos();
			return Response.ok(tiposAtividade).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

}
