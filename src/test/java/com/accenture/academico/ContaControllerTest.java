package com.accenture.academico;

import com.accenture.academico.controller.ContaController;
import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.model.enums.TipoConta;
import com.accenture.academico.service.ContaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@WebMvcTest(ContaController.class)
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testListarContasPorCliente_Success() throws Exception {
        ContaBancaria conta = new ContaBancaria();
        conta.setNumeroConta(1);
        conta.setSaldo(1000.0);
        conta.setTipo(TipoConta.CONTA_CORRENTE);

        when(contaService.listarContasPorCliente(anyInt(), anyInt())).thenReturn(Optional.of(Collections.singletonList(conta)));

        mockMvc.perform(MockMvcRequestBuilders.get("/bank/1/clientes/1/contas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numeroConta").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].saldo").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tipo").value("Corrente"));

        verify(contaService, times(1)).listarContasPorCliente(anyInt(), anyInt());
    }

    @Test
    void testListarContasPorCliente_NotFound() throws Exception {
        when(contaService.listarContasPorCliente(anyInt(), anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/bank/1/clientes/1/contas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(contaService, times(1)).listarContasPorCliente(anyInt(), anyInt());
    }

    @Test
    void testCriarConta_Success() throws Exception {
        ContaBancaria conta = new ContaBancaria();
        conta.setNumeroConta(1);
        conta.setSaldo(1000.0);
        conta.setTipo(TipoConta.CONTA_CORRENTE);

        when(contaService.criarConta(anyInt(), anyInt(), any(ContaBancaria.class))).thenReturn(conta);

        mockMvc.perform(MockMvcRequestBuilders.post("/bank/1/clientes/1/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conta)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numeroConta").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saldo").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tipo").value("Corrente"));

        verify(contaService, times(1)).criarConta(anyInt(), anyInt(), any(ContaBancaria.class));
    }

    @Test
    void testCriarConta_InternalServerError() throws Exception {
        when(contaService.criarConta(anyInt(), anyInt(), any(ContaBancaria.class)))
                .thenThrow(new RuntimeException("Erro interno"));

        mockMvc.perform(MockMvcRequestBuilders.post("/bank/1/clientes/1/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ContaBancaria())))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());

        verify(contaService, times(1)).criarConta(anyInt(), anyInt(), any(ContaBancaria.class));
    }

    @Test
    void testSacar_Success() throws Exception {
        ContaBancaria conta = new ContaBancaria();
        conta.setNumeroConta(1);
        conta.setSaldo(800.0);
        conta.setTipo(TipoConta.CONTA_CORRENTE);

        when(contaService.sacar(anyInt(), anyDouble())).thenReturn(conta);

        Map<String, Double> request = new HashMap<>();
        request.put("valor", 200.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/bank/1/clientes/1/contas/1/saque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numeroConta").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saldo").value(800.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tipo").value("Corrente"));

        verify(contaService, times(1)).sacar(anyInt(), anyDouble());
    }

    @Test
    void testSacar_InternalServerError() throws Exception {
        when(contaService.sacar(anyInt(), anyDouble())).thenThrow(new RuntimeException("Erro interno"));

        Map<String, Double> request = new HashMap<>();
        request.put("valor", 200.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/bank/1/clientes/1/contas/1/saque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());

        verify(contaService, times(1)).sacar(anyInt(), anyDouble());
    }

    @Test
    void testTransferir_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/bank/1/clientes/1/contas/1/transferencia")
                        .param("idAgenciaDestinataria", "2")
                        .param("idClienteDestinatario", "2")
                        .param("idContaDestinataria", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("500.0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Transferência realizada com sucesso."));

        verify(contaService, times(1)).transferir(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyDouble());
    }

//    @Test
//    void testTransferir_BadRequest() throws Exception {
//        when(contaService.transferir(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyDouble()))
//                .thenThrow(new IllegalArgumentException("Valor inválido"));
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/bank/1/clientes/1/contas/1/transferencia")
//                        .param("idAgenciaDestinataria", "2")
//                        .param("idClienteDestinatario", "2")
//                        .param("idContaDestinataria", "2")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("500.0"))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.content().string("Valor inválido"));
//
//        verify(contaService, times(1)).transferir(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyDouble());
//    }
}
