package br.com.contratediarista.service;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import br.com.contratediarista.dao.RotinaDao;
import br.com.contratediarista.dao.TipoAtividadeDao;
import br.com.contratediarista.dao.UsuarioDao;
import br.com.contratediarista.dao.VagaDao;
import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.TipoAtividade;
import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.entity.Vaga;
import br.com.contratediarista.enuns.DiasSemana;
import br.com.contratediarista.enuns.TipoPeriodo;

@RequestScoped
@Path("/vaga")
public class VagaService implements Serializable {
	Type typeRotina = new TypeToken<List<Rotina>>() {
	}.getType();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Gson gson = new Gson();
	Gson gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd").create();

	private static final long serialVersionUID = 1L;

	@Inject
	private VagaDao vagaDao;
	
	@Inject
	private RotinaDao rotinaDao;

	@Inject
	TipoAtividadeDao tipoAtividadeDao;

	@Inject
	UsuarioDao usuarioDao;

	public VagaService() {

	}

	@POST
	@Path("/salvar")
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

	@POST
	@Path("/cadastrar-vaga")
	public Response cadastrarVaga(String json) {
		Date dataInicial = null;
		Date dataFinal = null;
		Usuario usuario = null;
		TipoPeriodo periodo = null;
		int valorPeriodo = 0;
		Vaga vaga = new Vaga();
		List<TipoAtividade> tiposAtividade = new ArrayList<>();
		List<DiasSemana> diasSelecionados = new ArrayList<>();
		try {
			JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

			if (jsonObject.get("uid") != null) {
				String uidUsuario = jsonObject.get("uid").getAsString();
				usuario = usuarioDao.buscarUsuarioPorChave(uidUsuario);
			}
			if(jsonObject.get("valorPeriodo") != null) {
				valorPeriodo = jsonObject.get("valorPeriodo").getAsInt();
			}
			if (jsonObject.get("dataInicial") != null) {
				String strData = jsonObject.get("dataInicial").getAsString();
				dataInicial = sdf.parse(strData);
			}
			if (jsonObject.get("dataFinal") != null) {
				String strData = jsonObject.get("dataFinal").getAsString();
				dataFinal = sdf.parse(strData);
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
			if (jsonObject.get("diasSelecionados") != null) {
				JsonArray arrayDiasSelecionados = jsonObject.get("diasSelecionados").getAsJsonArray();
				if (arrayDiasSelecionados != null) {
					for (int i = 0; i < arrayDiasSelecionados.size(); i++) {
						JsonObject dias = arrayDiasSelecionados.get(i).getAsJsonObject();
						if (dias != null) {
							if (dias.get("id") != null) {
								int id = dias.get("id").getAsInt();
								DiasSemana dia = DiasSemana.getValor(id);
								diasSelecionados.add(dia);
							}
						}
					}
				}
			}
			
			if(tiposAtividade.isEmpty()) {
			 	tiposAtividade.addAll(tipoAtividadeDao.listarTodos());
			}
			vaga.setEndereco(usuario.getEndereco());
			vaga.setContratante(usuario);
			vaga.setValorPeriodo(valorPeriodo);
			vaga.setDataCadastrada(new Date());
			vaga.setTipoPeriodo(periodo);
			vaga.setTiposAtividade(new HashSet<>(tiposAtividade));
			vaga.setRotinas(new HashSet<>());
			LocalDate dataIni = LocalDate.fromDateFields(dataInicial);
			LocalDate dataFin = LocalDate.fromDateFields(dataFinal);
			List<Date> dias = new ArrayList<>();
			for (LocalDate data = dataIni; data.isBefore(dataFin); data = data.plusDays(1)) {
				dias.add(data.toDate());
			}
			dias.add(dataFin.toDate());
			for (Date data : dias) {
				LocalDate dataSelecionada = LocalDate.fromDateFields(data);
				DiasSemana diaSemana = DiasSemana.getValor(dataSelecionada.getDayOfWeek());
				if (diasSelecionados.contains(diaSemana)) {
					Rotina rotina = new Rotina();
					rotina.setData(data);
					rotina.setDiaSemana(diaSemana);
					rotina.setVaga(vaga);
					vaga.getRotinas().add(rotina);
				}
			}
			vagaDao.salvar(vaga);
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
			Vaga vaga = vagaDao.restoreById(id);
			if (vaga != null) {
				return Response.ok(vaga).build();
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
	@Path("/buscar-vagas-disponiveis")
	public Response buscarVagasDisponiveisPorDataValor(String json) {
		try {
			Date dataInicial = null;
			Date dataFinal = null;
			Integer valorInicial = null;
			Integer valorFinal = null;
			TipoPeriodo tipoPeriodo = null;
			List<TipoAtividade> tiposAtividades = null;
			List<DiasSemana> diasSemanas = null;
			JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
			if (jsonObject != null) {
				if (jsonObject.get("dataInicial") != null) {
					dataInicial = sdf.parse(jsonObject.get("dataInicial").getAsString());
				}
				if (jsonObject.get("dataFinal") != null) {
					dataFinal = sdf.parse(jsonObject.get("dataFinal").getAsString());
				}
				if (jsonObject.get("valorInicial") != null) {
					valorInicial = jsonObject.get("valorInicial").getAsInt();
				}
				if (jsonObject.get("valorFinal") != null) {
					valorFinal = jsonObject.get("valorFinal").getAsInt();
				}
				if (jsonObject.get("diasSelecionados") != null) {
					JsonArray arrayDias = jsonObject.get("diasSelecionados").getAsJsonArray();
					if (arrayDias != null) {
						diasSemanas = new ArrayList<>();
						for (int i = 0; i < arrayDias.size(); i++) {
							DiasSemana dia = DiasSemana.toDiaSemanaJson(arrayDias.get(i).getAsJsonObject());
							diasSemanas.add(dia);
						}
					}
				}
				if (jsonObject.get("periodo") != null) {
					tipoPeriodo = TipoPeriodo.getValor(jsonObject.get("periodo").getAsInt());
				}
				if (jsonObject.get("tiposAtividade") != null) {
					JsonArray arrayTiposAtividade = jsonObject.get("tiposAtividade").getAsJsonArray();
					tiposAtividades = new ArrayList<>();
					if (arrayTiposAtividade != null) {
						for (int i = 0; i < arrayTiposAtividade.size(); i++) {
							TipoAtividade tipoAtividade = TipoAtividade
									.toTipoAtividadeGson(arrayTiposAtividade.get(i).getAsJsonObject());
							if (tipoAtividade.getId() > 0) {
								tipoAtividade = tipoAtividadeDao.restoreById(tipoAtividade.getId());
								tiposAtividades.add(tipoAtividade);
							}
						}
					}
				}
			}
			List<Rotina> rotinas = vagaDao.buscarRotinasPorDataEValor(dataInicial, dataFinal, valorInicial, valorFinal,
					tipoPeriodo, tiposAtividades, diasSemanas);
			
			if (rotinas.isEmpty()) {
				return Response.status(Status.NO_CONTENT).build();
			} else {
				List<Rotina> rotinasRestauradas = new ArrayList<>();
				for(Rotina r : rotinas) {
					rotinasRestauradas.add(rotinaDao.restoreById(r.getId()));
				}
				String retorno = gsonBuilder.toJson(rotinasRestauradas, typeRotina);
				return Response.status(Status.OK).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
				.entity(gson.fromJson(retorno, typeRotina)).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

}
