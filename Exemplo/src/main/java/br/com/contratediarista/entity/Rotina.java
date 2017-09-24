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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import br.com.contratediarista.enuns.DiasSemana;

@Entity
@Table(name = "rotina")
public class Rotina implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@NotNull(message = "{Data é obrigatório}")
	@Temporal(TemporalType.DATE)
	@Column(name = "data")
	private Date data;

	@NotNull(message = "Dia da Semana é Obrigatório")
	@Column(name = "id_dia_semana", nullable = false)
	private DiasSemana diaSemana;

	@NotNull(message = "Vaga é Obrigatório")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_vaga")
	private Vaga vaga;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "rotina_has_prestador", joinColumns = { @JoinColumn(name = "id_rotina") }, inverseJoinColumns = {
			@JoinColumn(name = "id_usuario") })
	private List<Usuario> prestadores;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario prestadorSelecionado;

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

	public DiasSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiasSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public Vaga getVaga() {
		return vaga;
	}

	public void setVaga(Vaga vaga) {
		this.vaga = vaga;
	}

	public List<Usuario> getPrestadores() {
		return prestadores;
	}

	public void setPrestadores(List<Usuario> prestadores) {
		this.prestadores = prestadores;
	}

	public Usuario getPrestadorSelecionado() {
		return prestadorSelecionado;
	}

	public void setPrestadorSelecionado(Usuario prestadorSelecionado) {
		this.prestadorSelecionado = prestadorSelecionado;
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
		Rotina other = (Rotina) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
