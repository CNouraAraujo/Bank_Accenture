package com.accenture.academico;

import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.model.enums.TipoConta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContaBancariaTest {

	private ContaBancaria contaBancaria;

	@BeforeEach
	void setUp() {
		contaBancaria = new ContaBancaria();
		contaBancaria.setNumeroConta(1);
		contaBancaria.setSaldo(1000.0);
		contaBancaria.setTipo(TipoConta.CONTA_CORRENTE);
		contaBancaria.setCliente(new Cliente());
	}

	@Test
	void testDepositar_Success() {
		contaBancaria.depositar(200.0);
		assertEquals(1200.0, contaBancaria.getSaldo(), "O saldo após o depósito não corresponde ao esperado.");
	}

	@Test
	void testDepositar_ValorNegativo() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			contaBancaria.depositar(-200.0);
		});

		assertEquals("O valor do depósito deve ser positivo.", exception.getMessage());
	}

	@Test
	void testDepositar_ValorZero() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			contaBancaria.depositar(0.0);
		});

		assertEquals("O valor do depósito deve ser positivo.", exception.getMessage());
	}

	@Test
	void testSacar_Success() {
		contaBancaria.sacar(200.0);
		assertEquals(800.0, contaBancaria.getSaldo(), "O saldo após o saque não corresponde ao esperado.");
	}

	@Test
	void testSacar_ValorNegativo() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			contaBancaria.sacar(-200.0);
		});

		assertEquals("O valor do saque deve ser positivo.", exception.getMessage());
	}

	@Test
	void testSacar_ValorZero() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			contaBancaria.sacar(0.0);
		});

		assertEquals("O valor do saque deve ser positivo.", exception.getMessage());
	}

//    @Test
//    void testSacar_SaldoInsuficiente() {
//        contaBancaria.setSaldo(100.0);
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            contaBancaria.sacar(200.0);
//        });
//
//        assertEquals("Você não tem saldo suficiente", exception.getMessage());
//    }
}
