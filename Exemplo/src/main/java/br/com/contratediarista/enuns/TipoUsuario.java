package br.com.contratediarista.enuns;

public enum TipoUsuario {
	ADMINISTRADOR(
			"Administrador"),
	MODERADOR(
			"Moderador"),
	PRESTADOR(
			"Prestador"),
	CONTRATANTE(
			"Contratante");

	private final String descricao;

	private TipoUsuario(String descricao) {
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
