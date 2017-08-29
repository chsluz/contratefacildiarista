package br.com.contratediarista.service;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	@Path("/validar-login")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response validarLogin(Usuario usuario) {
		try {
			Usuario retorno = usuarioDao.validarLogin(usuario);
			return Response.ok(retorno).build();
		} catch (Exception e) {
			return Response.status(INTERNAL_SERVER_ERROR)
					.type(MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
		}
	}

//	@POST
//	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
//	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public void salvar(Usuario usuario) throws Exception {
		usuarioDao.salvar(usuario);
	}
	
	public void alterar(Usuario usuario) {
		usuarioDao.alterar(usuario);
	}

}
