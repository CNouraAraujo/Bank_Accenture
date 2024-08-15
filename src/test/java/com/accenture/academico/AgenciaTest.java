package com.accenture.academico;

import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AgenciaTest {

    @Test
    void contextLoads() {
        // Verifica se o contexto do Spring Boot é carregado corretamente.
    }

    @Test
    public void testAgenciaInitialization() {
        // Testa a criação de uma instância da classe Agencia usando o construtor padrão
        Agencia agencia = new Agencia();
        assertNotNull(agencia, "Agencia não deve ser nula após a criação.");
    }

    @Test
    public void testAgenciaParameterizedConstructor() {
        // Testa a criação de uma instância da classe Agencia usando o construtor com parâmetros
        Integer codigoBanco = 123;
        Agencia agencia = new Agencia(codigoBanco);
        assertEquals(codigoBanco, agencia.getIdAgencia(), "ID da Agencia não corresponde ao valor esperado.");
    }

    @Test
    public void testAgenciaProperties() {
        // Testa as propriedades da classe Agencia
        Agencia agencia = new Agencia();
        agencia.setIdAgencia(1);
        agencia.setNome("Agencia Central");
        agencia.setTelefone("123456789");

        assertEquals(1, agencia.getIdAgencia(), "ID da Agencia não corresponde ao valor esperado.");
        assertEquals("Agencia Central", agencia.getNome(), "Nome da Agencia não corresponde ao valor esperado.");
        assertEquals("123456789", agencia.getTelefone(), "Telefone da Agencia não corresponde ao valor esperado.");
    }

    @Test
    public void testAgenciaClientes() {
        // Testa a associação entre Agencia e Cliente
        Agencia agencia = new Agencia();
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();

        List<Cliente> clientes = new ArrayList<>();
        clientes.add(cliente1);
        clientes.add(cliente2);

        agencia.setClientes(clientes);

        assertNotNull(agencia.getClientes(), "Lista de clientes não deve ser nula.");
        assertEquals(2, agencia.getClientes().size(), "A quantidade de clientes não corresponde ao esperado.");
        assertSame(cliente1, agencia.getClientes().get(0), "O primeiro cliente não corresponde ao esperado.");
        assertSame(cliente2, agencia.getClientes().get(1), "O segundo cliente não corresponde ao esperado.");
    }
}
