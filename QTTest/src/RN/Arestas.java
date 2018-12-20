package RN;

import java.util.Random;

public class Arestas {
	protected Neuronio neuronio;
	protected Double peso;
	
	public static final double maxValor = 1.0;
	public static final double minValor = -1.0;
	public static final Double variacaoTotal;
	static {
		variacaoTotal = maxValor - minValor;
	}
	
	protected Arestas(Neuronio neuronio,Double peso) {
		this.neuronio = neuronio;
		this.peso = peso;
	}
	
	protected Arestas(Neuronio neuronio) {
		Random gen = new Random();
		this.neuronio = neuronio;
		this.peso = gen.nextDouble()*variacaoTotal + minValor;
	}
	
	public Neuronio getNeuronio() {
		return(neuronio);
	}
	
	public double getPeso() {
		return(peso);
	}
	
	public double getArestaValor() {
		return(neuronio.getValor()*peso);
	}
	
	public void updatePesoPorErro() {
		throw new UnsupportedOperationException("Implementar");
	}
}
