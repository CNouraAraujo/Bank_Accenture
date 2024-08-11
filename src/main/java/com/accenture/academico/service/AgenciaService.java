package com.accenture.academico.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.model.Agencia;
import com.accenture.academico.repository.AgenciaRepository;

@Service
public class AgenciaService {

	@Autowired
	private AgenciaRepository agenciaRepository;
	
	public List<Agencia> listarAgencias() {
        return agenciaRepository.findAll();
    }
	
	public Optional<Agencia> obterAgencia(Integer idAgencia) {
        return agenciaRepository.findById(idAgencia);
    }
	
	public Agencia criarAgencia(Agencia agencia) {
        return agenciaRepository.save(agencia);
    }
	
}
