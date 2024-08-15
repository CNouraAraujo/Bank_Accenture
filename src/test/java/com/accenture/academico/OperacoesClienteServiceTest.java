package com.accenture.academico;

import com.accenture.academico.dto.OperacoesClienteDTO;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.OperacoesCliente;
import com.accenture.academico.model.enums.TipoOperacao;
import com.accenture.academico.repository.OperacoesClienteRepository;
import com.accenture.academico.service.OperacoesClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@SpringJUnitConfig
public class OperacoesClienteServiceTest {

    @Mock
    private OperacoesClienteRepository operacoesClienteRepository;

    @InjectMocks
    private OperacoesClienteService operacoesClienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOperacoesPorCliente() {
        // Given
        Integer idCliente = 1;

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        cliente.setNome("João");
        cliente.setCpf("12345678900");

        OperacoesCliente operacao1 = new OperacoesCliente();
        operacao1.setId(1);
        operacao1.setTipoOperacao(TipoOperacao.DEPOSITO);
        operacao1.setValor(500.0);
        operacao1.setDataOperacao(LocalDateTime.now());
        operacao1.setCliente(cliente);

        OperacoesCliente operacao2 = new OperacoesCliente();
        operacao2.setId(2);
        operacao2.setTipoOperacao(TipoOperacao.SAQUE);
        operacao2.setValor(200.0);
        operacao2.setDataOperacao(LocalDateTime.now());
        operacao2.setCliente(cliente);

        when(operacoesClienteRepository.findByCliente_Id(idCliente)).thenReturn(Arrays.asList(operacao1, operacao2));

        // When
        List<OperacoesClienteDTO> operacoesDTO = operacoesClienteService.getOperacoesPorCliente(idCliente);

        // Then
        assertEquals(2, operacoesDTO.size());
        OperacoesClienteDTO dto1 = operacoesDTO.get(0);
        assertEquals(1, dto1.getId());
        assertEquals(TipoOperacao.DEPOSITO, dto1.getTipoOperacao());
        assertEquals(500.0, dto1.getValor());
        assertEquals(cliente.getId(), dto1.getCliente().getId());
        assertEquals(cliente.getNome(), dto1.getCliente().getNome());
        assertEquals(cliente.getCpf(), dto1.getCliente().getCpf());
    }

    @Test
    void testGetOperacoesPorClienteSemOperacoes() {
        // Given
        Integer idCliente = 1;

        when(operacoesClienteRepository.findByCliente_Id(idCliente)).thenReturn(Collections.emptyList());

        // When
        List<OperacoesClienteDTO> operacoesDTO = operacoesClienteService.getOperacoesPorCliente(idCliente);

        // Then
        assertTrue(operacoesDTO.isEmpty());
    }

    @Test
    void testGetOperacoesPorClienteComClienteInvalido() {
        // Given
        Integer idCliente = null;

        // Quando o cliente é inválido ou nulo
        List<OperacoesClienteDTO> operacoesDTO = operacoesClienteService.getOperacoesPorCliente(idCliente);

        // Então
        assertTrue(operacoesDTO.isEmpty());
    }

    @Test
    void testGetOperacoesPorClienteComOperacoesVazias() {
        // Given
        Integer idCliente = 2;

        OperacoesCliente operacao = new OperacoesCliente();
        operacao.setId(3);
        operacao.setTipoOperacao(TipoOperacao.TRANSFERENCIA_DESTINATARIO);
        operacao.setValor(100.0);
        operacao.setDataOperacao(LocalDateTime.now());
        operacao.setCliente(new Cliente()); // Cliente vazio

        when(operacoesClienteRepository.findByCliente_Id(idCliente)).thenReturn(Collections.singletonList(operacao));

        // When
        List<OperacoesClienteDTO> operacoesDTO = operacoesClienteService.getOperacoesPorCliente(idCliente);

        // Then
        assertEquals(1, operacoesDTO.size());
        OperacoesClienteDTO dto = operacoesDTO.get(0);
        assertEquals(3, dto.getId());
        assertEquals(TipoOperacao.TRANSFERENCIA_DESTINATARIO, dto.getTipoOperacao());
        assertEquals(100.0, dto.getValor());
        assertEquals(null, dto.getCliente().getId()); // Cliente não deve estar presente
    }
}