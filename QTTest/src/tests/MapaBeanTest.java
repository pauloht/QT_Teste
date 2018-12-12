package tests;

import java.io.File;

import undefinied.MapaBean;

public class MapaBeanTest {
	
	public static void test1() {
		int linhas = 6;
		int colunas = 5;
		int matriz[][] = { 	{ 4 , 4 , 4 , 4 , 4 },
							{ 4 , 2 , 0 , 3 , 4 },
							{ 4 , 0 , 3 , 0 , 4 },
							{ 4 , 0 , 0 , 2 , 4 },
							{ 4 , 0 , 1 , 2 , 4 },
							{ 4 , 4 , 4 , 4 , 4 }
		};
		MapaBean mapa = new MapaBean(linhas, colunas, matriz);
		System.out.println(mapa.toString());
		
		String path = "C:\\Users\\TotemSistemas\\Desktop\\paulopasta\\iaTestes";
		File file = new File(path + File.separator + "mapa.txt");
		mapa.saveToFile(file);
		
		MapaBean mapa2 = MapaBean.recoverFromFile(file);
		
		String sMapa1 = mapa.toString();
		String sMapa2 = mapa2.toString();
		System.out.println(sMapa2);
		if (sMapa1.equals(sMapa2)) {
			System.out.println("MATCH");
		}else {
			System.out.println("DONTMATCH");
		}
		
		int[] posInicial = mapa.getPosicaoInicial();
		int linhaInicial = posInicial[0];
		int colunaInicial = posInicial[1];
		System.out.println("Posicao Inicial:"+linhaInicial+","+colunaInicial);
		System.out.println(mapa.toString(linhaInicial,colunaInicial));
	}
	
	public static void main(String[] args) {
		test1();
	}
}
