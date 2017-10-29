/**
 *
 */
package br.com.contratediarista.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.primefaces.event.SelectEvent;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.contratediarista.dao.BairroDao;
import br.com.contratediarista.entity.Bairro;
import br.com.contratediarista.entity.Cep;
import br.com.contratediarista.entity.Cidade;
import br.com.contratediarista.entity.Endereco;
import br.com.contratediarista.entity.Estado;
import br.com.contratediarista.service.BairroService;
import br.com.contratediarista.service.CidadeService;
import br.com.contratediarista.service.EstadoService;

/**
 * @author Henrique 21-08-2017
 *
 */

/**
 * Bean utilizada no componente de cadastro de endereço
 *
 * @author Henrique 28-08-2017
 *
 */

@SuppressWarnings("unchecked")
@Named
@ViewScoped
public class BuscaEnderecoBean implements Serializable {

	static final String URL_WEBSERVICE_CEP = "https://viacep.com.br/ws/";
	static final String URL_BUSCAR_CORDENADAS = "https://maps.googleapis.com/maps/api/geocode/json?address=";
	static final String FORMATO_RETORNO = "/json/";

	@Inject
	private BairroService bairroService;

	@Inject
	private CidadeService cidadeService;

	@Inject
	private EstadoService estadoService;

	@Inject
	private BairroDao bairroDao;

	private Estado estado;

	private Cidade cidade;

	//
	// @Inject
	// private CidadeWebService cidadeWebService;
	//
	// private Gson gson;
	//

	//
	// /**
	// *
	// */
	public BuscaEnderecoBean() {

	}

	@PostConstruct
	public void init() {
		endereco = new Endereco();
		estado = new Estado();
		cidade = new Cidade();
	}

	private static final long serialVersionUID = 1L;

	private Endereco endereco;

	public List<Estado> getListarEstados() {
		try {
			Response response = null;
			List<Estado> estados = new ArrayList<>();
			response = estadoService.listAll();
			if (response != null && response.getEntity() != null) {
				estados = (List<Estado>) response.getEntity();
			}
			return estados;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Cidade> getListarCidades() {
		try {
			Response response = null;
			List<Cidade> cidades = new ArrayList<>();
			if (estado != null) {
				response = cidadeService.listByIdEstado(estado.getId());
				if (response != null && response.getEntity() != null) {
					cidades = (List<Cidade>) response.getEntity();
				}
			}
			return cidades;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Bairro> getListarBairros() {

		try {
			Response response = null;
			List<Bairro> bairros = new ArrayList<>();
			if (cidade != null) {
				response = bairroService.listByIdCidade(cidade.getId());
				if (response != null && response.getEntity() != null) {
					bairros = (List<Bairro>) response.getEntity();
				}
			}
			return bairros;
		} catch (Exception e) {
			return null;
		}
	}

	public void selectEstado(SelectEvent event) {
		estado = (Estado) event.getObject();
	}

	public void selectCidade(SelectEvent event) {
		cidade = (Cidade) event.getObject();
	}
	//
	// public void instanciarNovo() {
	// gson = new Gson();
	// endereco = new Endereco();
	// endereco.setBairro(new Bairro());
	// endereco.setCidade(new Cidade());
	// cep = null;
	// }
	//

	//
	// /**
	// * Método responsavel por setar a cidade.
	// * para que possa ser executado o serviço de buscar as ruas pela descrição,
	// * em conjunto com a cidade selecionada
	// *
	// * @param event
	// */
	// public void handleSelect(SelectEvent event) {
	// if (endereco != null) {
	// endereco.setCidade((Cidade) event.getObject());
	// }
	// }
	//
	// /***
	// * Método responsável por atribuir o logradouro selecionado no auto complete
	// do componente de enredeço
	// *
	// * @param event
	// * @throws MagicTradeException
	// */
	// public void selecionarRua(SelectEvent event) throws MagicTradeException {
	// cep = (Cep) event.getObject();
	// endereco.setCep(cep.getCep());
	// endereco.setLogradouro(cep.getLogradouro());
	// Response resultadoBairro = bairroWebService.autoComplete(cep.getBairro());
	// if (resultadoBairro.getEntity() != null) {
	// List<Bairro> bairros = (List<Bairro>) resultadoBairro.getEntity();
	// if (CollectionUtils.isNotEmpty(bairros)) {
	// endereco.setBairro(bairros.get(0));
	// }
	// }
	// }
	//
	// /**
	// * Método responsável por buscar uma lista de ruas que se encaixem na
	// descrição digitada
	// *
	// * @param descricao
	// * @return List<String> com as ruas
	// */
	// public List<Cep> autoCompleteRua(String descricao) {
	// if (descricao.length() < 4) {
	// return null;
	// }
	// if (endereco.getCidade().getId() == null) {
	// return null;
	// } else {
	// Client cliente = ClientBuilder.newClient();
	// WebTarget target = cliente.target(montarUrlRua(endereco.getCidade(),
	// descricao));
	// String jsonResponse = target.request()
	// .accept(MediaType.APPLICATION_JSON).get(String.class);
	// Type listType = new TypeToken<ArrayList<Cep>>() {
	// }.getType();
	// List<Cep> ceps = gson.fromJson(jsonResponse, listType);
	// return ceps;
	// }
	// }
	//
	// /**
	// * Método responsável por buscar as informações de endereço a partir do cep
	// digitado,
	// * caso cep seja encontrado o sistema atribuirá para seus respectivos lugares
	// as informações retornadas pelo serviço de busca cep
	// *
	// * @throws MagicTradeException
	// */
	public void buscarCep() {
		try {
			String cep = endereco.getCep().replace(".", "").replace("-", "");
			String url = montarUrlCep(cep);

			Client cliente = ClientBuilder.newClient();
			WebTarget target = cliente.target(url);

			String jsonResponse = target.request().accept(MediaType.APPLICATION_JSON).get(String.class);

			if (!jsonResponse.isEmpty()) {
				Cep cepJson = new Gson().fromJson(jsonResponse, Cep.class);
				endereco.setRua(cepJson.getLogradouro());
				Response resultadoEstado = estadoService.restoreBySigla(cepJson.getUf());
				if (resultadoEstado.getStatus() == Status.OK.getStatusCode() && resultadoEstado.getEntity() != null) {
					estado = (Estado) resultadoEstado.getEntity();
					Response resultadoCidade = cidadeService.restoreByNomeEEstado(cepJson.getLocalidade(),
							estado.getId());
					if (resultadoCidade.getStatus() == Status.OK.getStatusCode()
							&& resultadoCidade.getEntity() != null) {
						cidade = (Cidade) resultadoCidade.getEntity();
						Response resultadoBairro = bairroService.restoreByDescricaoCidade(cepJson.getBairro(),
								cidade.getId());
						Bairro bairro = null;
						if (resultadoBairro.getStatus() == Status.OK.getStatusCode()
								&& resultadoBairro.getEntity() != null) {
							bairro = (Bairro) resultadoBairro.getEntity();
						}
						if (bairro == null) {
							Bairro novo = new Bairro();
							novo.setCidade(cidade);
							novo.setDescricao(cepJson.getBairro());
							try {
								bairroDao.salvar(novo);
								bairro = bairroDao.restoreByDescricaoCidade(cepJson.getBairro(), cidade.getId());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (bairro != null) {
							endereco.setBairro(bairro);
							buscarCoordenadas();
						}
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/***
	 * Método responsável por buscar as coordenadas do cadastro de endereço ao
	 * clicar no botão pesquisar da latitude e longitude
	 *
	 * @throws MagicTradeException
	 */
	public void buscarCoordenadas() throws Exception {
		StringBuilder busca = new StringBuilder(endereco.getRua().trim());
		if (endereco.getNumero() != null) {
			busca.append(" ").append(endereco.getNumero());
		}
		if (endereco.getBairro().getDescricao() != null) {
			busca.append(" ").append(endereco.getBairro().getDescricao().trim());
		}
		busca.append(" ").append(cidade.getNome().trim()).append(" ").append(estado.getSigla().trim());

		String urlModificada;
		try {
			urlModificada = URLEncoder.encode(busca.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new Exception("erro.ao.converter.url");
		}
		String url = new StringBuilder().append(URL_BUSCAR_CORDENADAS).append(urlModificada).toString();

		Client cliente = ClientBuilder.newClient();
		WebTarget target = cliente.target(url);

		String jsonResponse = target.request().accept(MediaType.APPLICATION_JSON).get(String.class);

		if (!jsonResponse.isEmpty() && !jsonResponse.contains("ZERO_RESULTS")) {
			JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);
			JsonElement results = jsonObject.getAsJsonArray("results").get(0);
			JsonElement geometry = results.getAsJsonObject().get("geometry");
			JsonElement location = geometry.getAsJsonObject().get("location");
			endereco.setLatitude(location.getAsJsonObject().get("lat").getAsDouble());
			endereco.setLongitude(location.getAsJsonObject().get("lng").getAsDouble());
		}
	}

	private String montarUrlCep(String cep) {
		StringBuilder montarUrl = new StringBuilder(URL_WEBSERVICE_CEP);
		montarUrl.append(cep);
		montarUrl.append(FORMATO_RETORNO);
		return montarUrl.toString();
	}
	//
	// public String montarUrlRua(Cidade cidade, String nome) {
	// StringBuilder montarUrl = new StringBuilder(URL_WEBSERVICE_CEP);
	// montarUrl.append(cidade.getEstado().getSigla()).append("/");
	// montarUrl.append(cidade.getNome()).append("/").append(nome);
	// montarUrl.append(FORMATO_RETORNO);
	// return montarUrl.toString();
	// }
	//
	// public Cep getCep() {
	// return cep;
	// }
	//
	// public void setCep(Cep cep) {
	// this.cep = cep;
	// }

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

}
