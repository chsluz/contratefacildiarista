package br.com.contratediarista.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.contratediarista.enuns.TipoPeriodo;

@Named
@ViewScoped
public class BuscarVagaBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public List<TipoPeriodo> getTiposPeriodo() {
		return Arrays.asList(TipoPeriodo.values());
	}

}
