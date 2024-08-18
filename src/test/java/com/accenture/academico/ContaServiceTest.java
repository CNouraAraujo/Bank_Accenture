package com.accenture.academico;

import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.model.OperacoesCliente;
import com.accenture.academico.repository.AgenciaRepository;
import com.accenture.academico.repository.ClienteRepository;
import com.accenture.academico.repository.ContaRepository;
import com.accenture.academico.repository.OperacoesClienteRepository;
import com.accenture.academico.service.ClienteService;
import com.accenture.academico.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private AgenciaRepository agenciaRepository;

    @Mock
    private OperacoesClienteRepository operacoesClienteRepository;

    private Cliente cliente;
    private ContaBancaria contaBancaria;
    private Agencia agencia;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        agencia = new Agencia();
        agencia.setIdAgencia(1);

        cliente = new Cliente();
        cliente.setId(1);
        cliente.setAgencia(agencia);

        contaBancaria = new ContaBancaria();
        contaBancaria.setNumeroConta(1);
        contaBancaria.setSaldo(1000.0);
        contaBancaria.setCliente(cliente);
    }

//    @Test
//    void testCriarConta_Success() throws Exception {
//        when(agenciaRepository.findById(1)).thenReturn(Optional.of(agencia));
//        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
//        when(contaRepository.save(any(ContaBancaria.class))).thenReturn(contaBancaria);
//
//        ContaBancaria novaConta = contaService.criarConta(1, 1, contaBancaria);
//
//        assertNotNull(novaConta, "A conta bancária criada não deve ser nula.");
//        assertEquals(cliente, novaConta.getCliente(), "O cliente associado à conta criada não corresponde ao esperado.");
//        verify(clienteRepository, times(1)).save(cliente);
//    }

    @Test
    void testCriarConta_AgenciaNaoEncontrada() {
        when(agenciaRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            contaService.criarConta(1, 1, contaBancaria);
        });

        assertEquals("Agência não encontrada", exception.getMessage());
    }

//    @Test
//    void testCriarConta_ClienteNaoEncontrado() {
//        when(agenciaRepository.findById(1)).thenReturn(Optional.of(agencia));
//        when(clienteRepository.findById(1)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            contaService.criarConta(1, 1, contaBancaria);
//        });
//
//        assertEquals("Cliente não encontrado", exception.getMessage());
//    }

    @Test
    void testDepositar_Success() {
        when(contaRepository.findById(1)).thenReturn(Optional.of(contaBancaria));
        when(contaRepository.save(any(ContaBancaria.class))).thenReturn(contaBancaria);

        ContaBancaria contaAtualizada = contaService.depositar(1, 500.0);

        assertEquals(1500.0, contaAtualizada.getSaldo(), 0.01, "O saldo após o depósito não corresponde ao esperado.");
        verify(operacoesClienteRepository, times(1)).save(any(OperacoesCliente.class));
    }

    @Test
    void testDepositar_ContaNaoEncontrada() {
        when(contaRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            contaService.depositar(1, 500.0);
        });

        assertEquals("Conta não encontrada", exception.getMessage());
    }

    @Test
    void testSacar_Success() {
        when(contaRepository.findById(1)).thenReturn(Optional.of(contaBancaria));
        when(contaRepository.save(any(ContaBancaria.class))).thenReturn(contaBancaria);

        ContaBancaria contaAtualizada = contaService.sacar(1, 500.0);

        assertEquals(498.50, contaAtualizada.getSaldo(), 0.01, "O saldo após o saque não corresponde ao esperado.");
        verify(operacoesClienteRepository, times(1)).save(any(OperacoesCliente.class));
    }

    @Test
    void testSacar_SaldoInsuficiente() {
        when(contaRepository.findById(1)).thenReturn(Optional.of(contaBancaria));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.sacar(1, 1000.0);
        });

        assertEquals("Saldo insuficiente na conta.", exception.getMessage());
    }

    @Test
    void testSacar_ContaNaoEncontrada() {
        when(contaRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            contaService.sacar(1, 500.0);
        });

        assertEquals("Conta não encontrada", exception.getMessage());
    }

    @Test
    void testTransferir_ContaRemetenteNaoEncontrada() {
        when(contaRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.transferir(1, 1, 1, 1, 1, 2, 200.0);
        });

        assertEquals("Conta remetente não encontrada", exception.getMessage());
    }

    @Test
    void testListarContasPorCliente_ClienteNaoEncontrado() {
        when(clienteService.obterClienteDeAgencia(1, 1)).thenReturn(Optional.empty());

        Optional<List<ContaBancaria>> contas = contaService.listarContasPorCliente(1, 1);

        assertFalse(contas.isPresent(), "A lista de contas não deve ser encontrada.");
    }

    @Test
    void testDeletarConta_Success() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Integer idConta = 1;

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        cliente.setAgencia(new Agencia());
        cliente.getAgencia().setIdAgencia(idAgencia);

        ContaBancaria conta = new ContaBancaria();
        conta.setNumeroConta(idConta);
        conta.setCliente(cliente);

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(contaRepository.findById(idConta)).thenReturn(Optional.of(conta));

        assertDoesNotThrow(() -> contaService.deletarConta(idAgencia, idCliente, idConta));

        verify(contaRepository, times(1)).delete(conta);
    }

    @Test
    void testDeletarConta_ClienteNaoEncontrado() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Integer idConta = 1;

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                contaService.deletarConta(idAgencia, idCliente, idConta)
        );
        assertEquals("Cliente não encontrado", thrown.getMessage());

        verify(contaRepository, never()).delete(any(ContaBancaria.class));
    }

    @Test
    void testDeletarConta_ClienteNaoPertenceAgencia() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Integer idConta = 1;

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        cliente.setAgencia(new Agencia());
        cliente.getAgencia().setIdAgencia(2); // agência diferente

        ContaBancaria conta = new ContaBancaria();
        conta.setNumeroConta(idConta);
        conta.setCliente(cliente);

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(contaRepository.findById(idConta)).thenReturn(Optional.of(conta));

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                contaService.deletarConta(idAgencia, idCliente, idConta)
        );
        assertEquals("Cliente não pertence à agência informada", thrown.getMessage());

        verify(contaRepository, never()).delete(any(ContaBancaria.class));
    }

    @Test
    void testDeletarConta_ContaNaoEncontrada() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Integer idConta = 1;

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        cliente.setAgencia(new Agencia());
        cliente.getAgencia().setIdAgencia(idAgencia);

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(contaRepository.findById(idConta)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                contaService.deletarConta(idAgencia, idCliente, idConta)
        );
        assertEquals("Conta não encontrada", thrown.getMessage());

        verify(contaRepository, never()).delete(any(ContaBancaria.class));
    }

    @Test
    void testDeletarConta_ContaNaoPertenceCliente() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Integer idConta = 1;

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        cliente.setAgencia(new Agencia());
        cliente.getAgencia().setIdAgencia(idAgencia);

        ContaBancaria conta = new ContaBancaria();
        conta.setNumeroConta(idConta);
        Cliente clienteDiferente = new Cliente();
        clienteDiferente.setId(2); // cliente diferente
        conta.setCliente(clienteDiferente);

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(contaRepository.findById(idConta)).thenReturn(Optional.of(conta));

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                contaService.deletarConta(idAgencia, idCliente, idConta)
        );
        assertEquals("Conta não pertence ao cliente informado", thrown.getMessage());

        verify(contaRepository, never()).delete(any(ContaBancaria.class));
    }
}
