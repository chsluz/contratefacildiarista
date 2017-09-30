package br.com.contratediarista.enuns;

public enum TipoPeriodo {
	MANHA(
			"Manhã"),
	TARDE(
			"Tarde"),
	INTEGRAL(
			"Integral");

	private final String descricao;

	private TipoPeriodo(String descricao) {
		this.descricao = descricao;
	}
	
	public static TipoPeriodo getFromDescricao(String descricao) {
		for(TipoPeriodo tipo : TipoPeriodo.values()) {
			if(tipo.getDescricao().equals(descricao)) {
				return tipo;
			}
		}
		return null;
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
