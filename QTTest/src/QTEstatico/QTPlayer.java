package QTEstatico;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.Stack;

import undefinied.Acoes;
import undefinied.MapaBean;
import undefinied.ValorTile;

public class QTPlayer {
	private String nl = System.getProperty("line.separator");
	private int numeroMaximoGen = 100;
	
	//constantes
	private static final int recompensaValor = 30;
	private static final int pontuacaoInicial = 10;
	private static final int perdaPorBacktrack = 1;
	private static final int pontuacaoVitoria = 50;
	private static final int pontuacaoMorte = -50;
	
	int profundidade = -1;
	int ramoPorNo;
	int tamanhoDados;
	
	Random gen = new Random();
	int linha;
	int coluna;
	private ArvoreDeEstados av;
	private ValorTile[][] mapa;
	int geracaoCorrente = 0;
	int pontuacao = pontuacaoInicial;
	
	Stack<double[]> decisoesValores = new Stack<>();
	Stack<Integer> decisoesIndices = new Stack<>();
	
	private double[] ultimaDecisao = null;
	private Integer ultimoIndice = null;
	
	private static final double minValor = 0.000001;
	private static final double maxValor = 0.999998;
	
	private void julgarDecisao(double[] decisao,int indiceDecisao,double pontuacao) {
		double balanco = pontuacao;
		if (pontuacao < 0.0) {
			if (decisao[indiceDecisao] < Math.abs(pontuacao)) { // tem uma divida maior do que pagamento possivel
				balanco = -decisao[indiceDecisao];
			}
		}
		double divisao = balanco/decisao.length;
		for (int i=0;i<decisao.length;i++) {
			if (decisao[i] < minValor) {
				decisao[i] = 0.0;
			}else {
				if (decisao[i] >= divisao) {
					decisao[i] = decisao[i] - divisao;
					decisao[indiceDecisao] = decisao[indiceDecisao] + divisao;
				}else {
					decisao[indiceDecisao] += decisao[i];
					decisao[i] = 0;
				}
			}
		}
		if (decisao[indiceDecisao] > maxValor) {
			decisao[indiceDecisao] = 1.0;
		}
	}
	
	/**
	 * 
	 * @param acao
	 * @return true se acao foi efetuada com sucesso false caso contrario
	 */
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
					julgarDecisao(ultimaDecisao, ultimoIndice, 0.001); //0.1%
					break;
				case NADAMOVIDO:
					pontuacao = pontuacao - perdaPorBacktrack;
					julgarDecisao(ultimaDecisao, ultimoIndice, -0.001); //-0.1%
					break;
				case RECOMPENSA:
					mapa[linha][coluna] = ValorTile.NADAMOVIDO;
					pontuacao += recompensaValor; 
					julgarDecisao(ultimaDecisao, ultimoIndice, 0.01); //+1%
					break;
				case MORTE:
					pontuacao += pontuacaoMorte;
					julgarDecisao(ultimaDecisao, ultimoIndice, -0.05); //-5%
					break;
				case VITORIA:
					pontuacao += pontuacaoVitoria;
					julgarDecisao(ultimaDecisao, ultimoIndice, 0.10); // +10%
					break;
				default:
					//to nothing
			}
			return(true);
		}else {
			julgarDecisao(ultimaDecisao, ultimoIndice, -1.0); // -100%(punir severamente açoes ilegais para nao serem cometidas no futuro)  
		}
		return(false);
	}
	
	private int[] getVetorDeAcesso() {
		int[] vda = new int[profundidade];
		
		vda[0] = (mapa[linha-1][coluna]).getValor(); //cima
		vda[1] = (mapa[linha+1][coluna]).getValor(); //baixo
		vda[2] = (mapa[linha][coluna+1]).getValor(); //direita
		vda[3] = (mapa[linha][coluna-1]).getValor(); //esquerda
		
		return(vda);
	}
	
	private int validarVetor(double[] vet) {
		double sum = 0.0;
		double maior = 0.0;
		int maiorIndice = 0;
		for (int i=0;i<vet.length;i++) {
			sum += vet[i];
			if (vet[i] > maior) {
				maior = vet[i];
				maiorIndice = i;
			}
		}
		if (sum < 1.0) {
			double dif = 1.0 - maior;
			System.out.println("DIF:"+dif);
			vet[maiorIndice] += dif;
		}
		return(maiorIndice);
	}
	
	private Acoes decidirNextMove(double taxaDeAleatoriedade) {
		boolean agirAleatoriamente = false;
		double deveAgirAleatoriamente = gen.nextDouble();
		if (taxaDeAleatoriedade > deveAgirAleatoriamente) {
			agirAleatoriamente = true;
		}
		Acoes movimento = null;
		int[] vda = getVetorDeAcesso();
		double[] opcoes = av.acesarNo(vda);
		Integer opcaoSelecionada = null;
		if (agirAleatoriamente) {
			opcaoSelecionada = gen.nextInt(Acoes.getListaAcoes().length);
			movimento = Acoes.getAcaoFromValor( opcaoSelecionada );//acao totalmente aleatoria
		}else {
			double sorteio = gen.nextDouble();
			double contador = 0.0;
			for (int i=0;i<opcoes.length;i++) {
				contador += opcoes[i];
				if (contador > sorteio) {
					opcaoSelecionada = i;
					break;
				}
			}
			if (opcaoSelecionada == null) {
				//throw new UnsupportedOperationException("Esse caso raro nao foi tratado, tratar!");
				opcaoSelecionada = validarVetor(opcoes);
			}
			movimento = Acoes.getAcaoFromValor(opcaoSelecionada);
		}
		//decisoesValores.push(opcoes);
		//decisoesIndices.push(opcaoSelecionada);
		ultimaDecisao = opcoes;
		ultimoIndice = opcaoSelecionada;
		return(movimento);
	}
	
	private boolean movimentoIsValido(int linha,int coluna) {
		if (mapa[linha][coluna]==ValorTile.PAREDE) {
			return(false);
		}
		return(true);
	}
	
	private boolean jogoTerminou() {
		if (mapa[linha][coluna]==ValorTile.MORTE || mapa[linha][coluna]==ValorTile.VITORIA) {
			return(true);
		}
		if (geracaoCorrente > numeroMaximoGen) {
			return(true);
		}
		return(false);
	}
	
	private void recalcularArvoreFinal() {
		double variacao = 0;
		if (pontuacao > 0.0) {
			if (pontuacao < 10.0) {
				if (pontuacao < 20.0) {
					if (pontuacao < 30.0) {
						variacao = 0.1; // pontuacao maior que 30
					}else {
						variacao = 0.05; // pontuacao entre 20-30
					}
				}else {
					variacao = 0.03; // pontuacao entre 10-20
				}
			}else {
				variacao = 0.01; // pontuacao entre 0-10
			}
		}else if (pontuacao < 0.0) {
			if (pontuacao > -20.0) {
				variacao = 0.03;
			}else {
				variacao = -0.01;
			}
		}else {
			return;
		}
		while (!decisoesValores.isEmpty()) {
			double[] decisao = decisoesValores.pop();
			int indice = decisoesIndices.pop();
			double balanco = variacao;
			double divisao = balanco/decisao.length;
			for (int i=0;i<decisao.length;i++) {
				if (decisao[i] >= divisao) {
					decisao[i] = decisao[i] - divisao;
					decisao[indice] = decisao[indice] + divisao;
				}
			}
		}
	}
	
	/**
	 * 
	 * @param taxaDeAleatoriedade valor entre 0.0 e 1.0
	 */
	public void init(ArvoreDeEstados avInicial,File mapaFile, double taxaDeAleatoriedade,boolean showProcess) {
		StringBuilder relatorio = new StringBuilder();
		profundidade = Acoes.getListaAcoes().length; // numero de opcoes, nao prescissar ser necessariamente igual
		ramoPorNo = ValorTile.getListaValorTile().length; // numero de variacoes
		tamanhoDados = Acoes.getListaAcoes().length; // determina tamanho folhas
		if (avInicial==null) {
			av = new ArvoreDeEstados(profundidade,ramoPorNo,tamanhoDados);
		}else {
			av = avInicial;
		}
		//File mapaFile = new File("C:\\Users\\TotemSistemas\\Desktop\\paulopasta\\iaTestes\\mapa.txt");
		MapaBean mapa = MapaBean.recoverFromFile(mapaFile);
		this.mapa = mapa.getMapa();
		
		int[] posInicial = mapa.getPosicaoInicial();
		linha = posInicial[0];
		coluna = posInicial[1];
		
		if (showProcess) {
			relatorio.append("Inicio:").append(nl);
			relatorio.append(mapa.toString(linha, coluna));
		}
		while (!jogoTerminou()) {
			Acoes acao = decidirNextMove(taxaDeAleatoriedade); //decide acao
			if (agir(acao)) { // valida acao
				if (showProcess) {
					relatorio.append("Gen ").append(geracaoCorrente).append(" Movimento:").append(acao).append(nl);
					relatorio.append(mapa.toString(linha, coluna));
				}
				geracaoCorrente++;
			}else {
				//remove historico
				//decisoesValores.pop();
				//decisoesIndices.pop();
			}
		}
		if (showProcess) {
			String path = "dados";
			File file = new File(path + File.separator + "qtOutput.txt");
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
		//recalcularArvore();
	}
	
	public int getPontuacao() {
		return(pontuacao);
	}
	
	public ArvoreDeEstados getArvore() {
		return(av);
	}
}
