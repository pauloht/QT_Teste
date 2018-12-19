package RN;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Random;

import undefinied.Acoes;
import undefinied.MapaBean;
import undefinied.ValorTile;

public class RNPlayer {
	//constantes
	private static final String nl = System.getProperty("line.separator");
	private static final int recompensaValor = 30;
	private static final int pontuacaoInicial = 10;
	private static final int perdaPorBacktrack = 1;
	private static final int pontuacaoVitoria = 50;
	private static final int pontuacaoMorte = -50;
	
	private ValorTile[][] mapa = null;
	int linha = -1;
	int coluna = -1;
	
	int pontuacao = pontuacaoInicial;
	
	private int geracaoCorrente = 0;
	private int numeroMaximoGen = 100;

	private RedeNeuralInterface rni = null;
	

	
	private boolean jogoTerminou() {
		if (mapa[linha][coluna]==ValorTile.MORTE || mapa[linha][coluna]==ValorTile.VITORIA) {
			return(true);
		}
		if (geracaoCorrente > numeroMaximoGen) {
			return(true);
		}
		return(false);
	}
	
	private boolean agir(Acoes acao) {
		int novaLinha = linha;
		int novaColuna = coluna;
		switch (acao) {
			case BAIXO :
				novaLinha++; 
				break;
			case CIMA :
				novaLinha--;
				break;
			case DIREITA :
				novaColuna++;
				break;
			case ESQUERDA :
				novaColuna--;
				break;
			default:
				throw new UnsupportedOperationException("Caso nao implementado");
		}
		if (movimentoIsValido(novaLinha, novaColuna)) {
			linha = novaLinha;
			coluna = novaColuna;
			ValorTile novoTile = mapa[linha][coluna];
			switch (novoTile) {
				case NADA:
					mapa[linha][coluna] = ValorTile.NADAMOVIDO;
					pontuacao = pontuacao + 1;
					break;
				case NADAMOVIDO:
					pontuacao = pontuacao - perdaPorBacktrack;
					break;
				case RECOMPENSA:
					pontuacao += recompensaValor; 
					mapa[linha][coluna] = ValorTile.NADAMOVIDO;
					break;
				case MORTE:
					pontuacao += pontuacaoMorte;
					break;
				case VITORIA:
					pontuacao += pontuacaoVitoria;
					break;
				default:
					//to nothing
			}
			return(true);
		}
		return(false);
	}
	
	private Acoes decidirNextMove() {
		List<Double> lista = rni.getOutput();
		int indiceEscolha = 0;
		double maiorValor = lista.get(0);
		for (int i=1;i<lista.size();i++) {
			if (lista.get(i) > maiorValor) {
				indiceEscolha = i;
				maiorValor = lista.get(i);
			}
		}
		Acoes acaoEscolhida = Acoes.getAcaoFromValor(indiceEscolha);
		return(acaoEscolhida);
	}
	
	private boolean movimentoIsValido(int linha,int coluna) {
		if (mapa[linha][coluna]==ValorTile.PAREDE) {
			return(false);
		}
		return(true);
	}
	
	public void play(File mapaFile,boolean showProcess) {
		//inicializacao da RN
		int tamanhoInput = 4*ValorTile.getListaValorTile().length;
		int tamanhoOutput = Acoes.getListaAcoes().length;
		int numeroDeHiddenLayers = 3;
		int neuroniosEmHiddenLayer = 4;
		
		RedeNeuralInterface rninterface = new RedeNeuralInterface(tamanhoInput, tamanhoOutput, numeroDeHiddenLayers, neuroniosEmHiddenLayer,new Ativacao1());
		rni = rninterface;
		Double[] test = new Double[24];
		Random gen = new Random();
		for (int i=0;i<test.length;i++) {
			test[i] = new Double(gen.nextDouble()*3-1);
		}
		rninterface.setInput(test);

		//FIM inicializacao RN
		
		StringBuilder relatorio = new StringBuilder();
		
		//config mapa
		MapaBean mapa = MapaBean.recoverFromFile(mapaFile);
		this.mapa = mapa.getMapa();
		
		int[] posInicial = mapa.getPosicaoInicial();
		linha = posInicial[0];
		coluna = posInicial[1];
		//fim config mapa
		if (showProcess) {
			relatorio.append("Inicio:").append(nl);
			relatorio.append(mapa.toString(linha, coluna));
		}
		while (!jogoTerminou()) {
			Acoes acao = decidirNextMove(); //decide acao
			geracaoCorrente++;
			if (agir(acao)) { // valida acao
				if (showProcess) {
					relatorio.append("Gen ").append(geracaoCorrente).append(" Movimento:").append(acao).append(nl);
					relatorio.append(mapa.toString(linha, coluna));
				}
			}else {
				//remove historico
				//decisoesValores.pop();
				//decisoesIndices.pop();
			}
		}
		if (showProcess) {
			String path = "dados";
			File file = new File(path + File.separator + "rnOutput.txt");
			try(
				RandomAccessFile raf = new RandomAccessFile(file, "rw");
			){
				raf.setLength(0);
				raf.writeBytes(relatorio.toString());
				raf.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getPontuacao() {
		return(pontuacao);
	}
}
