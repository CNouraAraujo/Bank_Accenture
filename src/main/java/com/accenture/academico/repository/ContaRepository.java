package com.accenture.academico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.academico.model.ContaBancaria;

@Repository
public interface ContaRepository extends JpaRepository<ContaBancaria, Integer> {
	 List<ContaBancaria> findByCliente_Id(Integer clienteId);
}
