//package com.accenture.academico;
//
//import com.accenture.academico.dto.ClienteDTO;
//import com.accenture.academico.dto.ClienteEnderecoDTO;
//import com.accenture.academico.model.Agencia;
//import com.accenture.academico.model.Cliente;
//import com.accenture.academico.model.EnderecoCliente;
//import com.accenture.academico.repository.AgenciaRepository;
//import com.accenture.academico.repository.ClienteRepository;
//import com.accenture.academico.repository.ContaRepository;
//import com.accenture.academico.repository.EnderecoClienteRepository;
//import com.accenture.academico.service.ClienteService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class ClienteServiceTest {
//
//    @Mock
//    private ClienteRepository clienteRepository;
//
//    @Mock
//    private AgenciaRepository agenciaRepository;
//
//    @Mock
//    private EnderecoClienteRepository enderecoClienteRepository;
//
//    @Mock
//    private ContaRepository contaRepository;
//
//    @InjectMocks
//    private ClienteService clienteService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testListarClientesPorAgencia() {
//        // Configuração dos mocks
//        Integer idAgencia = 1;
//        Cliente cliente1 = new Cliente();
//        Cliente cliente2 = new Cliente();
//        when(clienteRepository.findAllByAgencia_IdAgencia(idAgencia))
//                .thenReturn(Arrays.asList(cliente1, cliente2));
//
//        // Chamada do método a ser testado
//        List<Cliente> clientes = clienteService.listarClientesPorAgencia(idAgencia);
//
//        // Verificações
//        assertNotNull(clientes, "A lista de clientes não deve ser nula.");
//        assertEquals(2, clientes.size(), "O tamanho da lista de clientes não corresponde ao esperado.");
//        verify(clienteRepository, times(1)).findAllByAgencia_IdAgencia(idAgencia);
//    }
//
//    @Test
//    void testObterClienteDeAgencia() {
//        Integer idAgencia = 1;
//        Integer idCliente = 1;
//        Cliente cliente = new Cliente();
//        when(clienteRepository.findByIdAndAgencia_IdAgencia(idCliente, idAgencia))
//                .thenReturn(Optional.of(cliente));
//
//        Optional<Cliente> result = clienteService.obterClienteDeAgencia(idAgencia, idCliente);
//
//        assertTrue(result.isPresent(), "Cliente deveria estar presente.");
//        assertEquals(cliente, result.get(), "O cliente retornado não corresponde ao esperado.");
//        verify(clienteRepository, times(1)).findByIdAndAgencia_IdAgencia(idCliente, idAgencia);
//    }
//
//    @Test
//    void testDeletarCliente_ClienteNaoEncontrado() {
//        Integer idAgencia = 1;
//        Integer idCliente = 1;
//
//        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());
//
//        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
//            clienteService.deletarCliente(idAgencia, idCliente);
//        });
//
//        assertEquals("Cliente não encontrado", thrown.getMessage());
//        verify(clienteRepository, times(0)).delete(any(Cliente.class));
//    }
//
//    @Test
//    void testDeletarCliente_ClienteNaoPertenceAgencia() {
//        Integer idAgencia = 1;
//        Integer idCliente = 1;
//        Cliente cliente = new Cliente();
//        Agencia agencia = new Agencia();
//        agencia.setIdAgencia(2); // Agência diferente
//        cliente.setAgencia(agencia);
//
//        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
//
//        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
//            clienteService.deletarCliente(idAgencia, idCliente);
//        });
//
//        assertEquals("Cliente não pertence à agência informada", thrown.getMessage());
//        verify(clienteRepository, times(0)).delete(any(Cliente.class));
//    }
//
//    @Test
//    void testCriarCliente() {
//        Integer idAgencia = 1;
//        Agencia agencia = new Agencia();
//        agencia.setIdAgencia(idAgencia);
//
//        EnderecoCliente endereco = new EnderecoCliente();
//        ClienteEnderecoDTO dto = new ClienteEnderecoDTO();
//        dto.setNome("Cliente Teste");
//        dto.setCpf("12345678900");
//        dto.setTelefone("123456789");
//        dto.setSenha("senha123");
//        dto.setEndereco(endereco);
//        dto.setContas(Collections.emptyList());
//
//        when(agenciaRepository.findById(idAgencia)).thenReturn(Optional.of(agencia));
//        when(enderecoClienteRepository.save(any(EnderecoCliente.class))).thenAnswer(i -> i.getArguments()[0]);
//        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArguments()[0]);
//        when(contaRepository.saveAll(anyList())).thenAnswer(i -> i.getArguments()[0]);
//
//        Cliente cliente = clienteService.criarCliente(idAgencia, dto);
//
//        assertNotNull(cliente, "Cliente não deve ser nulo.");
//        assertEquals(dto.getNome(), cliente.getNome(), "Nome do cliente não corresponde ao esperado.");
//        assertEquals(dto.getCpf(), cliente.getCpf(), "CPF do cliente não corresponde ao esperado.");
//        assertEquals(dto.getTelefone(), cliente.getTelefone(), "Telefone do cliente não corresponde ao esperado.");
//        assertEquals(dto.getSenha(), cliente.getSenha(), "Senha do cliente não corresponde ao esperado.");
//        assertEquals(endereco, cliente.getEnderecoCliente(), "Endereço do cliente não corresponde ao esperado.");
//        assertEquals(agencia, cliente.getAgencia(), "Agência do cliente não corresponde ao esperado.");
//        assertTrue(cliente.getContaBancarias().isEmpty(), "A lista de contas bancárias do cliente deve estar vazia.");
//
//        verify(enderecoClienteRepository, times(1)).save(endereco);
//        verify(clienteRepository, times(1)).save(any(Cliente.class));
//    }
//
//    @Test
//    void testAtualizarCliente() {
//        Integer idCliente = 1;
//        ClienteDTO dto = new ClienteDTO("Nome Atualizado", "12345678900", "123456789", "senha123", 1, 1);
//
//        Cliente cliente = new Cliente();
//        cliente.setId(idCliente);
//        cliente.setNome("Nome Original");
//        cliente.setCpf("98765432100");
//        cliente.setTelefone("987654321");
//        cliente.setSenha("senhaOriginal");
//
//        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
//        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArguments()[0]);
//
//        Optional<Cliente> clienteAtualizado = clienteService.atualizarCliente(idCliente, dto);
//
//        assertTrue(clienteAtualizado.isPresent(), "O cliente atualizado deveria estar presente.");
//        Cliente clienteRetornado = clienteAtualizado.get();
//        assertEquals(dto.nome(), clienteRetornado.getNome(), "O nome do cliente não corresponde ao esperado.");
//        assertEquals(dto.cpf(), clienteRetornado.getCpf(), "O CPF do cliente não corresponde ao esperado.");
//        assertEquals(dto.telefone(), clienteRetornado.getTelefone(), "O telefone do cliente não corresponde ao esperado.");
//        assertEquals(dto.senha(), clienteRetornado.getSenha(), "A senha do cliente não corresponde ao esperado.");
//
//        verify(clienteRepository, times(1)).findById(idCliente);
//        verify(clienteRepository, times(1)).save(clienteRetornado);
//    }
//
//    @Test
//    void testAtualizarCliente_ClienteNaoEncontrado() {
//        Integer idCliente = 1;
//        ClienteDTO dto = new ClienteDTO("Nome Atualizado", "12345678900", "123456789", "senha123", 1, 1);
//
//        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());
//
//        Optional<Cliente> clienteAtualizado = clienteService.atualizarCliente(idCliente, dto);
//
//        assertFalse(clienteAtualizado.isPresent(), "O cliente atualizado deveria estar ausente.");
//        verify(clienteRepository, times(1)).findById(idCliente);
//        verify(clienteRepository, times(0)).save(any(Cliente.class));
//    }
//}

package com.accenture.academico;

import com.accenture.academico.dto.ClienteDTO;
import com.accenture.academico.dto.ClienteEnderecoDTO;
import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.EnderecoCliente;
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
import java.util.Collections;
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
        Integer idAgencia = 1;
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        when(clienteRepository.findAllByAgencia_IdAgencia(idAgencia))
                .thenReturn(Arrays.asList(cliente1, cliente2));

        List<Cliente> clientes = clienteService.listarClientesPorAgencia(idAgencia);

        assertNotNull(clientes, "A lista de clientes não deve ser nula.");
        assertEquals(2, clientes.size(), "O tamanho da lista de clientes não corresponde ao esperado.");
        verify(clienteRepository, times(1)).findAllByAgencia_IdAgencia(idAgencia);
    }

    @Test
    void testObterClienteDeAgencia() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Cliente cliente = new Cliente();
        when(clienteRepository.findByIdAndAgencia_IdAgencia(idCliente, idAgencia))
                .thenReturn(Optional.of(cliente));

        Optional<Cliente> result = clienteService.obterClienteDeAgencia(idAgencia, idCliente);

        assertTrue(result.isPresent(), "Cliente deveria estar presente.");
        assertEquals(cliente, result.get(), "O cliente retornado não corresponde ao esperado.");
        verify(clienteRepository, times(1)).findByIdAndAgencia_IdAgencia(idCliente, idAgencia);
    }

    @Test
    void testDeletarCliente_ClienteNaoEncontrado() {
        Integer idAgencia = 1;
        Integer idCliente = 1;

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            clienteService.deletarCliente(idAgencia, idCliente);
        });

        assertEquals("Cliente não encontrado", thrown.getMessage());
        verify(clienteRepository, times(0)).delete(any(Cliente.class));
    }

    @Test
    void testDeletarCliente_ClienteNaoPertenceAgencia() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Cliente cliente = new Cliente();
        Agencia agencia = new Agencia();
        agencia.setIdAgencia(2); // Agência diferente
        cliente.setAgencia(agencia);

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            clienteService.deletarCliente(idAgencia, idCliente);
        });

        assertEquals("Cliente não pertence à agência informada", thrown.getMessage());
        verify(clienteRepository, times(0)).delete(any(Cliente.class));
    }

    @Test
    void testCriarCliente() {
        Integer idAgencia = 1;
        Agencia agencia = new Agencia();
        agencia.setIdAgencia(idAgencia);

        EnderecoCliente endereco = new EnderecoCliente();
        ClienteEnderecoDTO dto = new ClienteEnderecoDTO();
        dto.setNome("Cliente Teste");
        dto.setCpf("12345678900");
        dto.setTelefone("123456789");
        dto.setSenha("senha123");
        dto.setEndereco(endereco);
        dto.setContas(Collections.emptyList());

        when(agenciaRepository.findById(idAgencia)).thenReturn(Optional.of(agencia));
        when(enderecoClienteRepository.save(any(EnderecoCliente.class))).thenAnswer(i -> i.getArguments()[0]);
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArguments()[0]);
        when(contaRepository.saveAll(anyList())).thenAnswer(i -> i.getArguments()[0]);

        Cliente cliente = clienteService.criarCliente(idAgencia, dto);

        assertNotNull(cliente, "Cliente não deve ser nulo.");
        assertEquals(dto.getNome(), cliente.getNome(), "Nome do cliente não corresponde ao esperado.");
        assertEquals(dto.getCpf(), cliente.getCpf(), "CPF do cliente não corresponde ao esperado.");
        assertEquals(dto.getTelefone(), cliente.getTelefone(), "Telefone do cliente não corresponde ao esperado.");
        assertEquals(dto.getSenha(), cliente.getSenha(), "Senha do cliente não corresponde ao esperado.");
        assertEquals(endereco, cliente.getEnderecoCliente(), "Endereço do cliente não corresponde ao esperado.");
        assertEquals(agencia, cliente.getAgencia(), "Agência do cliente não corresponde ao esperado.");
        assertTrue(cliente.getContaBancarias().isEmpty(), "A lista de contas bancárias do cliente deve estar vazia.");

        verify(enderecoClienteRepository, times(1)).save(endereco);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testAtualizarCliente() {
        Integer idCliente = 1;
        ClienteDTO dto = new ClienteDTO("Nome Atualizado", "12345678900", "123456789", "senha123", 1, 1);

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        cliente.setNome("Nome Original");
        cliente.setCpf("98765432100");
        cliente.setTelefone("987654321");
        cliente.setSenha("senhaOriginal");

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<Cliente> clienteAtualizado = clienteService.atualizarCliente(idCliente, dto);

        assertTrue(clienteAtualizado.isPresent(), "O cliente atualizado deveria estar presente.");
        Cliente clienteRetornado = clienteAtualizado.get();
        assertEquals(dto.nome(), clienteRetornado.getNome(), "O nome do cliente não corresponde ao esperado.");
        assertEquals(dto.cpf(), clienteRetornado.getCpf(), "O CPF do cliente não corresponde ao esperado.");
        assertEquals(dto.telefone(), clienteRetornado.getTelefone(), "O telefone do cliente não corresponde ao esperado.");
        assertEquals(dto.senha(), clienteRetornado.getSenha(), "A senha do cliente não corresponde ao esperado.");

        verify(clienteRepository, times(1)).findById(idCliente);
        verify(clienteRepository, times(1)).save(clienteRetornado);
    }

    @Test
    void testAtualizarCliente_ClienteNaoEncontrado() {
        Integer idCliente = 1;
        ClienteDTO dto = new ClienteDTO("Nome Atualizado", "12345678900", "123456789", "senha123", 1, 1);

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());

        Optional<Cliente> clienteAtualizado = clienteService.atualizarCliente(idCliente, dto);

        assertFalse(clienteAtualizado.isPresent(), "O cliente atualizado deveria estar ausente.");
        verify(clienteRepository, times(1)).findById(idCliente);
        verify(clienteRepository, times(0)).save(any(Cliente.class));
    }

    @Test
    void testListarClientesPorAgenciaNenhumCliente() {
        Integer idAgencia = 1;
        when(clienteRepository.findAllByAgencia_IdAgencia(idAgencia))
                .thenReturn(Collections.emptyList());

        List<Cliente> clientes = clienteService.listarClientesPorAgencia(idAgencia);

        assertNotNull(clientes, "A lista de clientes não deve ser nula.");
        assertTrue(clientes.isEmpty(), "A lista de clientes deveria estar vazia.");
        verify(clienteRepository, times(1)).findAllByAgencia_IdAgencia(idAgencia);
    }

    @Test
    void testCriarClienteAgenciaNaoEncontrada() {
        Integer idAgencia = 1;
        ClienteEnderecoDTO dto = new ClienteEnderecoDTO();
        dto.setNome("Cliente Teste");
        dto.setCpf("12345678900");
        dto.setTelefone("123456789");
        dto.setSenha("senha123");

        when(agenciaRepository.findById(idAgencia)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            clienteService.criarCliente(idAgencia, dto);
        });

        assertEquals("Agência não encontrada", thrown.getMessage());
        verify(clienteRepository, times(0)).save(any(Cliente.class));
        verify(enderecoClienteRepository, times(0)).save(any(EnderecoCliente.class));
    }

}
