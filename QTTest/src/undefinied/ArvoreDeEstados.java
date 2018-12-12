package undefinied;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.Stack;

public class ArvoreDeEstados {
	private static final String nl = System.getProperty("line.separator");
	int profundidade;
	int ramoPorNo;
	int tamanhoDados;
	No raiz;
	
	public void saveToFile(File f) {
		try(
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
		){
			raf.setLength(0);
			StringBuilder aux = new StringBuilder();
			aux.append(profundidade).append(' ').append(ramoPorNo).append(' ').append(tamanhoDados).append(nl);
			Stack<No> stack = new Stack<>();
			Stack<No> tmpStack = new Stack<>();
			stack.push(raiz);
			while (!stack.isEmpty()) {
				while (!stack.isEmpty()) {
					No no = stack.pop();
					if (no.getValor()==null) {
						No[] filhos = no.getFilhos();
						for (int i=0;i<filhos.length;i++) {
							tmpStack.push(filhos[i]);
						}
					}else {
						double[] valor = no.getValor();
						for (int i=0;i<valor.length;i++) {
							aux.append(valor[i]);
							if (i < (valor.length-1)) {
								aux.append(' ');
							}else {
								aux.append(nl);
							}
						}
					}
				}
				while (!tmpStack.isEmpty()) {
					stack.push(tmpStack.pop());
				}
			}
			raf.writeBytes(aux.toString());
			raf.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArvoreDeEstados loadFromFile(File f) {
		try (
			BufferedReader br = new BufferedReader(new FileReader(f));	
			){
			String linhaLida = br.readLine();
			String[] tokens = linhaLida.split(" ");
			if (tokens.length!=3) {
				throw new IllegalArgumentException("Espeva-se token de tamanho 3 para String :"+linhaLida);
			}
			int profundidade = Integer.parseInt(tokens[0]);
			int ramoPorNo = Integer.parseInt(tokens[1]);
			int tamanhoDados = Integer.parseInt(tokens[2]);
			int numeroDeNosFolhas = new Double(Math.pow(ramoPorNo, profundidade)).intValue();
			No[] vetorNos = new No[numeroDeNosFolhas];
			for (int i=0;i<numeroDeNosFolhas;i++) {
				linhaLida = br.readLine();
				tokens = linhaLida.split(" ");
				if (tokens.length!=tamanhoDados) {
					throw new IllegalArgumentException("Esperava-se tamanho de dados igual a " + tamanhoDados + " para String:"+linhaLida);
				}
				double[] valores = new double[tamanhoDados];
				for (int j=0;j<valores.length;j++) {
					valores[j] = Double.parseDouble(tokens[j]);
				}
				No folha = new No(valores);
				vetorNos[i] = folha;
			}
			No raiz;
			while (true) {
				vetorNos = subirNivel(ramoPorNo, vetorNos);
				if (vetorNos.length==1) {
					raiz = vetorNos[0];
					break;
				}
			}
			ArvoreDeEstados retorno = new ArvoreDeEstados(profundidade,ramoPorNo,tamanhoDados,raiz);
			return(retorno);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return(null);
	}
	
	private static No[] subirNivel(int levelDeAgrupamento, No[] entrada){
		if (entrada.length%levelDeAgrupamento!=0) {
			throw new IllegalArgumentException("Entrada de tamanho "+entrada.length+" nao eh divisivel por " + levelDeAgrupamento);
		}
		int novoTamanho = entrada.length/levelDeAgrupamento;
		No[] retorno = new No[novoTamanho];
		for (int i=0;i<novoTamanho;i++) {
			No[] filhos = new No[levelDeAgrupamento];
			for (int j=0;j<levelDeAgrupamento;j++) {
				filhos[j] = entrada[i*levelDeAgrupamento+j];
			}
			retorno[i] = new No(filhos);
		}
		return(retorno);
	}
	
	public double[] acesarNo(int[] vetorDeAcesso) {
		if (vetorDeAcesso.length != profundidade) {
			throw new IllegalArgumentException("Vetor de acesso tem que ser igual a profundidade");
		}
		No noLocal = raiz;
		for (int i=0;i<vetorDeAcesso.length;i++) {
			int id = vetorDeAcesso[i];
			if (id>=ramoPorNo) {
				throw new IllegalArgumentException("Vetor de acesso com indice invalido('Array out of bounds') id : " + id);
			}
			noLocal = noLocal.getFilhos()[id];
		}
		return(noLocal.getValor());
	}
	
	private ArvoreDeEstados(int profundidade,int ramoPorNo,int tamanhoDados,No raiz) {
		this.profundidade = profundidade;
		this.ramoPorNo = ramoPorNo;
		this.tamanhoDados = tamanhoDados;
		this.raiz = raiz;
	}
	
	public ArvoreDeEstados(int profundidade,int ramoPorNo,int tamanhoDados) {
		//profundidade = Acoes.getListaAcoes().length; // numero de opcoes, nao prescissar ser necessariamente igual
		//ramoPorNo = ValorTile.getListaValorTile().length; // numero de variacoes
		//tamanhoDados = Acoes.getListaAcoes().length; // determina tamanho folhas
		this.profundidade = profundidade;
		this.ramoPorNo = ramoPorNo;
		this.tamanhoDados = tamanhoDados;
	
		int numeroDeNosFolhas = new Double(Math.pow(ramoPorNo, profundidade)).intValue();
		No[] vetorNos = new No[numeroDeNosFolhas];
		for (int i=0;i<numeroDeNosFolhas;i++) {
			double[] valores = new double[tamanhoDados];
			for (int j=0;j<valores.length;j++) {
				valores[j] = 1.0/valores.length;
			}
			No folha = new No(valores);
			vetorNos[i] = folha;
		}
		while (true) {
			vetorNos = subirNivel(ramoPorNo, vetorNos);
			if (vetorNos.length==1) {
				raiz = vetorNos[0];
				break;
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder aux = new StringBuilder();
		Stack<No> stack = new Stack<>();
		Stack<No> tmpStack = new Stack<>();
		int profundidade = 0;
		stack.push(raiz);
		while (!stack.isEmpty()) {
			aux.append("profundidade ").append(profundidade).append(":");
			while (!stack.isEmpty()) {
				No no = stack.pop();
				aux.append(no.toString()).append(' ');
				if (no.getValor()==null) {
					No[] filhos = no.getFilhos();
					for (int i=0;i<filhos.length;i++) {
						tmpStack.push(filhos[i]);
					}
				}
			}
			aux.append(nl);
			while (!tmpStack.isEmpty()) {
				stack.push(tmpStack.pop());
			}
			//stack = tmpStack;
			profundidade++;
		}
		return(aux.toString());
	}
}
