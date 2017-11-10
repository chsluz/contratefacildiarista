package br.com.contratediarista.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

@Entity
@Table(name="tipo_atividade")
public class TipoAtividade implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	@Expose
	private int id;
	
	@Size(max = 100, message = "Descrição não pode conter mais que 100 caracteres")
	@NotBlank(message = "{descricao} {e.obrigatorio}")
	@Column(name = "descricao", nullable = false)
	@Expose
	private String descricao;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoAtividade other = (TipoAtividade) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public static TipoAtividade toTipoAtividadeGson(JsonObject jsonObject) {
		TipoAtividade tipo = new TipoAtividade();
		if(jsonObject.get("id") != null) {
			tipo.setId(jsonObject.get("id").getAsInt());
		}
		return tipo;
	}
	
	
	
	

}
