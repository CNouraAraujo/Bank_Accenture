package com.accenture.academico;

import com.accenture.academico.dto.ExtratoDTO;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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