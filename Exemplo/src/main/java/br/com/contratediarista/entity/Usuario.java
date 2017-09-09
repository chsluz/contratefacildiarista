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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.contratediarista.enuns.TipoUsuario;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "uid", nullable = false)
	private String uid;

	@Size(max = 100, message = "Nome não pode conter mais que 100 caracteres")
	@NotBlank(message = "{nome} {e.obrigatorio}")
	@Column(name = "nome", nullable = false)
	private String nome;

	@NotEmpty(message = "Cpf é obrigatório")
	@Size(max = 15, message = "Cpf não pode conter mais que 15 dígitos")
	@Column(name = "cpf", nullable = false, unique = true)
	private String cpf;

	@NotNull(message = "Tipo de usuário é Obrigatório")
	@Column(name = "id_tipo_usuario", nullable = false)
	private TipoUsuario tipoUsuario;

	@NotNull(message = "Endereço é Obrigatório")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_endereco")
	@Cascade(CascadeType.ALL)
	private Endereco endereco;

	@NotNull(message = "Data de nascimento é Obrigatório")
	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento")
	private Date dataNascimento;

	@Size(max = 12, message = "Telefone não pode conter mais que 12 dígitos")
	@Column(name = "telefone")
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

}
