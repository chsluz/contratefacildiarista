package br.com.contratediarista.enuns;

public enum Ativo {
	NAO("Não"), SIM("Sim");

	private final String descricao;

	private Ativo(String descricao) {
		this.descricao = descricao;
	}

	public static Ativo getFromDescricao(String descricao) {
		for (Ativo tipo : Ativo.values()) {
			if (tipo.getDescricao().equals(descricao)) {
				return tipo;
			}
		}
		return null;
	}

	public static Ativo getValor(int ordem) {
		if (NAO.ordinal() == ordem) {
			return NAO;
		}
		if (SIM.ordinal() == ordem) {
			return SIM;
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
