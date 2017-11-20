package br.com.contratediarista.service;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
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

import org.joda.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import br.com.contratediarista.dao.DisponibilidadeDao;
import br.com.contratediarista.dao.TipoAtividadeDao;
import br.com.contratediarista.dao.UsuarioDao;
import br.com.contratediarista.dao.VagaDao;
import br.com.contratediarista.entity.Disponibilidade;
import br.com.contratediarista.entity.Endereco;
import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.TipoAtividade;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.entity.Vaga;
import br.com.contratediarista.enuns.DiasSemana;
import br.com.contratediarista.enuns.TipoPeriodo;

@RequestScoped
@Path("disponibilidade")
public class DisponibilidadeService implements Serializable {
	Type typeDisponibilidade = new TypeToken<List<Disponibilidade>>() {
	}.getType();
	Gson gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd").create();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Gson gson = new Gson();

	private static final long serialVersionUID = 1L;

	public DisponibilidadeService() {

	}

	@Inject
	private DisponibilidadeDao disponibilidadeDao;

	@Inject
	private UsuarioDao usuarioDao;

	@Inject
	private VagaDao vagaDao;

	@Inject
	private TipoAtividadeDao tipoAtividadeDao;

	@POST
	@Path("/contratar-usuario/{idDisponibilidade}/{uidUsuario}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response contratarUsuario(@PathParam("idDisponibilidade") int idDisponibilidade,
			@PathParam("uidUsuario") String uidUsuario) {
		try {
			Disponibilidade disponibilidade = disponibilidadeDao.restoreById(idDisponibilidade);
			Usuario usuario = usuarioDao.buscarUsuarioPorChave(uidUsuario);
			List<TipoAtividade> atividades = new ArrayList<>();
			for (TipoAtividade atividade : disponibilidade.getTiposAtividade()) {
				atividades.add(atividade);
			}
			Vaga vaga = new Vaga();
			vaga.setContratante(usuario);
			vaga.setDataCadastrada(new Date());
			vaga.setEndereco(Endereco.copy(usuario.getEndereco()));
			vaga.setTipoPeriodo(disponibilidade.getTipoPeriodo());
			vaga.setValorPeriodo(disponibilidade.getValorPeriodo());
			vaga.setTiposAtividade(new HashSet<>(atividades));
			List<Rotina> novasRotinas = new ArrayList<>();
			Rotina rotina = new Rotina();
			rotina.setData(disponibilidade.getData());
			rotina.setPrestadores(new HashSet<>(Arrays.asList(disponibilidade.getPrestador())));
			rotina.setPrestadorSelecionado(disponibilidade.getPrestador());
			rotina.setVaga(vaga);
			LocalDate dataSelecionada = LocalDate.fromDateFields(disponibilidade.getData());
			DiasSemana diaSemana = DiasSemana.getValor(dataSelecionada.getDayOfWeek());
			rotina.setDiaSemana(diaSemana);
			novasRotinas.add(rotina);
			vaga.setRotinas(new HashSet<>(novasRotinas));
			vagaDao.salvar(vaga);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/salvar")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response salvar(Disponibilidade disponibilidade) {
		try {
			disponibilidadeDao.salvar(disponibilidade);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	@Path("/cadastrar-disponibilidade/{uid}")
	public Response cadastrarDisponibilidade(@PathParam("uid") String uid, String json) {
		Date data = null;
		TipoPeriodo periodo = null;
		int valorPeriodo = 0;
		Usuario usuario = null;
		List<TipoAtividade> tiposAtividade = new ArrayList<>();
		try {
			usuario = usuarioDao.buscarUsuarioPorChave(uid);
			JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
			if (jsonObject.get("data") != null) {
				data = sdf.parse(jsonObject.get("data").getAsString());
			}
			if (jsonObject.get("valorPeriodo") != null) {
				valorPeriodo = jsonObject.get("valorPeriodo").getAsInt();
			}
			if (jsonObject.get("tipoPeriodo") != null) {
				int tipoPeriodo = jsonObject.get("tipoPeriodo").getAsInt();
				periodo = TipoPeriodo.getValor(tipoPeriodo);
			}
			if (jsonObject.get("tiposAtividade") != null) {
				JsonArray arrayAtividades = jsonObject.get("tiposAtividade").getAsJsonArray();
				if (arrayAtividades != null) {
					for (int i = 0; i < arrayAtividades.size(); i++) {
						JsonObject atividade = arrayAtividades.get(i).getAsJsonObject();
						if (atividade != null) {
							if (atividade.get("id") != null) {
								int id = atividade.get("id").getAsInt();
								TipoAtividade tipo = tipoAtividadeDao.restoreById(id);
								if (tipo != null) {
									tiposAtividade.add(tipo);
								}
							}
						}
					}
				}
			}
			Disponibilidade disponibilidade = new Disponibilidade();
			disponibilidade.setData(data);
			disponibilidade.setPrestador(usuario);
			disponibilidade.setEndereco(usuario.getEndereco());
			disponibilidade.setTipoPeriodo(periodo);
			disponibilidade.setValorPeriodo(valorPeriodo);
			disponibilidade.setTiposAtividade(tiposAtividade);
			disponibilidadeDao.salvar(disponibilidade);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/excluir")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response excluir(Disponibilidade disponibilidade) {
		try {
			disponibilidadeDao.excluir(disponibilidade);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("excluir-diponibilidade/{id}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response excluirDisponibilidade(@PathParam("id") int id) {
		try {
			Disponibilidade disponibilidade = disponibilidadeDao.restoreById(id);
			disponibilidadeDao.excluir(disponibilidade);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/buscar-id/{id}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response restoreById(@PathParam(value = "id") int id) {
		try {
			Disponibilidade disponibilidade = disponibilidadeDao.restoreById(id);
			if (disponibilidade != null) {
				return Response.ok(disponibilidade).build();
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
	@Path("/buscar-por-usuario-data/{uid}/{dataInicial}/{dataFinal}")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response buscarPorUsuarioEData(@PathParam(value = "uid") String uid,
			@PathParam(value = "dataInicial") String dataInicial, @PathParam(value = "dataFinal") String dataFinal) {
		try {
			Usuario usuario = usuarioDao.buscarUsuarioPorChave(uid);
			Date dataIni = sdf.parse(dataInicial);
			Date dataFin = sdf.parse(dataFinal);
			List<Disponibilidade> disponibilidades = disponibilidadeDao.buscarDisponibilidadePorUsuarioEData(usuario,
					dataIni, dataFin);
			if (disponibilidades.isEmpty()) {
				return Response.status(Status.NO_CONTENT).build();
			} else {
				String retorno = gsonBuilder.toJson(disponibilidades, typeDisponibilidade);
				return Response.status(Status.OK).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
						.entity(gsonBuilder.fromJson(retorno, typeDisponibilidade)).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

	@POST
	@Path("/buscar-disponibilidades")
	@Consumes({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public Response buscarDisponibilidades(String json) {
		try {
			JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
			Date dataIni = null;
			Date dataFin = null;
			int valorInicial = 0;
			int valorFinal = 0;
			if (jsonObject.get("dataInicial") != null) {
				dataIni = sdf.parse(jsonObject.get("dataInicial").getAsString());
			}
			if (jsonObject.get("dataFinal") != null) {
				dataFin = sdf.parse(jsonObject.get("dataFinal").getAsString());
			}
			if (jsonObject.get("valorInicial") != null) {
				valorInicial = jsonObject.get("valorInicial").getAsInt();
			}
			if (jsonObject.get("valorFinal") != null) {
				valorFinal = jsonObject.get("valorFinal").getAsInt();
			}
			List<Disponibilidade> disponibilidades = disponibilidadeDao.buscarDisponibilidades(dataIni, dataFin,
					valorInicial, valorFinal);
			if (disponibilidades.isEmpty()) {
				return Response.status(Status.NO_CONTENT).build();
			} else {
				String retorno = gsonBuilder.toJson(disponibilidades, typeDisponibilidade);
				return Response.status(Status.OK).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
						.entity(gsonBuilder.fromJson(retorno, typeDisponibilidade)).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

}
