package br.com.contratediarista.enuns;

public enum TipoSolicitacao {
	SOLICITACAO(
			"Solicitação"),
	RECLAMACAO(
			"Reclamação");

	private final String descricao;

	private TipoSolicitacao(String descricao) {
		this.descricao = descricao;
	}
	
	public static TipoSolicitacao getFromDescricao(String descricao) {
		for(TipoSolicitacao tipo : TipoSolicitacao.values()) {
			if(tipo.getDescricao().equals(descricao)) {
				return tipo;
			}
		}
		return null;
	}
	
	public static TipoSolicitacao getValor(int ordem) {
		if (SOLICITACAO.ordinal() == ordem) {
			return SOLICITACAO;
		}
		if (RECLAMACAO.ordinal() == ordem) {
			return RECLAMACAO;
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
