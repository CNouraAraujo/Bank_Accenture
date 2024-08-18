package com.accenture.academico;

import com.accenture.academico.model.Agencia;
import com.accenture.academico.repository.AgenciaRepository;
import com.accenture.academico.service.AgenciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AgenciaServiceTest {

	@Mock
	private AgenciaRepository agenciaRepository;

	@InjectMocks
	private AgenciaService agenciaService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testListarAgencias() {
		// Configuração dos mocks
		Agencia agencia1 = new Agencia();
		Agencia agencia2 = new Agencia();
		when(agenciaRepository.findAll()).thenReturn(Arrays.asList(agencia1, agencia2));

		// Chamada do método a ser testado
		List<Agencia> agencias = agenciaService.listarAgencias();

		// Verificações
		assertNotNull(agencias, "A lista de agências não deve ser nula.");
		assertEquals(2, agencias.size(), "O tamanho da lista de agências não corresponde ao esperado.");
		verify(agenciaRepository, times(1)).findAll();
	}

	@Test
	void testCriarAgencia() {
		// Configuração dos mocks
		Agencia agencia = new Agencia();
		when(agenciaRepository.save(any(Agencia.class))).thenReturn(agencia);

		// Chamada do método a ser testado
		Agencia result = agenciaService.criarAgencia(agencia);

		// Verificações
		assertNotNull(result, "Agência criada não deve ser nula.");
		assertEquals(agencia, result, "A agência criada não corresponde ao esperado.");
		verify(agenciaRepository, times(1)).save(agencia);
	}

}
