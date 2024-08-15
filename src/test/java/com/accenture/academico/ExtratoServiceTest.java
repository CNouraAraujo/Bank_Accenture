package com.accenture.academico;

import com.accenture.academico.dto.ExtratoDTO;
import com.accenture.academico.dto.OperacoesClienteDTO;
import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.model.enums.TipoConta;
import com.accenture.academico.repository.ClienteRepository;
import com.accenture.academico.service.ExtratoService;
import com.accenture.academico.service.OperacoesClienteService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@SpringJUnitConfig
public class ExtratoServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private OperacoesClienteService operacoesClienteService;

    @InjectMocks
    private ExtratoService extratoService;

    public ExtratoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObterExtratoClienteNaoExistente() {
        // Given
        Integer idAgencia = 1;
        Integer idCliente = 1;

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());

        // When
        Optional<ExtratoDTO> extratoDTO = extratoService.obterExtrato(idAgencia, idCliente);

        // Then
        assertTrue(extratoDTO.isEmpty());
    }

    @Test
    void testObterExtratoClienteAgenciaIncorreta() {
        // Given
        Integer idAgencia = 1;
        Integer idCliente = 1;

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        cliente.setAgencia(new Agencia()); // Defina a agência corretamente
        cliente.getAgencia().setIdAgencia(2); // Agência errada

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));

        // When
        Optional<ExtratoDTO> extratoDTO = extratoService.obterExtrato(idAgencia, idCliente);

        // Then
        assertTrue(extratoDTO.isEmpty());
    }

    @Test
    void testObterExtrato_Success() {
        Integer idAgencia = 1;
        Integer idCliente = 1;

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        cliente.setNome("Cliente Nome");
        cliente.setCpf("123.456.789-00");
        cliente.setTelefone("123456789");
        cliente.setSenha("senha");
        cliente.setAgencia(new Agencia());
        cliente.getAgencia().setIdAgencia(idAgencia);

        ContaBancaria conta1 = new ContaBancaria();
        conta1.setSaldo(1000.0);
        conta1.setTipo(TipoConta.CONTA_CORRENTE);

        ContaBancaria conta2 = new ContaBancaria();
        conta2.setSaldo(2000.0);
        conta2.setTipo(TipoConta.CONTA_POUPANCA);

        cliente.setContaBancarias(Arrays.asList(conta1, conta2));

        OperacoesClienteDTO operacaoDTO = new OperacoesClienteDTO();
        List<OperacoesClienteDTO> operacoesDTO = Arrays.asList(operacaoDTO);

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(operacoesClienteService.getOperacoesPorCliente(idCliente)).thenReturn(operacoesDTO);

        Optional<ExtratoDTO> extratoDTOOpt = extratoService.obterExtrato(idAgencia, idCliente);

        assertTrue(extratoDTOOpt.isPresent());
        ExtratoDTO extratoDTO = extratoDTOOpt.get();
        assertNotNull(extratoDTO.getCliente());
        assertEquals(cliente.getNome(), extratoDTO.getCliente().getNome());
        assertEquals(cliente.getCpf(), extratoDTO.getCliente().getCpf());
        assertEquals(cliente.getTelefone(), extratoDTO.getCliente().getTelefone());
        assertEquals(cliente.getSenha(), extratoDTO.getCliente().getSenha());

        assertEquals(2, extratoDTO.getCliente().getContas().size());
        assertEquals(conta1.getSaldo(), extratoDTO.getCliente().getContas().get(0).getSaldo());
        assertEquals(conta1.getTipo(), extratoDTO.getCliente().getContas().get(0).getTipo());
        assertEquals(conta2.getSaldo(), extratoDTO.getCliente().getContas().get(1).getSaldo());
        assertEquals(conta2.getTipo(), extratoDTO.getCliente().getContas().get(1).getTipo());

        assertEquals(operacoesDTO, extratoDTO.getOperacoes());
    }

    @Test
    void testObterExtrato_ClienteNaoPertenceAgencia() {
        Integer idAgencia = 1;
        Integer idCliente = 1;

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        cliente.setAgencia(new Agencia());
        cliente.getAgencia().setIdAgencia(2); // Agência diferente

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));

        Optional<ExtratoDTO> extratoDTOOpt = extratoService.obterExtrato(idAgencia, idCliente);

        assertFalse(extratoDTOOpt.isPresent());
    }

    @Test
    void testObterExtrato_ClienteNaoEncontrado() {
        Integer idAgencia = 1;
        Integer idCliente = 1;

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());

        Optional<ExtratoDTO> extratoDTOOpt = extratoService.obterExtrato(idAgencia, idCliente);

        assertFalse(extratoDTOOpt.isPresent());
    }

    private ContaBancaria createContaBancaria(Integer numeroConta, Double saldo, String tipo) {
        ContaBancaria conta = new ContaBancaria();
        conta.setNumeroConta(numeroConta);
        conta.setSaldo(saldo);
        conta.setTipo(TipoConta.CONTA_POUPANCA);
        return conta;
    }
}