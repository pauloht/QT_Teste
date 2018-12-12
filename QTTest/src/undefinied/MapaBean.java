package undefinied;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class MapaBean {
	private static final String nl = System.getProperty("line.separator");
	int linhas,colunas;
	
	ValorTile[][] mapa;
	
	public MapaBean(int linhas,int colunas,int[][] mapa) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.mapa = ValorTile.transformToValorTileMatrix(mapa);
	}
	
	public int[] getPosicaoInicial() {
		for (int i=0;i<linhas;i++) {
			for (int j=0;j<colunas;j++) {
				if (mapa[i][j]==ValorTile.NADAMOVIDO) {
					int[] ret = new int[2];
					ret[0] = i;
					ret[1] = j;
					return(ret);
				}
			}
		}
		return(null);
	}
	
	public void saveToFile(File f) {
		try(
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
		){
			raf.setLength(0);
			StringBuilder aux = new StringBuilder();
			aux.append(linhas).append(' ').append(colunas).append(nl);
			for (int i=0;i<linhas;i++) {
				for (int j=0;j<colunas;j++) {
					aux.append(mapa[i][j].getValor());
					if (j != (colunas-1)) {
						aux.append(' ');
					}
				}
				aux.append(nl);
			}
			raf.writeBytes(aux.toString());
			raf.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static MapaBean recoverFromFile(File f) {
		try (
			BufferedReader br = new BufferedReader(new FileReader(f));	
		){
			String lineRead;
			String[] tokens;
			lineRead = br.readLine();
			tokens = lineRead.split(" ");
			if (tokens.length!=2) {
				throw new IllegalArgumentException("Arquivo formato incorretamente");
			}
			int linhas = Integer.parseInt(tokens[0]);
			int colunas = Integer.parseInt(tokens[1]);
			ArrayList<String> linesRead = new ArrayList<>();
			while (true) {
				lineRead = br.readLine();
				if (lineRead==null) {
					break;
				}
				linesRead.add(lineRead);
			}
			if (linesRead.size()!=linhas) {
				throw new IllegalArgumentException("Arquivo formato incorretamente, numero de linhas errado");
			}
			int[][] mapa = new int[linhas][colunas];
			for (int i=0;i<linesRead.size();i++) {
				lineRead = linesRead.get(i);
				tokens = lineRead.split(" ");
				if (tokens.length!=colunas) {
					throw new IllegalArgumentException("Arquivo formato incorretamente, numero de colunas errado");
				}
				for (int j=0;j<colunas;j++) {
					mapa[i][j] = Integer.parseInt(tokens[j]);
				}
			}
			MapaBean mapaFromFile = new MapaBean(linhas, colunas, mapa);
			return(mapaFromFile);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return(null);
	}
	
	@Override
	public String toString() {
		StringBuilder aux = new StringBuilder();
		for (int i=0;i<linhas;i++) {
			for (int j=0;j<colunas;j++) {
				aux.append(mapa[i][j].getValor()).append(' ');
			}
			aux.append(nl);
		}
		return(aux.toString());
	}
	
	public String toString(int linha,int coluna) {
		StringBuilder aux = new StringBuilder();
		for (int i=0;i<linhas;i++) {
			for (int j=0;j<colunas;j++) {
				if (linha==i && coluna==j) {
					aux.append('X').append(' ');
				}else {
					aux.append(mapa[i][j].getValor()).append(' ');
				}
			}
			aux.append(nl);
		}
		return(aux.toString());
	}
	
	public ValorTile[][] getMapa(){
		return(mapa);
	}
}
