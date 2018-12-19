package tests;

import java.io.File;

import RN.RNPlayer;

public class RNPlayerTester {
	private final static String nl = System.getProperty("line.separator");
	private static final String path = "dados";
	
	public static void main(String args[]) {
		final File mapaFile = new File(path + File.separator + "mapa10x10.txt");
		RNPlayer rnp = new RNPlayer();
		rnp.play(mapaFile,false);
		
		int pontuacao = rnp.getPontuacao();
		System.out.println("Pontuacao:"+pontuacao);
	}
}
