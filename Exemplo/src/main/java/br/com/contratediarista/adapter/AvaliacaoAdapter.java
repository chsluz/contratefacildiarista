package br.com.contratediarista.adapter;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import br.com.contratediarista.entity.Avaliacao;

public class AvaliacaoAdapter extends TypeAdapter<Avaliacao>{

	@Override
	public Avaliacao read(JsonReader arg0) throws IOException {
		System.out.println("");
		return null;
	}

	@Override
	public void write(JsonWriter writer, Avaliacao avaliacao) throws IOException {
		
	}

}
