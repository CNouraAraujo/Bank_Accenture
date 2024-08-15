package com.accenture.academico;

import com.accenture.academico.controller.AgenciaController;
import com.accenture.academico.model.Agencia;
import com.accenture.academico.service.AgenciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class AgenciaControllerTest {

    @Mock
    private AgenciaService agenciaService;

    @InjectMocks
    private AgenciaController agenciaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObterAgencia() {
        // Given
        Integer idAgencia = 1;
        Agencia agencia = new Agencia();
        when(agenciaService.obterAgencia(idAgencia)).thenReturn(Optional.of(agencia));

        // When
        ResponseEntity<Agencia> response = agenciaController.obterAgencia(idAgencia);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "O status HTTP não corresponde ao esperado.");
        assertEquals(agencia, response.getBody(), "A agência retornada não corresponde ao esperado.");
        verify(agenciaService, times(1)).obterAgencia(idAgencia);
    }

    @Test
    void testObterAgenciaNaoEncontrada() {
        // Given
        Integer idAgencia = 1;
        when(agenciaService.obterAgencia(idAgencia)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Agencia> response = agenciaController.obterAgencia(idAgencia);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "O status HTTP não corresponde ao esperado.");
        assertNull(response.getBody(), "O corpo da resposta não deve ser nulo.");
        verify(agenciaService, times(1)).obterAgencia(idAgencia);
    }

    @Test
    void testCriarAgencia() {
        // Given
        Agencia agencia = new Agencia();
        when(agenciaService.criarAgencia(agencia)).thenReturn(agencia);

        // When
        ResponseEntity<Agencia> response = agenciaController.criarAgencia(agencia);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "O status HTTP não corresponde ao esperado.");
        assertEquals(agencia, response.getBody(), "A agência retornada não corresponde ao esperado.");
        assertEquals(URI.create("/bank/" + agencia.getIdAgencia()), response.getHeaders().getLocation(), "A URI retornada não corresponde ao esperado.");
        verify(agenciaService, times(1)).criarAgencia(agencia);
    }

    @Test
    void testCriarAgenciaFalha() {
        // Given
        Agencia agencia = new Agencia();
        when(agenciaService.criarAgencia(agencia)).thenThrow(new RuntimeException());

        // When
        ResponseEntity<Agencia> response = agenciaController.criarAgencia(agencia);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "O status HTTP não corresponde ao esperado.");
        assertNull(response.getBody(), "O corpo da resposta deve ser nulo.");
        verify(agenciaService, times(1)).criarAgencia(agencia);
    }

    @Test
    void testDeletarAgencia() {
        // Given
        Integer idAgencia = 1;

        // When
        ResponseEntity<Void> response = agenciaController.deletarAgencia(idAgencia);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "O status HTTP não corresponde ao esperado.");
        verify(agenciaService, times(1)).deletarAgencia(idAgencia);
    }

    @Test
    void testDeletarAgenciaNaoEncontrada() {
        // Given
        Integer idAgencia = 1;
        doThrow(new RuntimeException("Agência não encontrada")).when(agenciaService).deletarAgencia(idAgencia);

        // When
        ResponseEntity<Void> response = agenciaController.deletarAgencia(idAgencia);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "O status HTTP não corresponde ao esperado.");
        verify(agenciaService, times(1)).deletarAgencia(idAgencia);
    }

    // Testes adicionais

    @Test
    void testCriarAgenciaComCamposNulos() {
        // Given
        Agencia agencia = new Agencia();
        when(agenciaService.criarAgencia(agencia)).thenReturn(agencia);

        // When
        ResponseEntity<Agencia> response = agenciaController.criarAgencia(agencia);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "O status HTTP não corresponde ao esperado.");
        assertEquals(agencia, response.getBody(), "A agência retornada não corresponde ao esperado.");
        verify(agenciaService, times(1)).criarAgencia(agencia);
    }

    @Test
    void testCriarAgenciaComExcecaoEspecifica() {
        // Given
        Agencia agencia = new Agencia();
        when(agenciaService.criarAgencia(agencia)).thenThrow(new IllegalArgumentException("Dados inválidos"));

        // When
        ResponseEntity<Agencia> response = agenciaController.criarAgencia(agencia);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "O status HTTP não corresponde ao esperado.");
        assertNull(response.getBody(), "O corpo da resposta deve ser nulo.");
        verify(agenciaService, times(1)).criarAgencia(agencia);
    }
}
