package RN;

import java.util.List;
import java.util.Random;

public class Neuronio {
	List<Arestas> conexoes = null; // nivel anterior
	Double bias;
	Double valor;
	
	protected Neuronio(List<Arestas> conexoes,Double bias) {
		this.conexoes = conexoes;
		this.bias = bias;
	}
	
	protected Neuronio() {
		Random gen = new Random();
		double var = Arestas.maxValor - Arestas.minValor;
		this.bias = gen.nextDouble()*var + Arestas.minValor;
	}
	
	protected void setConexoes(List<Arestas> conexoes) {
		if (this.conexoes!=null) {
			throw new UnsupportedOperationException("conexoes ja setada");
		}
		this.conexoes = conexoes;
	}
	
	//nota esse calculo assume que um neuronio so tem 2 estados 0-desativado e 1-ativado
	protected double calcularValor() {
		if (this.conexoes==null) {
			throw new UnsupportedOperationException("conexoes nao setadas");
		}
		double valorTmp = 0.00;
		for (Arestas ar : conexoes) {
			valorTmp += ar.getArestaValor(); // ar.peso * valor de ativacao?
		}
		valorTmp += bias;
		return(valorTmp);
	}
	
	protected double setValor(Double inputValor) {
		valor = inputValor;
		return(valor);
	}
	
	protected double getValor() {
		return(valor);
	}
	
	protected List<Arestas> getArestas() {
		return(conexoes);
	}

	@Override
	public String toString() {
		StringBuilder aux = new StringBuilder();
		aux.append(valor).append("=");
		boolean first = true;
		for (Arestas ar : conexoes) {
			if (!first) {
				aux.append("+");
			}else {
				first = false;
			}
			aux.append(ar.getNeuronio().getValor() + "*" + ar.getPeso());
		}
		aux.append("+").append(bias);
		return(aux.toString());
	}
}
