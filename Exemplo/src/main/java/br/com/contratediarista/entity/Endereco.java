package br.com.contratediarista.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "endereco")
public class Endereco implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	@Expose
	private Long id;

	@NotEmpty(message = "Rua é obrigatório")
	@Size(max = 100, message = "Rua não pode conter mais que 100 caracteress")
	@Column(name = "rua", length = 100, nullable = false)
	@Expose
	private String rua;

	@Column(name = "numero", length = 8)
	@Expose
	private Integer numero;

	@NotEmpty(message = "Cep é obrigatório")
	@Size(max = 10, message = "Cep não pode conter mais que 10 dígitos")
	@Column(name = "cep", length = 10, nullable = false)
	@Expose
	private String cep;

	@Size(max = 100, message = "Complemento não pode conter mais que 100 caracteres")
	@Column(name = "complemento", length = 100)
	@Expose
	private String complemento;

	@NotNull(message = "Bairro é obrigatório")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_bairro")
	@Expose
	private Bairro bairro;

	@Column(name = "latitude")
	@Expose
	private Double latitude = 0.0;

	@Column(name = "longitude")
	@Expose
	private Double longitude = 0.0;
	
	public Endereco() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((rua == null) ? 0 : rua.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Endereco other = (Endereco) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (rua == null) {
			if (other.rua != null) {
				return false;
			}
		} else if (!rua.equals(other.rua)) {
			return false;
		}
		return true;
	}
	
	public static Endereco copy(Endereco endereco) {
		Endereco copia = new Endereco();
		copia.setBairro(endereco.getBairro());
		copia.setCep(endereco.getCep());
		copia.setComplemento(endereco.getComplemento());
		copia.setLatitude(endereco.getLatitude());
		copia.setLongitude(endereco.getLongitude());
		copia.setNumero(endereco.getNumero());
		copia.setRua(endereco.getRua());
		return copia;
	}

}
