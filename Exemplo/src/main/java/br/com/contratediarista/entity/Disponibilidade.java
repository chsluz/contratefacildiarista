package br.com.contratediarista.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotEmpty;

import com.google.gson.annotations.Expose;

import br.com.contratediarista.enuns.TipoPeriodo;

@Entity
@Table(name = "disponibilidade")
public class Disponibilidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	@Expose
	private int id;

	@NotNull(message = "Data é Obrigatório")
	@Temporal(TemporalType.DATE)
	@Column(name = "data", nullable = false)
	@Expose
	private Date data;

	@NotNull(message = "Usuário é Obrigatório")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "uid_usuario")
	@Expose
	private Usuario prestador;

	@NotNull(message = "Endereço é Obrigatório")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_endereco")
	@Cascade(CascadeType.ALL)
	@Expose
	private Endereco endereco;

	@NotNull(message = "Período é Obrigatório")
	@Column(name = "id_tipo_periodo", nullable = false)
	@Expose
	private TipoPeriodo tipoPeriodo;

	@NotNull(message = "Valor do período é Obrigatório")
	@Column(name = "valor_periodo", nullable = false)
	@Expose
	private Integer valorPeriodo;

	@NotEmpty(message = "Tipos de Atividade é Obrigatório")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "disponibilidade_has_atividade", joinColumns = {
			@JoinColumn(name = "id_disponibilidade") }, inverseJoinColumns = {
					@JoinColumn(name = "id_tipo_atividade") })
	@Expose
	private List<TipoAtividade> tiposAtividade;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Usuario getPrestador() {
		return prestador;
	}

	public void setPrestador(Usuario prestador) {
		this.prestador = prestador;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public TipoPeriodo getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

	public Integer getValorPeriodo() {
		return valorPeriodo;
	}

	public void setValorPeriodo(Integer valorPeriodo) {
		this.valorPeriodo = valorPeriodo;
	}

	public List<TipoAtividade> getTiposAtividade() {
		return tiposAtividade;
	}

	public void setTiposAtividade(List<TipoAtividade> tiposAtividade) {
		this.tiposAtividade = tiposAtividade;
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
		Disponibilidade other = (Disponibilidade) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
