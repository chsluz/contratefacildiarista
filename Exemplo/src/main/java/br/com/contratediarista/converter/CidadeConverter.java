package br.com.contratediarista.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.contratediarista.entity.Cidade;

@FacesConverter(value = "cidadeConverter")
public class CidadeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		try {
			if (value == null) {
				return null;
			}
			return component.getAttributes().get(value);
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		try {
			if (value == null) {
				return null;
			}
			Cidade cidade = (Cidade) value;
			component.getAttributes().put(String.valueOf(cidade.getId()), cidade);
			return String.valueOf(cidade.getId());
		} catch (Exception e) {
			return null;
		}
	}

}
