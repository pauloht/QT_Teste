package tests;

import java.io.File;

import undefinied.ArvoreDeEstados;
import undefinied.QT;

public class QTTeste {
	public static void t1() {
		long start = System.nanoTime();
		String path = "C:\\Users\\TotemSistemas\\Desktop\\paulopasta\\iaTestes";
		File f = new File(path + File.separator + "arvore.txt");
		for (int i=0;i<1;i++) {
			QT qt = new QT();
			ArvoreDeEstados av = qt.init(0.5,null);
			av.saveToFile(f);
		}
		
		double elapsed = (0.0 + System.nanoTime() - start)/(Math.pow(10, 9));
		System.out.println(String.format("%.4f", elapsed));
	}
	
	public static void main(String[] args) {
		t1();
	}
}
