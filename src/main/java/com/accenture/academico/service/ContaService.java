package com.accenture.academico.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.academico.model.Cliente;
import com.accenture.academico.model.ContaBancaria;
import com.accenture.academico.repository.ContaRepository;

@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private ClienteService clienteService;

	public ContaBancaria depositar(Integer idConta, Double valor) {
		ContaBancaria conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new RuntimeException("Conta não encontrada"));

		conta.depositar(valor);
		return contaRepository.save(conta);
	}

	public ContaBancaria sacar(Integer idConta, Double valor) {
		ContaBancaria conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new RuntimeException("Conta não encontrada"));
		conta.sacar(valor);
		return contaRepository.save(conta);
	}

	public void transferir(Integer idAgenciaRemetente, Integer idClienteRemetente, Integer idContaRemetente,
			Integer idAgenciaDestinataria, Integer idClienteDestinataria, Integer idContaDestinataria, Double valor) {
		if (valor <= 0) {
			throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
		}

		ContaBancaria contaRemetente = contaRepository.findById(idContaRemetente)
				.orElseThrow(() -> new IllegalArgumentException("Conta remetente não encontrada"));

		if (!contaRemetente.getCliente().getId().equals(idClienteRemetente)
				|| !contaRemetente.getCliente().getAgencia().getIdAgencia().equals(idAgenciaRemetente)) {
			throw new IllegalArgumentException("Conta remetente não pertence ao cliente ou agência corretos.");
		}

		if (contaRemetente.getSaldo() < valor) {
			throw new IllegalArgumentException("Saldo insuficiente na conta remetente.");
		}

		ContaBancaria contaDestinataria = contaRepository.findById(idContaDestinataria)
				.orElseThrow(() -> new IllegalArgumentException("Conta destinatária não encontrada"));

		if (!contaDestinataria.getCliente().getId().equals(idClienteDestinataria)
				|| !contaDestinataria.getCliente().getAgencia().getIdAgencia().equals(idAgenciaDestinataria)) {
			throw new IllegalArgumentException("Conta destinatária não pertence ao cliente ou agência corretos.");
		}

		contaRemetente.setSaldo(contaRemetente.getSaldo() - valor);
		contaDestinataria.setSaldo(contaDestinataria.getSaldo() + valor);

		contaRepository.save(contaRemetente);
		contaRepository.save(contaDestinataria);
	}
	
	public Optional<List<ContaBancaria>> listarContasPorCliente(Integer idAgencia, Integer idCliente) {
        return clienteService.obterClienteDeAgencia(idAgencia, idCliente)
                .map(Cliente::getContaBancarias);
    }
	
	public Optional<ContaBancaria> obterContaPorId(Integer idAgencia, Integer idCliente, Integer idConta) {
        return contaRepository.findByIdAndClienteIdAndClienteAgenciaId(idConta, idCliente, idAgencia);
    }

}
