package tests;

import java.io.File;

import undefinied.ArvoreDeEstados;
import undefinied.QT;

public class QTTeste {
	public static void treinar() {
		long start = System.nanoTime();
		String path = "dados";
		File f = new File(path + File.separator + "arvoreFinal.txt");
		File mapaFile = new File(path + File.separator + "mapa.txt");
		int numeroDeTestes = 10000;
		ArvoreDeEstados arvoreUsada = null;
		for (int i=0;i<numeroDeTestes;i++) {
			QT qt = new QT();
			arvoreUsada = qt.init(0.5,arvoreUsada,mapaFile);
			if (i==(numeroDeTestes-1)) {
				arvoreUsada.saveToFile(f);
			}
		}
		
		double elapsed = (0.0 + System.nanoTime() - start)/(Math.pow(10, 9));
		System.out.println(String.format("%.4f", elapsed));
	}
	
	public static void t2() {
		long start = System.nanoTime();
		String path = "dados";
		File f = new File(path + File.separator + "arvore16.txt");
		int numeroDeTestes = 1;
		ArvoreDeEstados arvoreUsada = ArvoreDeEstados.loadFromFile(f);
		File mapaFile = new File(path + File.separator + "mapa.txt");
		for (int i=0;i<numeroDeTestes;i++) {
			QT qt = new QT();
			arvoreUsada = qt.init(0.5,arvoreUsada,mapaFile);
		}
		
		double elapsed = (0.0 + System.nanoTime() - start)/(Math.pow(10, 9));
		System.out.println(String.format("%.4f", elapsed));
	}
	
	public static void main(String[] args) {
		t2();
	}
}
