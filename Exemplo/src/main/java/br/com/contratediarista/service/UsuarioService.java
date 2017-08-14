package br.com.contratediarista.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.contratediarista.dao.UsuarioDao;
import br.com.contratediarista.entity.Usuario;

public class UsuarioService implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private UsuarioDao usuarioDao;

//	@GET
//	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Usuario retornarUsuarioByUid(String uid) {
		return usuarioDao.retornarUsuarioByUid(uid);
	}

//	@POST
//	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
//	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public void salvar(Usuario usuario) {
		usuarioDao.salvar(usuario);

	}

}
