package br.com.contratediarista.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.google.gson.annotations.Expose;

import br.com.contratediarista.enuns.Ativo;
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
	@Expose
	private String uid;

	@Size(max = 100, message = "Nome não pode conter mais que 100 caracteres")
	@NotBlank(message = "{nome} {e.obrigatorio}")
	@Column(name = "nome", nullable = false)
	@Expose
	private String nome;

	@NotEmpty(message = "Cpf é obrigatório")
	@Size(max = 15, message = "Cpf não pode conter mais que 15 dígitos")
	@Column(name = "cpf", nullable = false, unique = true)
	@Expose
	private String cpf;

	@NotNull(message = "Tipo de usuário é Obrigatório")
	@Column(name = "id_tipo_usuario", nullable = false)
	@Expose
	private TipoUsuario tipoUsuario;
	
	@Column(name = "ativo")
	@Expose
	private Ativo ativo = Ativo.SIM;

	@NotNull(message = "Endereço é Obrigatório")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_endereco")
	@Cascade(CascadeType.ALL)
	@Expose
	private Endereco endereco;

	@NotNull(message = "Data de nascimento é Obrigatório")
	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento")
	@Expose
	private Date dataNascimento;

	@Size(max = 12, message = "Telefone não pode conter mais que 12 dígitos")
	@Column(name = "telefone")
	@Expose
	private String telefone;

	@OneToMany(fetch = FetchType.EAGER, cascade = javax.persistence.CascadeType.ALL, mappedBy = "usuario")
	@Expose
	private Set<Avaliacao> avaliacoes;
	
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

	public Ativo getAtivo() {
		return ativo;
	}

	public void setAtivo(Ativo ativo) {
		this.ativo = ativo;
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
	
	public Set<Avaliacao> getAvaliacoes() {
		return avaliacoes;
	}

	public void setAvaliacoes(Set<Avaliacao> avaliacoes) {
		this.avaliacoes = avaliacoes;
	}

	public Float getMediaAprovacaoUsuario() {
		if (avaliacoes != null && !avaliacoes.isEmpty()) {
			int av = 0;
			for (Avaliacao avaliacao : avaliacoes) {
				av = av + avaliacao.getNota();
			}
			return (float) (av / avaliacoes.size());
		} else {
			return (float) 0;
		}
	}

	public int getQuantidadeAvaliacoesUsuario() {
		return (avaliacoes != null && !avaliacoes.isEmpty()) ? avaliacoes.size() : 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
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
		Usuario other = (Usuario) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}

}
