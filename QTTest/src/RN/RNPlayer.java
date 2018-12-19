package RN;

import java.util.List;

import undefinied.Acoes;
import undefinied.ValorTile;

public class RNPlayer {
	public void play() {
		int tamanhoInput = 4*ValorTile.getListaValorTile().length;
		int tamanhoOutput = Acoes.getListaAcoes().length;
		int numeroDeHiddenLayers = 3;
		int neuroniosEmHiddenLayer = 4;
		
		RedeNeuralInterface rninterface = new RedeNeuralInterface(tamanhoInput, tamanhoOutput, numeroDeHiddenLayers, neuroniosEmHiddenLayer,new Ativacao1());
		
		List<Double> output = rninterface.getOutput();
		for (int i=0;i<tamanhoOutput;i++) {
			System.out.println("output "+i+":"+output.get(i));
		}
	}
	
	public static void main(String args[]) {
		RNPlayer rnp = new RNPlayer();
		rnp.play();
	}
}
