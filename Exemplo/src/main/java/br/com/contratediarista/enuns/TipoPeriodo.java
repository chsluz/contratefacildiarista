package br.com.contratediarista.enuns;

public enum TipoPeriodo {
	MANHA("Manhã"), TARDE("Tarde"), INTEGRAL("Noite");

	private final String descricao;

	private TipoPeriodo(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Obtem o valor de descricao.
	 *
	 * @return O valor de descricao.
	 */
	public String getDescricao() {
		return descricao;
	}

}
