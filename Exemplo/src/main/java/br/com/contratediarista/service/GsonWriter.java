package br.com.contratediarista.service;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;

/**
 * Provider específico para utilizar o Gson na serialização dos objetos para JSON
 *
 * @author geraldo
 *
 * @param <T>
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class GsonWriter<T> implements MessageBodyWriter<T> {

	@Override
	public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		httpHeaders.get("Content-Type").add("application/json; charset=UTF-8");
		Gson gson = new Gson();
		entityStream.write(gson.toJson(t).getBytes("UTF-8"));
	}

	@Override
	public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}
}
