package com.accenture.academico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.academico.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	 List<Cliente> findAllByAgencia_IdAgencia(Integer idAgencia);

	 Optional<Cliente> findByIdAndAgencia_IdAgencia(Integer idCliente, Integer idAgencia);
}
