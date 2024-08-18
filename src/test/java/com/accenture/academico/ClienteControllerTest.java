package com.accenture.academico;

import com.accenture.academico.controller.ClienteController;
import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.service.AgenciaService;
import com.accenture.academico.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private AgenciaService agenciaService;

    @Test
    void testListarClientesPorAgencia() throws Exception {
        Agencia agencia = new Agencia();
        agencia.setIdAgencia(1);

        Cliente cliente1 = new Cliente();
        cliente1.setId(1);
        cliente1.setNome("Cliente 1");
        cliente1.setCpf("11111111111");
        cliente1.setTelefone("999999999");

        Cliente cliente2 = new Cliente();
        cliente2.setId(2);
        cliente2.setNome("Cliente 2");
        cliente2.setCpf("22222222222");
        cliente2.setTelefone("888888888");

        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);

        when(agenciaService.obterAgencia(1)).thenReturn(Optional.of(agencia));
        when(clienteService.listarClientesPorAgencia(1)).thenReturn(clientes);

        mockMvc.perform(get("/bank/1/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Cliente 1"))
                .andExpect(jsonPath("$[1].nome").value("Cliente 2"));
    }

    @Test
    void testListarClientesPorAgencia_NotFound() throws Exception {
        when(agenciaService.obterAgencia(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/bank/1/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testObterClienteDeAgencia() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("Cliente 1");
        cliente.setCpf("11111111111");
        cliente.setTelefone("999999999");

        when(clienteService.obterClienteDeAgencia(1, 1)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/bank/1/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Cliente 1"))
                .andExpect(jsonPath("$.cpf").value("11111111111"));
    }

    @Test
    void testObterClienteDeAgencia_NotFound() throws Exception {
        when(clienteService.obterClienteDeAgencia(1, 1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/bank/1/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
