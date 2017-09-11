package br.com.contratediarista.lazy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class AbstractLazyDataModel<T> extends LazyDataModel<T> {

	private static final long serialVersionUID = 1L;

	private List<T> dataSource;

	public AbstractLazyDataModel(List<T> dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public T getRowData(String rowKey) {
		for (T tipo : dataSource) {
			Field field;
			try {
				field = tipo.getClass().getDeclaredField("id");
				field.setAccessible(true);
				Object value = field.get(tipo);
				if (rowKey.equals(String.valueOf(value))) {
					return tipo;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(T tipo) {
		try {
			Field field = tipo.getClass().getDeclaredField("id");
			field.setAccessible(true);
			Object value = field.get(tipo);
			return value;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		List<T> data = new ArrayList<T>();

		//filter
		for (T tipo : dataSource) {
			boolean match = true;

			if (filters != null) {
				for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
					try {
						String filterProperty = it.next();
						Object filterValue = filters.get(filterProperty);
						Field field = tipo.getClass().getDeclaredField(filterProperty);
						field.setAccessible(true);
						Object value = field.get(tipo);
						String fieldValue = String.valueOf(value);

						if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
							match = true;
						} else {
							match = false;
							break;
						}
					} catch (Exception e) {
						match = false;
					}
				}
			}

			if (match) {
				data.add(tipo);
			}
		}

		//sort
		if (sortField != null) {
			Collections.sort(data, new Comparator() {

				@Override
				public int compare(Object o1, Object o2) {
					try {

						Object value1 = o1.getClass().getField(sortField).get(o1);
						Object value2 = o2.getClass().getField(sortField).get(o2);
						int value = ((Comparable) value1).compareTo(value2);
						return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
					} catch (Exception e) {
						throw new RuntimeException();
					}
				}
			});
		}

		//rowCount
		int dataSize = data.size();
		this.setRowCount(dataSize);

		//paginate
		if (dataSize > pageSize) {
			try {
				return data.subList(first, first + pageSize);
			} catch (IndexOutOfBoundsException e) {
				return data.subList(first, first + (dataSize % pageSize));
			}
		} else {
			return data;
		}
	}

}
