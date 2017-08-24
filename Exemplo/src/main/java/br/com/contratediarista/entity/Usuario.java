package br.com.contratediarista.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.contratediarista.enuns.TipoUsuario;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String uid;

	@Size(max = 100, message = "Nome não pode conter mais que 100 caracteres")
	@NotNull(message = "Nome é Obrigatório")
	@Column(name = "nome", nullable = false)
	private String nome;

	@Size(max = 15, message = "Cpf não pode conter mais que 11 dígitos")
	private String cpf;

	@NotNull(message = "Tipo de usuário é Obrigatório")
	@Column(name = "id_tipo_usuario", nullable = false)
	private TipoUsuario tipoUsuario;

	@NotNull(message = "Endereço é Obrigatório")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_endereco")
	private Endereco endereco;

	@NotNull(message = "Data de nascimento é Obrigatório")
	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento")
	private Date dataNascimento;

	@Size(max = 12, message = "Telefone não pode conter mais que 12 dígitos")
	@Column(name = "telefone")
	private String telefone;

	@Column(name = "latitude")
	private Double latitude = 0.0;

	@Column(name = "longitude")
	private Double longitude = 0.0;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
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
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
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
		Usuario other = (Usuario) obj;
		if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		if (uid == null) {
			if (other.uid != null) {
				return false;
			}
		} else if (!uid.equals(other.uid)) {
			return false;
		}
		return true;
	}

}
