package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Main {

	public static void main(String[] args) {
		Calculos c = new Calculos();
		c.suma(1,2,3,4,5,6,7,8,9);
	}

	@Test
	public void comprobacionSuma() {
		Calculos c = new Calculos();
		int resultado = c.suma(4, 5);
		assertEquals(9, resultado);
	}

	public void comprobacionSuma1() {
		Calculos c = new Calculos();
		int resultado = c.suma(4, 5);
		assertEquals(9, resultado);
	}

	public void comprobacionSuma2() {
		Calculos c = new Calculos();
		int resultado = c.suma(4, 5);
		assertEquals(9, resultado);
	}
}
