package br.com.contratediarista.service;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.contratediarista.dao.UsuarioDao;
import br.com.contratediarista.entity.Usuario;

@RequestScoped
@Path("usuario")
public class UsuarioService implements Serializable {

	public UsuarioService() {

	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private UsuarioDao usuarioDao;

	@GET
	@Path("retornarUsuario/{uid}")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Usuario retornarUsuarioByUid(@PathParam("uid") String uid) {
		return usuarioDao.retornarUsuarioByUid(uid);
	}

//	@POST
//	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
//	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public void salvar(Usuario usuario) {
		usuarioDao.salvar(usuario);

	}

}
