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
public class MenuBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private FacesUtil facesUtil;
	
	MenuModel menuModel;
	
	@PostConstruct
	public void init() {
		Usuario usuarioLogado = facesUtil.getUsuarioLogado();
		menuModel = new DefaultMenuModel();
		 //First submenu
        DefaultSubMenu cadastro = new DefaultSubMenu("Cadastrar");
         
        DefaultMenuItem item = new DefaultMenuItem("Vaga");
        item.setHref("../../paginas/contratante/cadastrar_vaga.jsf");
        item.setIcon("ui-icon-home");
        cadastro.addElement(item);
        
        item = new DefaultMenuItem("Tipo de Atividade");
        item.setHref("../../paginas/publico/tipo_atividade.jsf");
        item.setIcon("ui-icon-home");
        cadastro.addElement(item);
        menuModel.addElement(cadastro);
	}
	
	public MenuModel getMenuModel() {
		return menuModel;
	}

}
