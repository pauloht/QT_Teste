package undefinied;

public enum Acoes {
	CIMA,
	BAIXO,
	ESQUERDA,
	DIREITA;
	
	private static Acoes[] listaAcoes;
	static {
		listaAcoes = new Acoes[4];
		listaAcoes[0] = CIMA;
		listaAcoes[1] = BAIXO;
		listaAcoes[2] = ESQUERDA;
		listaAcoes[3] = DIREITA;
	}

	public static Acoes getAcaoFromValor(int valor) {
		return(listaAcoes[valor]);
	}
	
	public static Acoes[] getListaAcoes() {
		return(listaAcoes);
	}
	
	@Override
	public String toString() {
		switch(this) {
			case CIMA :
				return("CIMA");
			case BAIXO :
				return("BAIXO");
			case ESQUERDA :
				return("ESQUERDA");
			case DIREITA :
				return("DIREITA");
		}
		return("???");
	}
}
