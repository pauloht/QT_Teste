package RN;

public class Ativacao1 implements FuncaoDeAtivacao{

	@Override
	public void ativa(Neuronio neuronio) {
		// TODO Auto-generated method stub
		if (neuronio.calcularValor() > 0.0) {
			neuronio.ativo = true;
		}
		neuronio.ativo = false;
	}

	@Override
	public void ativa(Neuronio neuronio, double valor) {
		if (valor > 0.0) {
			neuronio.ativo = true;
		}
		neuronio.ativo = false;
	}

}
