package br.com.contratediarista.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.contratediarista.entity.Estado;

@FacesConverter("estadoConverter")
public class EstadoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		try {
			if (value == null) {
				return value;
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
			Estado estado = (Estado) value;
			component.getAttributes().put(String.valueOf(estado.getId()), estado);
			return String.valueOf(estado.getId());
		} catch (Exception e) {
			return null;
		}
	}

}
