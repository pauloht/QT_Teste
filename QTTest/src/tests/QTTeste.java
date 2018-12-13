package tests;

import java.io.File;
import java.io.RandomAccessFile;

import QTEstatico.ArvoreDeEstados;
import QTEstatico.QT;
import undefinied.Acoes;
import undefinied.ValorTile;

public class QTTeste {
	private final static String nl = System.getProperty("line.separator");
	private static final String path = "dados";
	
	public static void treinar(File arvoreInput,File arvoreOutput,File mapaFile) {
		long start = System.nanoTime();
		int numeroDeTestes = 100000;
		int testesPorAmostra = numeroDeTestes/100;
		StringBuilder aux = new StringBuilder();
		ArvoreDeEstados arvoreUsada = null;
		if (arvoreInput != null) {
			arvoreUsada = ArvoreDeEstados.loadFromFile(arvoreInput);
		}else {
			arvoreUsada = new ArvoreDeEstados( Acoes.getListaAcoes().length, ValorTile.getListaValorTile().length, Acoes.getListaAcoes().length);
		}
		int contadorTeste = 0;
		aux.append("teste pontuacao taxaAleatoria").append(nl);
		for (int i=0;i<numeroDeTestes;i++) {
			QT qt = new QT();
			//double taxaAleatoria = (1.0 - (i+0.00)/(numeroDeTestes+0.0));
			double taxaAleatoria = 0.0;
			//System.out.println("taxaAleatoria:"+taxaAleatoria);
			qt.init(arvoreUsada,mapaFile,taxaAleatoria,false);
			arvoreUsada = qt.getArvore();
			if (i%testesPorAmostra==0) {
				//aux.append(contadorTeste).append(' ').append(qt.getPontuacao()).append(' ').append(Double.toString(taxaAleatoria*100).replaceFirst("[.]", ",")).append(nl);
				aux.append(contadorTeste).append(' ').append(qt.getPontuacao()).append(nl);
				contadorTeste++;
			}
			if (i==(numeroDeTestes-1)) {
				arvoreUsada.saveToFile(arvoreOutput);
			}
		}
		try(
			RandomAccessFile raf = new RandomAccessFile(new File(path + File.separator + "testesOutput" + mapaFile.getName()), "rw");
		){
			raf.setLength(0);
			raf.writeBytes(aux.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		double elapsed = (0.0 + System.nanoTime() - start)/(Math.pow(10, 9));
		System.out.println(String.format("%.4f", elapsed));
	}
	
	public static void t2(File arvoreFile , File mapaFile) {
		long start = System.nanoTime();
		String path = "dados";
		int numeroDeTestes = 1;
		ArvoreDeEstados arvoreUsada = ArvoreDeEstados.loadFromFile(arvoreFile);
		for (int i=0;i<numeroDeTestes;i++) {
			QT qt = new QT();
			qt.init(arvoreUsada,mapaFile,0.0,true);
			arvoreUsada = qt.getArvore();
			System.out.println("Pontuacao:"+qt.getPontuacao());
		}
		
		double elapsed = (0.0 + System.nanoTime() - start)/(Math.pow(10, 9));
		System.out.println(String.format("%.4f", elapsed));
	}
	
	public static void main(String[] args) {
		final File mapaFile = new File(path + File.separator + "mapa10x10.txt");
		final File mapaFile2 = new File(path + File.separator + "mapa10x10_v2.txt");
		final File mapaFile3 = new File(path + File.separator + "mapa10x10_v3.txt");
		final File arvore1_ = new File(path + File.separator + "arvore1.txt");
		final File arvore2_ = new File(path + File.separator + "arvore2.txt");
		//File arvoreInput = new File(path + File.separator + "arvoreTeste.txt");
		treinar(arvore1_,arvore1_,mapaFile3);
		t2(arvore1_,mapaFile3);
	}
}
