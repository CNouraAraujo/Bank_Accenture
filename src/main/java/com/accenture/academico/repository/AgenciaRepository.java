package com.accenture.academico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.academico.model.Agencia;

@Repository
public interface AgenciaRepository extends JpaRepository<Agencia, Integer> {
	
	Optional<Agencia> findByNumeroAgencia(Integer numeroAgencia);

    boolean existsByNumeroAgencia(Integer numeroAgencia);

    void deleteByNumeroAgencia(Integer numeroAgencia);
    
}
