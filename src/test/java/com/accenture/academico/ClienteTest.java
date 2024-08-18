package com.accenture.academico;

import com.accenture.academico.model.Agencia;
import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.model.EnderecoCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
    }

    @Test
    void testGetSetId() {
        cliente.setId(1);
        assertEquals(1, cliente.getId(), "O ID do cliente não corresponde ao esperado.");
    }

    @Test
    void testGetSetNome() {
        String nome = "Maria Silva";
        cliente.setNome(nome);
        assertEquals(nome, cliente.getNome(), "O nome do cliente não corresponde ao esperado.");
    }

    @Test
    void testGetSetCpf() {
        String cpf = "123.456.789-00";
        cliente.setCpf(cpf);
        assertEquals(cpf, cliente.getCpf(), "O CPF do cliente não corresponde ao esperado.");
    }

    @Test
    void testGetSetTelefone() {
        String telefone = "123456789";
        cliente.setTelefone(telefone);
        assertEquals(telefone, cliente.getTelefone(), "O telefone do cliente não corresponde ao esperado.");
    }

    @Test
    void testGetSetSenha() {
        String senha = "senha123";
        cliente.setSenha(senha);
        assertEquals(senha, cliente.getSenha(), "A senha do cliente não corresponde ao esperado.");
    }

    @Test
    void testGetSetAgencia() {
        Agencia agencia = new Agencia();
        agencia.setIdAgencia(1);
        cliente.setAgencia(agencia);
        assertEquals(agencia, cliente.getAgencia(), "A agência associada ao cliente não corresponde ao esperado.");
    }

    @Test
    void testGetSetEnderecoCliente() {
        EnderecoCliente endereco = new EnderecoCliente();
        cliente.setEnderecoCliente(endereco);
        assertEquals(endereco, cliente.getEnderecoCliente(), "O endereço do cliente não corresponde ao esperado.");
    }

    @Test
    void testGetSetContaBancarias() {
        List<ContaBancaria> contas = new ArrayList<>();
        ContaBancaria conta1 = new ContaBancaria();
        ContaBancaria conta2 = new ContaBancaria();
        contas.add(conta1);
        contas.add(conta2);

        cliente.setContaBancarias(contas);
        assertEquals(contas, cliente.getContaBancarias(), "As contas bancárias associadas ao cliente não correspondem ao esperado.");
        assertEquals(2, cliente.getContaBancarias().size(), "O número de contas bancárias não corresponde ao esperado.");
    }

    @Test
    void testAddRemoveContaBancaria() {
        ContaBancaria conta = new ContaBancaria();

        cliente.getContaBancarias().add(conta);
        assertTrue(cliente.getContaBancarias().contains(conta), "A conta bancária deveria estar associada ao cliente.");

        cliente.getContaBancarias().remove(conta);
        assertFalse(cliente.getContaBancarias().contains(conta), "A conta bancária deveria ter sido removida do cliente.");
    }
}
