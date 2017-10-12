package br.com.contratediarista.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.contratediarista.dao.RotinaDao;
import br.com.contratediarista.dao.UsuarioDao;
import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.Usuario;

@RequestScoped
@Path("rotina")
public class RotinaService implements Serializable {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private static final long serialVersionUID = 1L;

	@Inject
	private RotinaDao rotinaDao;

	@Inject
	private UsuarioDao usuarioDao;

	public RotinaService() {
	}

	@POST
	@Path("/buscar-id/{id}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response restoreById(@PathParam(value = "id") int id) {
		try {
			Rotina rotina = rotinaDao.restoreById(id);
			if (rotina != null) {
				return Response.ok(rotina).build();
			} else {
				return Response.noContent().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/buscar-rotina-por-usuario-data/{uid}/{dataInicial}/{dataFinal}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response buscarPorUsuarioEData(@PathParam(value = "uid") String uid,
			@PathParam(value = "dataInicial") String dataInicial, @PathParam(value = "dataFinal") String dataFinal) {
		try {
			Date dataIni = sdf.parse(dataInicial);
			Date dataFin = sdf.parse(dataFinal);
			Usuario usuario = usuarioDao.buscarUsuarioPorChave(uid);
			List<Rotina> rotinas = rotinaDao.listarRotinasPorDataEUsuario(dataIni, dataFin, usuario);
			if (rotinas != null) {
				return Response.ok(rotinas).build();
			} else {
				return Response.noContent().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}
	
	@POST
	@Path("/buscar-rotinas-vinculadas/{uid}/{dataInicial}/{dataFinal}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response buscarPorUsuarioEDataVinculada(@PathParam(value = "uid") String uid,
			@PathParam(value = "dataInicial") String dataInicial, @PathParam(value = "dataFinal") String dataFinal) {
		try {
			Date dataIni = sdf.parse(dataInicial);
			Date dataFin = sdf.parse(dataFinal);
			Usuario usuario = usuarioDao.buscarUsuarioPorChave(uid);
			List<Rotina> rotinas = rotinaDao.listarRotinasVinculadasPorDataEUsuario(dataIni, dataFin, usuario);
			if (rotinas != null) {
				return Response.ok(rotinas).build();
			} else {
				return Response.noContent().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/salvar")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response salvar(Rotina rotina) {
		try {
			rotinaDao.salvar(rotina);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/alterar")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response alterar(Rotina rotina) {
		try {
			rotinaDao.alterar(rotina);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("excluir")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response excluir(Rotina rotina) {
		try {
			rotinaDao.excluir(rotina);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/buscar-vagas-usuario/{uid}/{dataInicial}/{dataFinal}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response buscarVagasUsuario(@PathParam(value = "uid") String uid,
			@PathParam(value = "dataInicial") String dataInicial, @PathParam(value = "dataFinal") String dataFinal) {
		try {
			Usuario usuario = usuarioDao.buscarUsuarioPorChave(uid);
			Date dataIni = sdf.parse(dataInicial);
			Date dataFin = sdf.parse(dataFinal);
			List<Rotina> rotinas = rotinaDao.buscarVagasPorUsuario(usuario, dataIni, dataFin);
			if (rotinas != null) {
				return Response.ok(rotinas).build();
			} else {
				return Response.noContent().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

}
