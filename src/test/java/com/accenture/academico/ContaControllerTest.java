//package com.accenture.academico;
//
//import com.accenture.academico.controller.ContaController;
//import com.accenture.academico.model.ContaBancaria;
//import com.accenture.academico.service.ContaService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class ContaControllerTest {
//
//    @Mock
//    private ContaService contaService;
//
//    @InjectMocks
//    private ContaController contaController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testListarContasPorCliente_Success() {
//        Integer idAgencia = 1;
//        Integer idCliente = 1;
//        ContaBancaria conta1 = new ContaBancaria();
//        ContaBancaria conta2 = new ContaBancaria();
//        when(contaService.listarContasPorCliente(idAgencia, idCliente)).thenReturn(Optional.of(Arrays.asList(conta1, conta2)));
//
//        ResponseEntity<List<ContaBancaria>> response = contaController.listarContasPorCliente(idAgencia, idCliente);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(2, response.getBody().size());
//        verify(contaService, times(1)).listarContasPorCliente(idAgencia, idCliente);
//    }
//
//    @Test
//    void testListarContasPorCliente_NotFound() {
//        Integer idAgencia = 1;
//        Integer idCliente = 1;
//        when(contaService.listarContasPorCliente(idAgencia, idCliente)).thenReturn(Optional.empty());
//
//        ResponseEntity<List<ContaBancaria>> response = contaController.listarContasPorCliente(idAgencia, idCliente);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        verify(contaService, times(1)).listarContasPorCliente(idAgencia, idCliente);
//    }
//
//    @Test
//    void testObterContaPorId_Success() {
//        Integer idAgencia = 1;
//        Integer idCliente = 1;
//        Integer idConta = 1;
//        ContaBancaria conta = new ContaBancaria();
//        when(contaService.obterContaPorId(idAgencia, idCliente, idConta)).thenReturn(Optional.of(conta));
//
//        ResponseEntity<ContaBancaria> response = contaController.obterContaPorId(idAgencia, idCliente, idConta);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(conta, response.getBody());
//        verify(contaService, times(1)).obterContaPorId(idAgencia, idCliente, idConta);
//    }
//
//    @Test
//    void testObterContaPorId_NotFound() {
//        Integer idAgencia = 1;
//        Integer idCliente = 1;
//        Integer idConta = 1;
//        when(contaService.obterContaPorId(idAgencia, idCliente, idConta)).thenReturn(Optional.empty());
//
//        ResponseEntity<ContaBancaria> response = contaController.obterContaPorId(idAgencia, idCliente, idConta);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        verify(contaService, times(1)).obterContaPorId(idAgencia, idCliente, idConta);
//    }
//
//    @Test
//    void testCriarConta_Success() throws Exception {
//        Integer idAgencia = 1;
//        Integer idCliente = 1;
//        ContaBancaria contaBancaria = new ContaBancaria();
//        contaBancaria.setNumeroConta(12345);
//        when(contaService.criarConta(idAgencia, idCliente, contaBancaria)).thenReturn(contaBancaria);
//
//        ResponseEntity<ContaBancaria> response = contaController.criarConta(idAgencia, idCliente, contaBancaria);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals("/bank/" + idAgencia + "/clientes/" + idCliente + "/contas/" + contaBancaria.getNumeroConta(), response.getHeaders().getLocation().toString());
//        assertEquals(contaBancaria, response.getBody());
//        verify(contaService, times(1)).criarConta(idAgencia, idCliente, contaBancaria);
//    }
//
//    @Test
//    void testCriarConta_InternalServerError() throws Exception {
//        Integer idAgencia = 1;
//        Integer idCliente = 1;
//        ContaBancaria contaBancaria = new ContaBancaria();
//        when(contaService.criarConta(idAgencia, idCliente, contaBancaria)).thenThrow(new RuntimeException());
//
//        ResponseEntity<ContaBancaria> response = contaController.criarConta(idAgencia, idCliente, contaBancaria);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertNull(response.getBody());
//        verify(contaService, times(1)).criarConta(idAgencia, idCliente, contaBancaria);
//    }
//
//    @Test
//    void testDeletarConta_Success() {
//        Integer idAgencia = 1;
//        Integer idCliente = 1;
//        Integer idConta = 1;
//        doNothing().when(contaService).deletarConta(idAgencia, idCliente, idConta);
//
//        ResponseEntity<Void> response = contaController.deletarConta(idAgencia, idCliente, idConta);
//
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        verify(contaService, times(1)).deletarConta(idAgencia, idCliente, idConta);
//    }
//
//    @Test
//    void testDeletarConta_NotFound() {
//        Integer idAgencia = 1;
//        Integer idCliente = 1;
//        Integer idConta = 1;
//        doThrow(new RuntimeException()).when(contaService).deletarConta(idAgencia, idCliente, idConta);
//
//        ResponseEntity<Void> response = contaController.deletarConta(idAgencia, idCliente, idConta);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        verify(contaService, times(1)).deletarConta(idAgencia, idCliente, idConta);
//    }
//
//    @Test
//    void testDepositaraConta_Success() {
//        Integer idAgencia = 1;
//        Integer idCliente = 1;
//        Integer idConta = 1;
//        Double valor = 100.0;
//        ContaBancaria conta = new ContaBancaria();
//        when(contaService.depositar(idConta, valor)).thenReturn(conta);
//
//        Map<String, Double> request = new HashMap<>();
//        request.put("valor", valor);
//        ResponseEntity<ContaBancaria> response = contaController.depositar(idAgencia, idCliente, idConta, request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(conta, response.getBody());
//        verify(contaService, times(1)).depositar(idConta, valor);
//    }
//
//    @Test
//    void testSacarConta_Success() {
//        Integer idAgencia = 1;
//        Integer idCliente = 1;
//        Integer idConta = 1;
//        Double valor = 50.0;
//        ContaBancaria conta = new ContaBancaria();
//        when(contaService.sacar(idConta, valor)).thenReturn(conta);
//
//        Map<String, Double> request = new HashMap<>();
//        request.put("valor", valor);
//        ResponseEntity<ContaBancaria> response = contaController.sacar(idAgencia, idCliente, idConta, request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(conta, response.getBody());
//        verify(contaService, times(1)).sacar(idConta, valor);
//    }
//
//    @Test
//    void testTransferirEntreContas_Success() {
//        Integer idAgenciaRemetente = 1;
//        Integer idClienteRemetente = 1;
//        Integer idContaRemetente = 1;
//        Integer idAgenciaDestinataria = 2;
//        Integer idClienteDestinatario = 2;
//        Integer idContaDestinataria = 2;
//        Double valor = 200.0;
//
//        ResponseEntity<String> response = contaController.transferirEntreContas(idAgenciaRemetente, idClienteRemetente,
//                idContaRemetente, idAgenciaDestinataria, idClienteDestinatario, idContaDestinataria, valor);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Transferência realizada com sucesso.", response.getBody());
//        verify(contaService, times(1)).transferir(idAgenciaRemetente, idClienteRemetente, idContaRemetente,
//                idAgenciaDestinataria, idClienteDestinatario, idContaDestinataria, valor);
//    }
//
//}
//

package com.accenture.academico;

import com.accenture.academico.controller.ContaController;
import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContaControllerTest {

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarContasPorCliente_Success() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        ContaBancaria conta1 = new ContaBancaria();
        ContaBancaria conta2 = new ContaBancaria();
        when(contaService.listarContasPorCliente(idAgencia, idCliente)).thenReturn(Optional.of(Arrays.asList(conta1, conta2)));

        ResponseEntity<List<ContaBancaria>> response = contaController.listarContasPorCliente(idAgencia, idCliente);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(contaService, times(1)).listarContasPorCliente(idAgencia, idCliente);
    }

    @Test
    void testListarContasPorCliente_NotFound() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        when(contaService.listarContasPorCliente(idAgencia, idCliente)).thenReturn(Optional.empty());

        ResponseEntity<List<ContaBancaria>> response = contaController.listarContasPorCliente(idAgencia, idCliente);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(contaService, times(1)).listarContasPorCliente(idAgencia, idCliente);
    }

    @Test
    void testObterContaPorId_Success() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Integer idConta = 1;
        ContaBancaria conta = new ContaBancaria();
        when(contaService.obterContaPorId(idAgencia, idCliente, idConta)).thenReturn(Optional.of(conta));

        ResponseEntity<ContaBancaria> response = contaController.obterContaPorId(idAgencia, idCliente, idConta);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conta, response.getBody());
        verify(contaService, times(1)).obterContaPorId(idAgencia, idCliente, idConta);
    }

    @Test
    void testObterContaPorId_NotFound() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Integer idConta = 1;
        when(contaService.obterContaPorId(idAgencia, idCliente, idConta)).thenReturn(Optional.empty());

        ResponseEntity<ContaBancaria> response = contaController.obterContaPorId(idAgencia, idCliente, idConta);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(contaService, times(1)).obterContaPorId(idAgencia, idCliente, idConta);
    }

    @Test
    void testCriarConta_Success() throws Exception {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        ContaBancaria contaBancaria = new ContaBancaria();
        contaBancaria.setNumeroConta(12345);
        when(contaService.criarConta(idAgencia, idCliente, contaBancaria)).thenReturn(contaBancaria);

        ResponseEntity<ContaBancaria> response = contaController.criarConta(idAgencia, idCliente, contaBancaria);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("/bank/" + idAgencia + "/clientes/" + idCliente + "/contas/" + contaBancaria.getNumeroConta(), response.getHeaders().getLocation().toString());
        assertEquals(contaBancaria, response.getBody());
        verify(contaService, times(1)).criarConta(idAgencia, idCliente, contaBancaria);
    }

    @Test
    void testCriarConta_InternalServerError() throws Exception {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        ContaBancaria contaBancaria = new ContaBancaria();
        when(contaService.criarConta(idAgencia, idCliente, contaBancaria)).thenThrow(new RuntimeException());

        ResponseEntity<ContaBancaria> response = contaController.criarConta(idAgencia, idCliente, contaBancaria);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(contaService, times(1)).criarConta(idAgencia, idCliente, contaBancaria);
    }

    @Test
    void testDeletarConta_Success() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Integer idConta = 1;
        doNothing().when(contaService).deletarConta(idAgencia, idCliente, idConta);

        ResponseEntity<Void> response = contaController.deletarConta(idAgencia, idCliente, idConta);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(contaService, times(1)).deletarConta(idAgencia, idCliente, idConta);
    }

    @Test
    void testDeletarConta_NotFound() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Integer idConta = 1;
        doThrow(new RuntimeException()).when(contaService).deletarConta(idAgencia, idCliente, idConta);

        ResponseEntity<Void> response = contaController.deletarConta(idAgencia, idCliente, idConta);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(contaService, times(1)).deletarConta(idAgencia, idCliente, idConta);
    }

    @Test
    void testDepositaraConta_Success() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Integer idConta = 1;
        Double valor = 100.0;
        ContaBancaria conta = new ContaBancaria();
        when(contaService.depositar(idConta, valor)).thenReturn(conta);

        Map<String, Double> request = new HashMap<>();
        request.put("valor", valor);
        ResponseEntity<ContaBancaria> response = contaController.depositar(idAgencia, idCliente, idConta, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conta, response.getBody());
        verify(contaService, times(1)).depositar(idConta, valor);
    }

    @Test
    void testDepositaraConta_InternalServerError() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Integer idConta = 1;
        when(contaService.depositar(idConta, null)).thenThrow(new IllegalArgumentException("Valor não pode ser nulo"));

        Map<String, Double> request = new HashMap<>();
        request.put("valor", null);
        ResponseEntity<ContaBancaria> response = contaController.depositar(idAgencia, idCliente, idConta, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(contaService, times(1)).depositar(idConta, null);
    }

    @Test
    void testSacarConta_Success() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Integer idConta = 1;
        Double valor = 50.0;
        ContaBancaria conta = new ContaBancaria();
        when(contaService.sacar(idConta, valor)).thenReturn(conta);

        Map<String, Double> request = new HashMap<>();
        request.put("valor", valor);
        ResponseEntity<ContaBancaria> response = contaController.sacar(idAgencia, idCliente, idConta, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conta, response.getBody());
        verify(contaService, times(1)).sacar(idConta, valor);
    }

    @Test
    void testSacarConta_InternalServerError() {
        Integer idAgencia = 1;
        Integer idCliente = 1;
        Integer idConta = 1;
        when(contaService.sacar(idConta, null)).thenThrow(new IllegalArgumentException("Valor não pode ser nulo"));

        Map<String, Double> request = new HashMap<>();
        request.put("valor", null);
        ResponseEntity<ContaBancaria> response = contaController.sacar(idAgencia, idCliente, idConta, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(contaService, times(1)).sacar(idConta, null);
    }

    @Test
    void testTransferirEntreContas_Success() {
        Integer idAgenciaRemetente = 1;
        Integer idClienteRemetente = 1;
        Integer idContaRemetente = 1;
        Integer idAgenciaDestinataria = 2;
        Integer idClienteDestinatario = 2;
        Integer idContaDestinataria = 2;
        Double valor = 200.0;

        ResponseEntity<String> response = contaController.transferirEntreContas(idAgenciaRemetente, idClienteRemetente,
                idContaRemetente, idAgenciaDestinataria, idClienteDestinatario, idContaDestinataria, valor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transferência realizada com sucesso.", response.getBody());
        verify(contaService, times(1)).transferir(idAgenciaRemetente, idClienteRemetente, idContaRemetente,
                idAgenciaDestinataria, idClienteDestinatario, idContaDestinataria, valor);
    }

}
