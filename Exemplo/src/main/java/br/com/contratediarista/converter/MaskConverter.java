package br.com.contratediarista.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="maskConverter")
public class MaskConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return String.valueOf(value).replace("(","").replace(")", "").replace("-", "");
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return String.valueOf(value).replace("(","").replace(")", "").replace("-", "");
	}

}
