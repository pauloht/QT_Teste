package RN;

public class Ativacao2 implements FuncaoDeAtivacao{

	@Override
	public double ativa(Neuronio neuronio) {
		// TODO Auto-generated method stub
		double sig = 1.0/(1.0+Math.pow(Math.E, -neuronio.calcularValor()));
		neuronio.setValor(sig);
		return(sig);
	}
	
}
