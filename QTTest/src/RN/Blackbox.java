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
		for (int i=hiddenLayer.size()-1;i>0;i--) { // iteracoes por cada nivel
			List<Neuronio> layerSelecionada = hiddenLayer.get(i); //layer de neuronios que para cada neuronio associara conexoes
			List<Neuronio> alvo;
			if (i==hiddenLayer.size()-1) {
				alvo = outputLayer; // se for ultima lista de neuronios associa a outputlayer
			}else {
				alvo = hiddenLayer.get(i+1); // se nao for ultima lista de neuronio associa essa lista de hidden layer a proxima lista
			}
			for (int j=0;j<layerSelecionada.size();j++){ // iteracao por cada neuronio
				Neuronio neuronioSetado = layerSelecionada.get(j); //neuronio selecionado
				List<Arestas> conexoes = new ArrayList<>(); // conexoes que serao adicionadas ao neuronio
				for (int k=0;k<alvo.size();k++) { // lista de neuronios para criar lista de arestas
					Neuronio nr = alvo.get(k); // selecionado neuronio
					Arestas ar = new Arestas(nr); // cria aresta associada a neuronio acima
					conexoes.add(ar); // adiciona aresta a lista de arestas
				}
				neuronioSetado.setConexoes(conexoes); // seta lista de conexoes para neuronio selecionado
			}
		}
	}
	
	protected void conectarInputLayer(List<Neuronio> inputLayer) {
		for (Neuronio nr : inputLayer) {
			List<Arestas> conexoes = new ArrayList<>();
			for (Neuronio hiddenLayerNr : hiddenLayer.get(0)) {
				Arestas ar = new Arestas(hiddenLayerNr);
				conexoes.add(ar);
			}
			nr.setConexoes(conexoes);
		}
	}
}
