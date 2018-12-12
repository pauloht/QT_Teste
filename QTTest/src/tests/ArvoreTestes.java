package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

import undefinied.ArvoreDeEstados;

public class ArvoreTestes {
	private static String getFileContents(File file) {
		try(
			BufferedReader br = new BufferedReader(new FileReader(file));
		){
			StringBuilder aux = new StringBuilder();
			String lineRead;
			while (true) {
				lineRead = br.readLine();
				if (lineRead==null) {
					break;
				}
				aux.append(lineRead);
			}
			br.close();
			return(aux.toString());
		}catch(IOException e) {
			e.printStackTrace();
		}
		return(null);
	}
	
	public static void t1() throws IOException {
		ArvoreDeEstados av = new ArvoreDeEstados(2, 4, 4);
		String path = "C:\\Users\\TotemSistemas\\Desktop\\paulopasta\\iaTestes";
		File f = new File(path + File.separator + "arvore.txt");
		av.saveToFile(f);
		
		File f2 = new File(path + File.separator + "arvore2.txt");
		ArvoreDeEstados av2 = ArvoreDeEstados.loadFromFile(f);
		av2.saveToFile(f2);
		
		String s1 = getFileContents(f);
		String s2 = getFileContents(f2);
		
		if (s1.equals(s2)) {
			System.out.println("TRUE");
		}else {
			System.out.println("FALSE");
		}
	}
	
	public static void main(String[] args) throws IOException {
		t1();
	}
}
