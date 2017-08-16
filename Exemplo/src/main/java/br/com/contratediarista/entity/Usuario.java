package br.com.contratediarista.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

	private String nome;

	private String cpf;

	@Column(name = "id_tipo_usuario")
	private TipoUsuario tipoUsuario;

	private String telefone;

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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

}
