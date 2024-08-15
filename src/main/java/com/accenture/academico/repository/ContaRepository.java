package com.accenture.academico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.accenture.academico.model.ContaBancaria;

@Repository
public interface ContaRepository extends JpaRepository<ContaBancaria, Integer> {
    
    List<ContaBancaria> findByCliente_Id(Integer clienteId);
    
    @Query("SELECT c FROM ContaBancaria c WHERE c.numeroConta = :numeroConta AND c.cliente.id = :idCliente AND c.cliente.agencia.numeroAgencia = :numeroAgencia")
    Optional<ContaBancaria> findByNumeroContaAndClienteIdAndClienteAgenciaNumero(@Param("numeroConta") Integer numeroConta, 
                                                                                  @Param("idCliente") Integer idCliente, 
                                                                                  @Param("numeroAgencia") Integer numeroAgencia);
}
