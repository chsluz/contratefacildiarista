package br.com.contratediarista.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import br.com.contratediarista.dao.BairroDao;
import br.com.contratediarista.entity.Bairro;

@FacesConverter(value="bairroConverter")
public class BairroConverter implements Converter {
	@Inject BairroDao bairroDao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		try{
            return bairroDao.restoreById(Integer.parseInt(value));
        } catch(NumberFormatException ex){
            return null;
        }
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		 Bairro bairro = (Bairro) value;
	        try{
	            return String.valueOf(bairro.getId());
	        } catch(Exception e){
	            return null;
	        }
	}

}
