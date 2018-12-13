package QTEstatico;

public class No {
	private No[] filhos = null;
	private double[] valor = null;
	private int id = -1;
	private static int idCounter = 0;
	
	private void setId() {
		this.id = idCounter;
		idCounter++;
	}
	
	public No(No[] filhos) {
		this.setId();
		this.filhos = filhos;
	}
	public No(double[] valor) {
		this.setId();
		this.valor = valor;
	}
	
	public No[] getFilhos() {
		return(filhos);
	}
	
	public double[] getValor() {
		return(valor);
	}
	
	@Override
	public String toString() {
		return(Integer.toString(id));
	}
}
