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
import br.com.contratediarista.utils.FacesUtil;

@Named
@SessionScoped
public class MenuBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesUtil facesUtil;

	MenuModel menuModel;

	@PostConstruct
	public void init() {
		Usuario usuarioLogado = facesUtil.getUsuarioLogado();
		menuModel = new DefaultMenuModel();
		//First submenu
		DefaultSubMenu cadastro = new DefaultSubMenu(facesUtil.getLabel("cadastrar"));

		DefaultMenuItem item = new DefaultMenuItem(facesUtil.getLabel("vaga"));
		item.setHref("../../paginas/contratante/cadastrar_vaga.jsf");
		item.setIcon("ui-icon-home");
		cadastro.addElement(item);

		item = new DefaultMenuItem(facesUtil.getLabel("tipo.de.atividade"));
		item.setHref("../../paginas/publico/tipo_atividade.jsf");
		item.setIcon("ui-icon-home");
		cadastro.addElement(item);

		DefaultSubMenu consulta = new DefaultSubMenu(facesUtil.getLabel("consultar"));
		item = new DefaultMenuItem(facesUtil.getLabel("vaga"));
		item.setHref("../../paginas/prestador/consultar_vaga.jsf");
		item.setIcon("ui-icon-home");
		consulta.addElement(item);
		
		item = new DefaultMenuItem(facesUtil.getLabel("aprovacao.vaga"));
		item.setHref("../../paginas/contratante/aprovacao_vaga.jsf");
		item.setIcon("ui-icon-home");
		consulta.addElement(item);

		menuModel.addElement(cadastro);
		menuModel.addElement(consulta);
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

}
