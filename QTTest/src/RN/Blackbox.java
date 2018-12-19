package RN;

import java.util.ArrayList;
import java.util.List;

public class Blackbox {
	List<List<Neuronio>> hiddenLayer;
	
	protected Blackbox(int numeroDeLayers,int neuroniosPorLayer,List<Neuronio> outputLayer) {
		hiddenLayer = new ArrayList<>();
		for (int i=0;i<numeroDeLayers;i++) { // cria N layers
			List<Neuronio> novaLista = new ArrayList<>(); // cria layer
			hiddenLayer.add(novaLista); // associa layer a lista de layers
			for (int j=0;j<neuroniosPorLayer;j++) { // seta cada layer seta X neuronios
				Neuronio nr = new Neuronio(); // cria neuronio
				novaLista.add(nr); // inclue neuronios em lista
			}
		}
		for (int i=hiddenLayer.size()-1;i>=0;i--) { // iteracoes por cada nivel associa nivel i com nivel i-1
			List<Neuronio> proximaLayer; //layer de neuronios que para cada neuronio associara conexoes
			List<Neuronio> layerAtual = hiddenLayer.get(i);
			if (i==hiddenLayer.size()-1) {
				proximaLayer = outputLayer; // se for ultima lista de neuronios associa a outputlayer
				System.out.println("Setando conexoes em layer :" + (i)+"->outputLayer");
			}else {
				proximaLayer = hiddenLayer.get(i+1); // se nao for ultima lista de neuronio associa essa lista de hidden layer a proxima lista
				System.out.println("Setando conexoes em layer :" + (i)+"->"+(i+1));
			}
			for (int j=0;j<proximaLayer.size();j++){ // iteracao por cada neuronio
				Neuronio neuronioSetado = proximaLayer.get(j); //neuronio selecionado
				List<Arestas> conexoes = new ArrayList<>(); // conexoes que serao adicionadas ao neuronio
				for (int k=0;k<layerAtual.size();k++) { // lista de neuronios para criar lista de arestas
					Neuronio nr = layerAtual.get(k); // selecionado neuronio
					Arestas ar = new Arestas(nr); // cria aresta associada a neuronio acima
					conexoes.add(ar); // adiciona aresta a lista de arestas
				}
				neuronioSetado.setConexoes(conexoes); // seta lista de conexoes para neuronio selecionado
			}
		}
	}
	
	protected void ativarHiddenLayer(FuncaoDeAtivacao fda) {
		//ativa layers em ordem 1,2...ultima
		int numeroLayer = 0;
		for (List<Neuronio> layer : hiddenLayer) {
			//System.out.println("Ativando layer " + numeroLayer);
			numeroLayer++;
			for (Neuronio nr : layer) {
				fda.ativa(nr);
			}
		}
	}

	public void conectarInputLayer(List<Neuronio> inputLayer) {
		// TODO Auto-generated method stub
		for (Neuronio hiddenLayerNr : hiddenLayer.get(0)) {
			List<Arestas> conexoes = new ArrayList<>();
			for (int k=0;k<inputLayer.size();k++) {
				Neuronio inputLayerNr = inputLayer.get(k);
				Arestas ar = new Arestas(inputLayerNr);
				conexoes.add(ar);
			}
			hiddenLayerNr.setConexoes(conexoes);
		}
	}
}
