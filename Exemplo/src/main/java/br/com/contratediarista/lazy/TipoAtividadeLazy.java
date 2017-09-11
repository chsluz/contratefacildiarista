package br.com.contratediarista.lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.contratediarista.entity.TipoAtividade;

@SuppressWarnings({"unchecked","rawtypes"})
public class TipoAtividadeLazy extends LazyDataModel<TipoAtividade>{


	private static final long serialVersionUID = 1L;
	
	private List<TipoAtividade> dataSource;

	public TipoAtividadeLazy(List<TipoAtividade> dataSource) {
		this.dataSource = dataSource;
	}
	
	 @Override
	    public TipoAtividade getRowData(String rowKey) {
	        for(TipoAtividade tipoAtividade : dataSource) {
	        	if(rowKey.equals(String.valueOf(tipoAtividade.getId()))) {
	                return tipoAtividade;
	        	}
	        }
	        return null;
	    }
	 
	    @Override
	    public Object getRowKey(TipoAtividade tipoAtividade) {
	        return tipoAtividade.getId();
	    }
	 
	 
		@Override
	    public List<TipoAtividade> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
	        List<TipoAtividade> data = new ArrayList<TipoAtividade>();
	 
	        //filter
	        for(TipoAtividade tipo : dataSource) {
	            boolean match = true;
	 
	            if (filters != null) {
	                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
	                    try {
	                        String filterProperty = it.next();
	                        Object filterValue = filters.get(filterProperty);
	                        String fieldValue = String.valueOf(tipo.getClass().getField(filterProperty).get(tipo));
	 
	                        if(filterValue == null || fieldValue.startsWith(filterValue.toString())) {
	                            match = true;
	                    }
	                    else {
	                            match = false;
	                            break;
	                        }
	                    } catch(Exception e) {
	                        match = false;
	                    }
	                }
	            }
	 
	            if(match) {
	                data.add(tipo);
	            }
	        }
	 
	        //sort
	        if(sortField != null) {
	        	Collections.sort(data, new Comparator() {

					@Override
					public int compare(Object o1, Object o2) {
						 try {
					            Object value1 = TipoAtividade.class.getField(sortField).get(o1);
					            Object value2 = TipoAtividade.class.getField(sortField).get(o2);
								int value = ((Comparable)value1).compareTo(value2);
					            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
					        }
					        catch(Exception e) {
					            throw new RuntimeException();
					        }
					}
				});
	        }
	 
	        //rowCount
	        int dataSize = data.size();
	        this.setRowCount(dataSize);
	 
	        //paginate
	        if(dataSize > pageSize) {
	            try {
	                return data.subList(first, first + pageSize);
	            }
	            catch(IndexOutOfBoundsException e) {
	                return data.subList(first, first + (dataSize % pageSize));
	            }
	        }
	        else {
	            return data;
	        }
	    }

}
