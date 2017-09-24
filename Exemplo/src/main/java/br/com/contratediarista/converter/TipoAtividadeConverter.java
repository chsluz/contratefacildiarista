package br.com.contratediarista.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.contratediarista.entity.TipoAtividade;

@FacesConverter("tipoAtividadeConverter")
public class TipoAtividadeConverter implements Converter {

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
			TipoAtividade tipoAtividade = (TipoAtividade) value;
			component.getAttributes().put(String.valueOf(tipoAtividade.getId()), tipoAtividade);
			return String.valueOf(tipoAtividade.getId());
		} catch (Exception e) {
			return null;
		}
	}
}
