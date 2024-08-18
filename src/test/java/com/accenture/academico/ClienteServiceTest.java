package com.accenture.academico;

import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.repository.AgenciaRepository;
import com.accenture.academico.repository.ClienteRepository;
import com.accenture.academico.repository.ContaRepository;
import com.accenture.academico.repository.EnderecoClienteRepository;
import com.accenture.academico.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

	@Mock
	private ClienteRepository clienteRepository;

	@Mock
	private AgenciaRepository agenciaRepository;

	@Mock
	private EnderecoClienteRepository enderecoClienteRepository;

	@Mock
	private ContaRepository contaRepository;

	@InjectMocks
	private ClienteService clienteService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testListarClientesPorAgencia() {
		Integer numeroAgencia = 1;
		Cliente cliente1 = new Cliente();
		Cliente cliente2 = new Cliente();
		when(clienteRepository.findAllByAgencia_NumeroAgencia(numeroAgencia))
				.thenReturn(Arrays.asList(cliente1, cliente2));

		List<Cliente> clientes = clienteService.listarClientesPorAgencia(numeroAgencia);

		assertNotNull(clientes, "A lista de clientes não deve ser nula.");
		assertEquals(2, clientes.size(), "O tamanho da lista de clientes não corresponde ao esperado.");
		verify(clienteRepository, times(1)).findAllByAgencia_NumeroAgencia(numeroAgencia);
	}

	@Test
	void testObterClienteDeAgencia() {
		Integer numeroAgencia = 1;
		Integer idCliente = 1;
		Cliente cliente = new Cliente();
		when(clienteRepository.findByIdAndAgencia_NumeroAgencia(idCliente, numeroAgencia))
				.thenReturn(Optional.of(cliente));

		Optional<Cliente> result = clienteService.obterClienteDeAgencia(numeroAgencia, idCliente);

		assertTrue(result.isPresent(), "Cliente deveria estar presente.");
		assertEquals(cliente, result.get(), "O cliente retornado não corresponde ao esperado.");
		verify(clienteRepository, times(1)).findByIdAndAgencia_NumeroAgencia(idCliente, numeroAgencia);
	}

	@Test
	void testDeletarCliente_ClienteNaoEncontrado() {
		Integer numeroAgencia = 1;
		Integer idCliente = 1;

		when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());

		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			clienteService.deletarCliente(numeroAgencia, idCliente);
		});

		assertEquals("Cliente não encontrado", thrown.getMessage());
		verify(clienteRepository, times(0)).delete(any(Cliente.class));
	}

	@Test
	void testDeletarCliente_ClienteNaoPertenceAgencia() {
		Integer numeroAgencia = 1;
		Integer idCliente = 1;
		Cliente cliente = new Cliente();
		Agencia agencia = new Agencia();
		agencia.setNumeroAgencia(2); // Agência diferente
		cliente.setAgencia(agencia);

		when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));

		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			clienteService.deletarCliente(numeroAgencia, idCliente);
		});

		assertEquals("Cliente não pertence à agência informada", thrown.getMessage());
		verify(clienteRepository, times(0)).delete(any(Cliente.class));
	}

}
