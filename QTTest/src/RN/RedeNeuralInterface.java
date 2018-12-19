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
		//seta valores da primeira layer
		for (int i=0;i<inputLayer.size();i++) {
			Neuronio inputNr = inputLayer.get(i);
			double valor = inputNr.calcularValor(inputValores, inputPesos[i]);
			fda.ativa(inputNr,valor);
		}
		//agora os neuronios da inputlayer estado ou ativos ou inativo
		//com  base na inputlayer eh possivel ativar a blackbox(hidden layer)
		bb.ativarHiddenLayer(fda); // ativa hidden layer
		List<Double> lastLayerOutput = new ArrayList<>();
		for (int i=0;i<outputLayer.size();i++) {
			Neuronio outputNr = outputLayer.get(i);
			Double valor = outputNr.calcularValor();
			lastLayerOutput.add(valor);
		}
		return lastLayerOutput;
	}
	
	public void setInput(Double[] valoresInput) {
		if (valoresInput.length != inputLayer.size()) {
			throw new IllegalArgumentException("valores.size != inputLayer.size() => " + valoresInput.length +" != " + inputLayer.size());
		}
		inputValores = valoresInput;
	}
}
