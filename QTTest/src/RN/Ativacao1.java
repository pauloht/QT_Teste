package RN;

public class Ativacao1 implements FuncaoDeAtivacao{

	@Override
	public double ativa(Neuronio neuronio) {
		if (neuronio.calcularValor() > 0.0) {
			neuronio.setValor(1.0);
			return(1.0);
		}
		neuronio.setValor(0.0);
		return(0.0);
	}
}
