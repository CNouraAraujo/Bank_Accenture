package com.accenture.academico.repository;

import com.accenture.academico.model.OperacoesCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperacoesClienteRepository extends JpaRepository<OperacoesCliente, Integer> {

	// Método para buscar todas as operações realizadas por um cliente específico
	List<OperacoesCliente> findByCliente_Id(Integer idCliente);
}
