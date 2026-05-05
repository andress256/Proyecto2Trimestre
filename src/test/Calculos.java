package test;

public class Calculos {

	public int suma(int x, int y) {
		return x + y;
	}
	public int suma(int... numeros ) {
		int resultado = 0;
		for(int i:numeros) {
			resultado += i;
		}
		return resultado;
	}
}
