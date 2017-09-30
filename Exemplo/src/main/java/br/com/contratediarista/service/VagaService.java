package br.com.contratediarista.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.com.contratediarista.dao.TipoAtividadeDao;
import br.com.contratediarista.dao.VagaDao;
import br.com.contratediarista.entity.Rotina;
import br.com.contratediarista.entity.TipoAtividade;
import br.com.contratediarista.entity.Vaga;
import br.com.contratediarista.enuns.DiasSemana;
import br.com.contratediarista.enuns.TipoPeriodo;

@RequestScoped
@Path("vaga")
public class VagaService implements Serializable {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private static final long serialVersionUID = 1L;

	@Inject
	private VagaDao vagaDao;

	@Inject
	TipoAtividadeDao tipoAtividadeDao;

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
					tipoPeriodo =  TipoPeriodo.getFromDescricao(jsonObject.get("periodo").getAsString());
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
				return Response.status(Status.OK).entity(rotinas).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.build();
		}
	}

}
