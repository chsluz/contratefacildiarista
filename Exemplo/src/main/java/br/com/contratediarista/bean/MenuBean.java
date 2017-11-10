package br.com.contratediarista.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import br.com.contratediarista.entity.Usuario;
import br.com.contratediarista.enuns.TipoUsuario;
import br.com.contratediarista.utils.FacesUtil;

@Named
@SessionScoped
public class MenuBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesUtil facesUtil;

	MenuModel menuModel;
	
	public MenuBean() {
	}

	@PostConstruct
	public void init() {
		Usuario usuarioLogado = facesUtil.getUsuarioLogado();
		TipoUsuario tipo = usuarioLogado.getTipoUsuario();

		menuModel = new DefaultMenuModel();

		DefaultSubMenu cadastro = new DefaultSubMenu(facesUtil.getLabel("cadastrar"));
		DefaultMenuItem item;
		if (tipo == TipoUsuario.CONTRATANTE) {
			item = new DefaultMenuItem(facesUtil.getLabel("vaga"));
			item.setHref("../../paginas/contratante/cadastrar_vaga.jsf");
			item.setIcon("ui-icon-home");
			cadastro.addElement(item);
			
			item = new DefaultMenuItem(facesUtil.getLabel("tipo.de.atividade"));
			item.setHref("../../paginas/publico/tipo_atividade.jsf");
			item.setIcon("ui-icon-home");
			cadastro.addElement(item);
			
			item = new DefaultMenuItem(facesUtil.getLabel("cad.solicitacao"));
			item.setHref("../../paginas/publico/solicitacao_reclamacao.jsf");
			item.setIcon("ui-icon-home");
			cadastro.addElement(item);
		}
		if (tipo == TipoUsuario.PRESTADOR) {
			item = new DefaultMenuItem(facesUtil.getLabel("disponibilidade"));
			item.setHref("../../paginas/prestador/cadastrar_disponibilidade.jsf");
			item.setIcon("ui-icon-home");
			cadastro.addElement(item);
			
			item = new DefaultMenuItem(facesUtil.getLabel("tipo.de.atividade"));
			item.setHref("../../paginas/publico/tipo_atividade.jsf");
			item.setIcon("ui-icon-home");
			cadastro.addElement(item);
			
			item = new DefaultMenuItem(facesUtil.getLabel("cad.solicitacao"));
			item.setHref("../../paginas/publico/solicitacao_reclamacao.jsf");
			item.setIcon("ui-icon-home");
			cadastro.addElement(item);
		}

		

		DefaultSubMenu consulta = new DefaultSubMenu(facesUtil.getLabel("consultar"));
		if (tipo == TipoUsuario.PRESTADOR) {
			item = new DefaultMenuItem(facesUtil.getLabel("vaga"));
			item.setHref("../../paginas/prestador/consultar_vaga.jsf");
			item.setIcon("ui-icon-home");
			consulta.addElement(item);
			item = new DefaultMenuItem("Vagas Pretendidas");
			item.setHref("../../paginas/prestador/visualizar_vagas_ja_candidato.jsf");
			item.setIcon("ui-icon-home");
			consulta.addElement(item);

			item = new DefaultMenuItem("Minha Agenda");
			item.setHref("../../paginas/prestador/visualizar_vagas_vinculadas.jsf");
			item.setIcon("ui-icon-home");
			consulta.addElement(item);
			
			item = new DefaultMenuItem(facesUtil.getLabel("disponibilidade"));
			item.setHref("../../paginas/prestador/consultar_disponibilidades_cadastradas.jsf");
			item.setIcon("ui-icon-home");
			consulta.addElement(item);
		}

		if (tipo == TipoUsuario.CONTRATANTE) {
			item = new DefaultMenuItem(facesUtil.getLabel("aprovacao.vaga"));
			item.setHref("../../paginas/contratante/aprovacao_vaga.jsf");
			item.setIcon("ui-icon-home");
			consulta.addElement(item);
			
			item = new DefaultMenuItem("Vagas cadastradas");
			item.setHref("../../paginas/contratante/consultar_vagas_cadastradas.jsf");
			item.setIcon("ui-icon-home");
			consulta.addElement(item);

			item = new DefaultMenuItem(facesUtil.getLabel("disponibilidade.prestador"));
			item.setHref("../../paginas/contratante/visualizar_disponibilidade_prestador.jsf");
			item.setIcon("ui-icon-home");
			consulta.addElement(item);
			
			item = new DefaultMenuItem("Minha Agenda");
			item.setHref("../../paginas/contratante/visualizar_vagas_prestador_aprovado.jsf");
			item.setIcon("ui-icon-home");
			consulta.addElement(item);
		}

		menuModel.addElement(cadastro);
		menuModel.addElement(consulta);
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

}
