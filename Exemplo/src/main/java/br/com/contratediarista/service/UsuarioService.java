package br.com.contratediarista.service;

import java.io.Serializable;

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

import br.com.contratediarista.dao.UsuarioDao;
import br.com.contratediarista.entity.Usuario;

@RequestScoped
@Path("usuario")
public class UsuarioService implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioDao usuarioDao;

	@GET
	@Path("/buscar-uid/{uid}")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response buscarUsuarioByUid(@PathParam(value = "uid") String uid) {
		try {
			Usuario usuario = usuarioDao.buscarUsuarioPorChave(uid);
			return Response.ok(usuario).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/salvar/")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response salvar(Usuario usuario) throws Exception {
		try {
			usuarioDao.salvar(usuario);
			return Response.ok(usuario).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/alterar/")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response alterar(Usuario usuario) {
		try {
			usuarioDao.alterar(usuario);
			return Response.ok(usuario).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

}
