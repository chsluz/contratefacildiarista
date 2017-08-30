package br.com.contratediarista.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.contratediarista.entity.Bairro;

@FacesConverter(value = "bairroConverter")
public class BairroConverter implements Converter {

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
			Bairro bairro = (Bairro) value;
			component.getAttributes().put(String.valueOf(bairro.getId()), bairro);
			return String.valueOf(bairro.getId());
		} catch (Exception e) {
			return null;
		}
	}

}
