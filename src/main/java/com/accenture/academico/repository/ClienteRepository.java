package com.accenture.academico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.Agencia;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // Adiciona um método para buscar clientes pelo número da agência
    List<Cliente> findAllByAgencia_NumeroAgencia(Integer numeroAgencia);

    // Adiciona um método para buscar cliente por ID e número da agência
    Optional<Cliente> findByIdAndAgencia_NumeroAgencia(Integer idCliente, Integer numeroAgencia);
    
    Optional<Cliente> findByAgencia_NumeroAgenciaAndCpfAndSenha(Integer numeroAgencia, String cpf, String senha);

}
