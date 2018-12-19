package RN;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RedeNeuralInterface {
	List<Neuronio> inputLayer;
	List<Neuronio> outputLayer;
	Blackbox bb;
	
	Double[][] inputPesos;
	Double[] inputValores;
	
	FuncaoDeAtivacao fda = null;
	
	public RedeNeuralInterface(int tamanhoInputLayer,int tamanhoOutputLayer,int numeroDeHiddenLayers,int neuroniosPorLayerInHiddenaLayer,FuncaoDeAtivacao fda) {
		outputLayer = new ArrayList<>();
		for (int i=0;i<tamanhoOutputLayer;i++) { //cria outputLayer
			Neuronio nr = new Neuronio();
			outputLayer.add(nr);
		}
		bb = new Blackbox(numeroDeHiddenLayers, neuroniosPorLayerInHiddenaLayer, outputLayer);//cria blackbox
		inputLayer = new ArrayList<>();//cria inputLayer
		for (int i=0;i<tamanhoInputLayer;i++) {
			Neuronio nr = new Neuronio();
			inputLayer.add(nr);
		}
		bb.conectarInputLayer(inputLayer);
		
		inputPesos = new Double[tamanhoInputLayer][tamanhoInputLayer];
		Random gen = new Random();
		for (int i=0;i<inputPesos.length;i++) {
			for (int j=0;j<inputPesos[0].length;j++) {
				inputPesos[i][j] = gen.nextDouble()*Arestas.variacaoTotal + Arestas.minValor; 
			}
		}
		inputValores = new Double[tamanhoInputLayer];
		
		this.fda = fda;
	}
	
	public List<Double> getOutput() {
		for (int i=0;i<inputLayer.size();i++) {
			Neuronio inputNr = inputLayer.get(i);
			double valor = inputNr.calcularValor(inputValores, inputPesos[i]);
			fda.ativa(inputNr,valor);
		}
	}
}
