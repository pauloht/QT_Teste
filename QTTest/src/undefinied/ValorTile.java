package undefinied;

public enum ValorTile {
	NADA(0),
	NADAMOVIDO(1),
	RECOMPENSA(2),
	MORTE(3),
	PAREDE(4),
	VITORIA(5);

	private int valor;
	private static ValorTile[] lista;
	
	static {
		lista = new ValorTile[6];
		lista[0] = NADA;
		lista[1] = NADAMOVIDO;
		lista[2] = RECOMPENSA;
		lista[3] = MORTE;
		lista[4] = PAREDE;
		lista[5] = VITORIA;
	}
	
	private ValorTile(int v) {
		this.valor = v;
	}
	
	public int getValor() {
		return(valor);
	}
	
	public static ValorTile getTileFromValor(int valor) {
		return(lista[valor]);
	}
	
	public static ValorTile[][] transformToValorTileMatrix(int[][] entrada){
		int linha = entrada.length;
		int coluna = entrada[0].length;
		ValorTile[][] ret = new ValorTile[linha][coluna];
		for (int i=0;i<linha;i++) {
			for (int j=0;j<coluna;j++) {
				ret[i][j] = getTileFromValor(entrada[i][j]);
			}
		}
		return(ret);
	}
	
	public static ValorTile[] getListaValorTile() {
		return(lista);
	}
}
