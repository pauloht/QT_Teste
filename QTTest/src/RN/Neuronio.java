package RN;

import java.util.List;
import java.util.Random;

public class Neuronio {
	List<Arestas> conexoes = null;
	Double bias;
	
	boolean ativo = false;
	
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
			if (ar.neuronio.ativo) {
				valorTmp += ar.peso; // ar.peso * valor de ativacao?
			}
		}
		valorTmp += bias;
		return(valorTmp);
	}
	
	protected double calcularValor(Double[] inputValor,Double[] pesos) {
		if (this.conexoes!=null) {
			throw new UnsupportedOperationException("conexoes ja setadas");
		}
		if (inputValor.length != pesos.length) {
			throw new IllegalArgumentException("Tamanhos diferentes!!");
		}
		double valorTmp = 0.00;
		for (int i=0;i<inputValor.length;i++) {
			valorTmp += inputValor[i]*pesos[i];
		}
		return(valorTmp);
	}
}
