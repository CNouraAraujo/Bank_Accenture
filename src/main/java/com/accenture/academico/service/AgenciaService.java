package com.accenture.academico.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.model.Agencia;
import com.accenture.academico.repository.AgenciaRepository;

@Service
public class AgenciaService {

//	@Autowired
//	private AgenciaRepository agenciaRepository;
//
//	public List<Agencia> listarAgencias() {
//		return agenciaRepository.findAll();
//	}
//
//	public Optional<Agencia> obterAgencia(Integer idAgencia) {
//		return agenciaRepository.findById(idAgencia);
//	}
//
//	public Agencia criarAgencia(Agencia agencia) {
//		return agenciaRepository.save(agencia);
//	}
//
//	public void deletarAgencia(Integer idAgencia) {
//		Agencia agencia = agenciaRepository.findById(idAgencia)
//				.orElseThrow(() -> new RuntimeException("Agência não encontrada"));
//		agenciaRepository.delete(agencia);
//	}
//	
//	 public Optional<Agencia> atualizarAgencia(Integer idAgencia, Agencia agencia) {
//	        Optional<Agencia> agenciaExistente = agenciaRepository.findById(idAgencia);
//
//	        if (agenciaExistente.isPresent()) {
//	            Agencia agenciaParaAtualizar = agenciaExistente.get();
//	            agenciaParaAtualizar.setNome(agencia.getNome());
//	            agenciaParaAtualizar.setTelefone(agencia.getTelefone());
//
//	            return Optional.of(agenciaRepository.save(agenciaParaAtualizar));
//	        } else {
//	            return Optional.empty();
//	        }
//	    }
	@Autowired
	private AgenciaRepository agenciaRepository;

	public List<Agencia> listarAgencias() {
		return agenciaRepository.findAll();
	}

	public Optional<Agencia> obterAgencia(Integer numeroAgencia) {
		return agenciaRepository.findByNumeroAgencia(numeroAgencia);
	}

	public Agencia criarAgencia(Agencia agencia) {
		return agenciaRepository.save(agencia);
	}

	public void deletarAgencia(Integer numeroAgencia) {
		if (agenciaRepository.existsByNumeroAgencia(numeroAgencia)) {
			agenciaRepository.deleteByNumeroAgencia(numeroAgencia);
		} else {
			throw new RuntimeException("Agência não encontrada.");
		}
	}

	public Optional<Agencia> atualizarAgencia(Integer numeroAgencia, Agencia agencia) {
		if (agenciaRepository.existsByNumeroAgencia(numeroAgencia)) {
			agencia.setNumeroAgencia(numeroAgencia);
			return Optional.of(agenciaRepository.save(agencia));
		} else {
			return Optional.empty();
		}
	}

}
