package RN;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RedeNeuralInterface {
	private static final String nl = System.getProperty("line.separator");
	List<Neuronio> inputLayer;
	List<Neuronio> outputLayer;
	Blackbox bb;
	
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

		inputValores = null;
		this.fda = fda;
	}
	
	public List<Double> getOutput() {
		//seta valores da primeira layer
		for (int i=0;i<inputLayer.size();i++) {
			Neuronio inputNr = inputLayer.get(i);
			double valor = inputNr.setValor(inputValores[i]);
		}
		//agora os neuronios da inputlayer estado ou ativos ou inativo
		//com  base na inputlayer eh possivel ativar a blackbox(hidden layer)
		bb.ativarHiddenLayer(fda); // ativa hidden layer
		List<Double> lastLayerOutput = new ArrayList<>();
		for (int i=0;i<outputLayer.size();i++) {
			Neuronio outputNr = outputLayer.get(i);
			fda.ativa(outputNr);
			Double valor = outputNr.getValor();
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
	
	@Override
	public String toString() {
		StringBuilder aux = new StringBuilder();
		for (int i=0;i<inputLayer.size();i++) {
			aux.append(inputLayer.get(i).getValor() + " ");
		}
		aux.append(nl);
		aux.append(bb.toString());
		for (int i=0;i<outputLayer.size();i++) {
			aux.append(outputLayer.get(i) + " ");
		}
		return(aux.toString());
	}
	
	public void setOutputLayerArestas(List<List<Double>> pesos) {
		int tamanhoOutputlayer = outputLayer.size();
		int numeroDeArestas = outputLayer.get(0).getArestas().size();
		if (pesos.size() != tamanhoOutputlayer) {
			throw new IllegalArgumentException("tamanhoOutputlayer != outputLayer.size()");
		}
		if ((pesos.get(0).size()-1) != numeroDeArestas) {
			throw new IllegalArgumentException("numeroDeArestas != (pesos.get(0).size()-1)");
		}
		List<Neuronio> layerNeuronio = outputLayer;
		for (int i=0;i<tamanhoOutputlayer;i++) {
			Neuronio nr = layerNeuronio.get(i);
			List<Arestas> arLista = nr.getArestas();
			List<Double> arestaP = pesos.get(i);
			for (int j=0;j<arLista.size();j++) {
				Arestas ar = arLista.get(j);
				ar.peso = arestaP.get(j);
			}
			nr.bias = arestaP.get(arestaP.size()-1);
		}
	}
	
	public void iniciarBackpropagation(List<Double> erro) {
		int outputlayerTam = outputLayer.size();
		if (erro.size()!=outputlayerTam) {
			throw new IllegalArgumentException();
		}
		
	}
	
	public static void main(String args[]) {
		int tamanhoInputLayer = 2;
		int tamanhoOutputLayer = 2;
		int tamanhoHiddenLayer = 1;
		int numeroNeuronioPorLayer = 2;
		FuncaoDeAtivacao fda = new Ativacao2();
		RedeNeuralInterface rni = new RedeNeuralInterface(tamanhoInputLayer, tamanhoOutputLayer, tamanhoHiddenLayer, numeroNeuronioPorLayer, fda);
		
		List<List<List<Double>>> pesosArestas = new ArrayList<>();
		List<List<Double>> layer1 = new ArrayList<>();
		pesosArestas.add(layer1);
		
		List<Double> pesos1 = new ArrayList<>();
		List<Double> pesos2 = new ArrayList<>();
		layer1.add(pesos1);
		layer1.add(pesos2);
		
		pesos1.add(0.15);
		pesos1.add(0.20);
		pesos1.add(0.35);
		pesos2.add(0.25);
		pesos2.add(0.30);
		pesos2.add(0.35);
		
		List<List<Double>> outputPesos = new ArrayList<>();
		List<Double> pesoOutput1 = new ArrayList<>();
		pesoOutput1.add(0.40);
		pesoOutput1.add(0.45);
		pesoOutput1.add(0.60);
		List<Double> pesoOutput2 = new ArrayList<>();
		pesoOutput2.add(0.50);
		pesoOutput2.add(0.55);
		pesoOutput2.add(0.60);
		outputPesos.add(pesoOutput1);
		outputPesos.add(pesoOutput2);
		rni.setOutputLayerArestas(outputPesos);
		
		Double[] valoresInput = { 0.05 , 0.1 };
		rni.setInput(valoresInput);
		rni.bb.setArestasPesos(pesosArestas);
		
		List<Double> ret = rni.getOutput();
		for (Double v : ret) {
			System.out.println(" " + v);
		}
		
		System.out.println("toString:"+nl+rni.toString());
	}
}
