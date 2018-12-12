package undefinied;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.Stack;

public class QT {
	private String nl = System.getProperty("line.separator");
	private int numeroMaximoGen = 100;
	
	//constantes
	private static final boolean showProcess = false;
	private static final int recompensaValor = 30;
	private static final int pontuacaoInicial = 10;
	private static final int perdaPorPasso = 1;
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
					break;
				case RECOMPENSA:
					mapa[linha][coluna] = ValorTile.NADAMOVIDO;
					pontuacao += recompensaValor; 
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
	
	private int[] getVetorDeAcesso() {
		int[] vda = new int[profundidade];
		
		vda[0] = (mapa[linha-1][coluna]).getValor(); //cima
		vda[1] = (mapa[linha+1][coluna]).getValor(); //baixo
		vda[2] = (mapa[linha][coluna+1]).getValor(); //direita
		vda[3] = (mapa[linha][coluna-1]).getValor(); //esquerda
		
		return(vda);
	}
	
	private Acoes decidirNextMove() {
		boolean agirAleatoriamente = false;
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
				throw new UnsupportedOperationException("Esse caso raro nao foi tratado, tratar!");
			}
			movimento = Acoes.getAcaoFromValor(opcaoSelecionada);
		}
		decisoesValores.push(opcoes);
		decisoesIndices.push(opcaoSelecionada);
		return(movimento);
	}
	
	private boolean movimentoIsValido(int linha,int coluna) {
		if (mapa[linha][coluna]==ValorTile.PAREDE) {
			return(false);
		}
		return(true);
	}
	
	private boolean jogoTerminou() {
		if (mapa[linha][coluna]==ValorTile.MORTE) {
			return(true);
		}
		if (geracaoCorrente > numeroMaximoGen) {
			return(true);
		}
		return(false);
	}
	
	/**
	 * 
	 * @param taxaAleatoria valor entre 0.0 e 1.0
	 */
	public ArvoreDeEstados init(double taxaAleatoria, ArvoreDeEstados avInicial) {
		StringBuilder relatorio = new StringBuilder();
		profundidade = Acoes.getListaAcoes().length; // numero de opcoes, nao prescissar ser necessariamente igual
		ramoPorNo = ValorTile.getListaValorTile().length; // numero de variacoes
		tamanhoDados = Acoes.getListaAcoes().length; // determina tamanho folhas
		if (avInicial==null) {
			av = new ArvoreDeEstados(profundidade,ramoPorNo,tamanhoDados);
		}else {
			av = avInicial;
		}
		File mapaFile = new File("C:\\Users\\TotemSistemas\\Desktop\\paulopasta\\iaTestes\\mapa.txt");
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
			Acoes acao = decidirNextMove(); //decide acao
			if (agir(acao)) { // valida acao
				if (showProcess) {
					relatorio.append("Gen ").append(geracaoCorrente).append(" Movimento:").append(acao).append(nl);
					relatorio.append(mapa.toString(linha, coluna));
				}
				geracaoCorrente++;
				pontuacao = pontuacao - perdaPorPasso;
			}else {
				//remove historico
				decisoesValores.pop();
				decisoesIndices.pop();
			}
		}
		if (showProcess) {
			String path = "C:\\Users\\TotemSistemas\\Desktop\\paulopasta\\iaTestes";
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
		System.out.println("Pontuacao final : " + pontuacao);
		return(av);
	}
}
