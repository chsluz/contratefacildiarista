package br.com.contratediarista.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.contratediarista.enuns.TipoPeriodo;

@Entity
@Table(name="vaga")
public class Vaga implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",nullable=false)
	private int id;
	
	@NotNull(message = "Data é Obrigatório")
	@Temporal(TemporalType.DATE)
	@Column(name = "data",nullable=false)
	private Date dataCadastrada;
	
	@NotNull(message = "Usuário é Obrigatório")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario contratante;
	
	@NotNull(message = "Endereço é Obrigatório")
	@ManyToOne(fetch = FetchType.LAZY,cascade= CascadeType.ALL)
	@JoinColumn(name = "id_endereco")
	private Endereco endereco;
	
	@NotNull(message = "Período é Obrigatório")
	@Column(name = "id_tipo_periodo", nullable = false)
	private TipoPeriodo tipoPeriodo;
	
	@NotNull(message="Valor do período é Obrigatório")
	@Column(name="valor_periodo",nullable=false)
	private Integer valorPeriodo;
	
	@NotEmpty(message="Tipos de Atividade é Obrigatório")
	@ManyToMany(fetch = FetchType.LAZY,cascade= CascadeType.ALL)
	@JoinTable(name = "vaga_has_atividade", joinColumns = { @JoinColumn(name = "id_vaga") }, inverseJoinColumns = {
			@JoinColumn(name = "id_tipo_atividade") })
	private List<TipoAtividade> tiposAtividade;
	
	@NotEmpty(message="Rotina é Obrigatório")
	@OneToMany(fetch = FetchType.LAZY,cascade= CascadeType.ALL,mappedBy="vaga")
	private List<Rotina> rotinas;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDataCadastrada() {
		return dataCadastrada;
	}

	public void setDataCadastrada(Date dataCadastrada) {
		this.dataCadastrada = dataCadastrada;
	}

	public Usuario getContratante() {
		return contratante;
	}

	public void setContratante(Usuario contratante) {
		this.contratante = contratante;
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

	public List<TipoAtividade> getTiposAtividade() {
		return tiposAtividade;
	}

	public void setTiposAtividade(List<TipoAtividade> tiposAtividade) {
		this.tiposAtividade = tiposAtividade;
	}

	public List<Rotina> getRotinas() {
		return rotinas;
	}

	public void setRotinas(List<Rotina> rotinas) {
		this.rotinas = rotinas;
	}
	
	public Integer getValorPeriodo() {
		return valorPeriodo;
	}

	public void setValorPeriodo(Integer valorPeriodo) {
		this.valorPeriodo = valorPeriodo;
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
		Vaga other = (Vaga) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
